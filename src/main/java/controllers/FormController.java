package controllers;

import com.gluonhq.charm.glisten.control.TextField;
import com.jfoenix.controls.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Action;
import model.Anomalie;
import model.Emeteur;
import model.Fiche;
import services.FicheService;
import tools.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormController implements Initializable,EventHandler<Event> {
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
    TextField processus;
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
    JFXTextArea autreText;
    JFXRadioButton autre;
    List<JFXRadioButton> radioButtons;
    JFXButton okay;
    int page = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(suivant!=null) page = 1;
        if (suivant2!=null) page = 2;
        if( suivant3 != null) page = 3;
        if(terminer != null) page = 4;


        if(listAnomalies!=null){
            radioButtons = new ArrayList<>();
            for(int i = 0;i<50;i++){
                listAnomalies.setOnMouseClicked(this);
                JFXRadioButton radioButton = new JFXRadioButton();
                radioButton.setText("item "+i);
                radioButton.getStyleClass().add("listeViewItem");
                radioButton.setSelectedColor(Color.valueOf("#FA2C56"));
                radioButton.setUnSelectedColor(Color.valueOf("#d4d0cf"));
                radioButton.setTextFill(Paint.valueOf("#d4d0cf"));
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(radioButton);
                radioButton.setOnAction(this::handle);
                System.out.println("item "+i +" Added");
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
            autreText.setLayoutY(autre.getLayoutY()+autre.getHeight()+10);
            autreText.setDisable(true);
            anchorPane.getChildren().add(autreText);
            listAnomalies.getItems().add(anchorPane);
            radioButtons.add(autre);
        }
        if (fiche==null) fiche = new Fiche();
        fullFill();
    }




    @Override
    public void handle(Event actionEvent) {
        System.out.println("Click");
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
            System.out.println("clicked");
            radioButtons.forEach(r->r.setSelected(false));
            if(source == listAnomalies)
            radioButtons.get(listAnomalies.getSelectionModel().getSelectedIndex()).setSelected(true);
            else {
                ((JFXRadioButton)source).setSelected(true);
            }
        }
        if(autre!=null){
            autreText.setDisable(!autre.isSelected());
        }
        if(source==suivant2){
            fiche.setProcess(processus.getText());
            String description = "";
            if(autre.isSelected()) description = autreText.getText();
            else {
                for (JFXRadioButton radioButton : radioButtons) {
                    if(radioButton.isSelected()){
                        description = radioButton.getText();
                        break;
                    }
                }
            }
            Anomalie anomalie = new Anomalie();
            anomalie.setDesc(description);
            fiche.setAnomalie(anomalie);
            try {
                Constants.navigate("../Statut.fxml",(Stage) ((JFXButton)source).getScene().getWindow(),"Statut");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            System.out.println(fiche);
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
                    break;
                case 2:
                    if(fiche.getAnomalie()!=null ){
                        processus.setText(fiche.getProcess());
                        boolean selected = false;
                        for (JFXRadioButton radioButton : radioButtons) {
                            if(radioButton.getText().equals(fiche.getAnomalie().getDesc())){
                                radioButton.setSelected(true);
                                selected = true;
                                break;
                            }

                        }
                        if(!selected && fiche.getAnomalie()!=null&& fiche.getAnomalie().getDesc()!=null){
                            autre.setSelected(true);
                            autreText.setText(fiche.getAnomalie().getDesc());
                        }
                    }
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
}