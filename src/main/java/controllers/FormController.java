package controllers;

import com.gluonhq.charm.glisten.control.TextField;
import com.jfoenix.controls.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import model.Action;
import model.Anomalie;
import model.Emeteur;
import model.Fiche;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import services.FicheService;
import tools.Constants;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FormController implements Initializable,EventHandler<Event>,Validator<String> {
    @Getter
    @Setter
    private static Fiche fiche;
    @FXML
    StackPane root;
    @FXML
    JFXTextField email;
    @FXML
    JFXTextField fName;
    @FXML
    JFXTextField lName;
    @FXML
    JFXRadioButton interne;
    @FXML
    JFXRadioButton externe;
    @FXML
    JFXButton suivant;
    @FXML
    JFXTextField processus;
    @FXML
    JFXButton annuler;
    @FXML
    JFXListView<AnchorPane> listAnomalies;
    @FXML JFXButton suivant2;
    @FXML JFXButton annuler2;
    @FXML JFXButton precedent;
    @FXML JFXButton suivant3;
    @FXML JFXButton annuler3;
    @FXML JFXButton annuler4;
    @FXML JFXButton precedent2;
    @FXML JFXButton precedent3;
    @FXML JFXButton ignorer;
    @FXML JFXButton terminer;
    @FXML JFXRadioButton nonConfirmiteTrue;
    @FXML JFXRadioButton nonConfirmiteFalse;
    @FXML JFXTextArea remarqueStatut;
    @FXML JFXRadioButton correction;
    @FXML JFXRadioButton rebut;
    @FXML JFXRadioButton derogation;
    @FXML JFXTextArea remarqueAction;
    ValidationSupport validationSupport;
    JFXTextArea autreText;
    JFXRadioButton autre;
    List<JFXRadioButton> radioButtons;
    public static List<Anomalie> anomalies;
    JFXButton okay;
    int page = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(suivant!=null) page = 1;
        if (suivant2!=null) page = 2;
        if( suivant3 != null) page = 3;
        if(terminer != null) page = 4;

        validationSupport = new ValidationSupport();
        if(listAnomalies!=null){
            if(anomalies==null)
            anomalies = FicheService.getInstance().getFreqAnomalie();
            radioButtons = new ArrayList<>();
            for (Anomalie anomaly : anomalies) {
                listAnomalies.setOnMouseClicked(this);
                JFXRadioButton radioButton = new JFXRadioButton();
                radioButton.setText(anomaly.getDesc());
                radioButton.getStyleClass().add("listeViewItem");
                radioButton.setSelectedColor(Color.valueOf("#FA2C56"));
                radioButton.setUnSelectedColor(Color.valueOf("#d4d0cf"));
                radioButton.setTextFill(Paint.valueOf("#d4d0cf"));
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(radioButton);
                radioButton.setOnAction(this::handle);
                listAnomalies.getItems().add(anchorPane);
                radioButtons.add(radioButton);
            }
            autre = new JFXRadioButton();
            autre.setText("autres");
            autre.getStyleClass().add("listeViewItem");
            autre.setSelectedColor(Color.valueOf("#FA2C56"));
            autre.setUnSelectedColor(Color.valueOf("#d4d0cf"));
            autre.setTextFill(Paint.valueOf("#d4d0cf"));
            autre.setOnAction(this::handle);
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(autre);
            autreText = new JFXTextArea();
            autreText.setMaxHeight(100);
            autreText.setMaxWidth(500);
            autreText.setPadding(new Insets(10,0,0,0));
            autreText.setLayoutY(autre.getLayoutY()+autre.getHeight()+10);
            autreText.setDisable(true);
            autreText.setLayoutY(25);
            autreText.setLayoutX(50);
            autreText.setStyle("-fx-font-size: 16; -fx-text-fill: #FFFF; -jfx-unfocus-color: #d4d0cf; -jfx-focus-color: #FA2C56;");
            anchorPane.getChildren().add(autreText);
            listAnomalies.getItems().add(anchorPane);
            radioButtons.add(autre);
        }
        if (fiche==null) fiche = new Fiche();
        fullFill();
        validationSupport.revalidate();
        validationSupport.initInitialDecoration();
    }




    @Override
    public void handle(Event actionEvent) {
        Object source = actionEvent.getSource();
        if(source == interne){
            externe.setSelected(false);
            return;
        }
        if(source == externe){
            interne.setSelected(false);
            return;
        }
        if(source==suivant){
            //get the data
            Emeteur emeteur = new Emeteur();
            emeteur.setEmail(email.getText());
            emeteur.setFName(fName.getText());
            emeteur.setLName(lName.getText());
            emeteur.setUserType((interne.isSelected()?"interne":(externe.isSelected()?"externe":null)));
            fiche.setEmeteur(emeteur);
            validationSupport.getValidationResult().getErrors().forEach(s-> System.out.println(s.getText()));

            //go to next page
            try {
                Constants.navigate("../ConstatAnomalie.fxml",(Stage) suivant.getScene().getWindow(),"Gestion d'anomalies");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(source == annuler ||source==annuler2 ||source==annuler3 ||source == annuler4){
            try {
                Constants.navigate("../main.fxml",(Stage) ((JFXButton)source).getScene().getWindow(),"Gestion d'anomalies");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(source == listAnomalies ||(radioButtons!=null && radioButtons.contains(source))) {
            radioButtons.forEach(r->r.setSelected(false));
            if(source == listAnomalies)
            radioButtons.get(listAnomalies.getSelectionModel().getSelectedIndex()).setSelected(true);
            else {
                ((JFXRadioButton)source).setSelected(true);
            }
        }
        if(autre!=null){
            autreText.setDisable(!autre.isSelected());
            validationSupport.revalidate();
            validationSupport.initInitialDecoration();
        }
        if(source==suivant2){
            fiche.setProcess(processus.getText());
            String description = "";
            Anomalie anomalie = null;
            if(autre.isSelected()){
                description = autreText.getText();
                anomalie = new Anomalie();
                anomalie.setDesc(description);

            }
            else {
                for (JFXRadioButton radioButton : radioButtons) {
                    if(radioButton.isSelected()){
                        description = radioButton.getText();
                        int i = radioButtons.indexOf(radioButton);
                        anomalie = anomalies.get(i);
                        break;
                    }
                }
            }

            fiche.setAnomalie(anomalie);
            try {
                Constants.navigate("../Statut.fxml",(Stage) ((JFXButton)source).getScene().getWindow(),"Statut");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(source == nonConfirmiteFalse){
            nonConfirmiteFalse.setSelected(true);
            nonConfirmiteTrue.setSelected(false);
        }if(source == nonConfirmiteTrue){
            nonConfirmiteTrue.setSelected(true);
            nonConfirmiteFalse.setSelected(false);
        }
        if(source == rebut){
            rebut.setSelected(true);
            derogation.setSelected(false);
            correction.setSelected(false);
        }
        if(source == correction){
            rebut.setSelected(false);
            derogation.setSelected(false);
            correction.setSelected(true);
        }
        if(source == derogation){
            rebut.setSelected(false);
            derogation.setSelected(true);
            correction.setSelected(false);
        }
        if(source==suivant3){
            fiche.setNonConfirmite(nonConfirmiteTrue.isSelected() || !nonConfirmiteFalse.isSelected());
            fiche.setRemarqueStatut(remarqueStatut.getText());
            try {
                Constants.navigate("../Action.fxml",(Stage) suivant3.getScene().getWindow(),"Action");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (precedent == source){
            try {
                Constants.navigate("../emeteur.fxml",(Stage) precedent.getScene().getWindow(),"Emeteur");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(precedent2==source){
            try {
                Constants.navigate("../ConstatAnomalie.fxml",(Stage) precedent2.getScene().getWindow(),"Gestion d'anomalies");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(precedent3==source){
            try {
                Constants.navigate("../Statut.fxml",(Stage) precedent3.getScene().getWindow(),"Statut");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(terminer == source){
            Action action = new Action();
            action.setName(correction.isSelected()?correction.getText():(rebut.isSelected()?rebut.getText():(derogation.isSelected()?derogation.getText():null)));
            action.setRemarque(remarqueAction.getText());
            fiche.setAction(action);
            fiche.setDate(new Date());
            FicheService.getInstance().save(fiche);
            fiche = null;
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            jfxDialogLayout.setBody(new Text("Operation done!"));
            okay = new JFXButton();
            okay.setText("Okay");
            okay.setStyle("-fx-background-color: #FA2C56; -fx-background-radius: 7; ");
            okay.setOnAction(this::handle);
            JFXDialog jfxDialog = new JFXDialog(root,jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxDialogLayout.setActions(okay);
            jfxDialog.show();
        }
        if(source==okay){
            try {
                Constants.navigate("../main.fxml",(Stage) ((JFXButton)source).getScene().getWindow(),"Gestion d'anomalies");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void fullFill(){
        if(fiche!=null){
            switch (page){
                case 1:
                    if(fiche.getEmeteur()!=null){
                        fName.setText(fiche.getEmeteur().getFName());
                        lName.setText(fiche.getEmeteur().getLName());
                        email.setText(fiche.getEmeteur().getEmail());
                        interne.setSelected("interne".equals(fiche.getEmeteur().getUserType()));
                        externe.setSelected("externe".equals(fiche.getEmeteur().getUserType()));
                    }
                    validationSupport.registerValidator( email, true, this );
                    validationSupport.registerValidator(fName,true,this);
                    validationSupport.registerValidator(lName,true,this);
                    break;
                case 2:
                    if(fiche.getAnomalie()!=null ){
                        processus.setText(fiche.getProcess());
                        if(fiche.getAnomalie().getFreq()!=null && fiche.getAnomalie().getFreq()){
                            for (JFXRadioButton radioButton : radioButtons) {
                                if(radioButton.getText().equals(fiche.getAnomalie().getDesc())){
                                    radioButton.setSelected(true);
                                    break;
                                }

                            }
                        }

                        else{
                            autre.setSelected(true);
                            autreText.setText(fiche.getAnomalie().getDesc());
                        }
                    }
                    validationSupport.registerValidator(processus,true,this);
                    validationSupport.registerValidator(autreText,true,this);
                    break;
                case 3:
                    nonConfirmiteTrue.setSelected((fiche.getNonConfirmite()!=null && fiche.getNonConfirmite()));
                    nonConfirmiteFalse.setSelected((fiche.getNonConfirmite() !=null && !fiche.getNonConfirmite()));
                    remarqueStatut.setText(fiche.getRemarqueStatut());
                    break;
                case 4:
                    if(fiche.getAction()!=null){
                        rebut.setSelected(rebut.getText().equals(fiche.getAction().getName()));
                        correction.setSelected(correction.getText().equals(fiche.getAction().getName()));
                        derogation.setSelected(derogation.getText().equals(fiche.getAction().getName()));
                        remarqueAction.setText(fiche.getAction().getRemarque());
                    }

                    break;
                default:
                    break;
            }
        }

    }
    @Override
    public ValidationResult apply(Control control, String value) {
        boolean condition = true;
        String message = "";
        if(control==email){
            condition =
                    value != null
                            ? !value
                            .matches(
                                    "^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$" )
                            : value == null;


            message = "Not an email";
        }
        if(control==fName||control==lName||control == processus|| (control==autreText && autre.isSelected())){
            condition =
                    value != null
                            ? value.isEmpty()
                            : value == null;


            message="This field should not be empty";
        }
        if(control==autreText && !autre.isSelected()) condition = false;

        return ValidationResult.fromMessageIf( control, message, Severity.WARNING, condition );
    }
}