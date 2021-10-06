package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import model.Fiche;
import services.FicheService;
import tools.Constants;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListController implements Initializable, EventHandler<Event> {

    @FXML
    private VBox pnItems;
    @FXML
    private StackPane root;
    @FXML
    private JFXButton backButton;
    private List<JFXButton> listSupprimer;
    private List<JFXButton> listmodifier ;
    private List<JFXButton> listImprimer ;
    private List<Fiche> fiches;
    private List<Node> nodes;
    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listmodifier = new ArrayList<>();
        listImprimer = new ArrayList<>();
        listSupprimer = new ArrayList<>();
        fiches = FicheService.getInstance().getAllFiches();
        nodes = new ArrayList<>();
        loadList();


    }

    private void getButtons(Pane box){

        listmodifier.add((JFXButton) box.lookup("#modifier"));
        listSupprimer.add((JFXButton) box.lookup("#supprimer"));
        listImprimer.add((JFXButton) box.lookup("#imprimer"));

    }


    @SneakyThrows
    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        if(listSupprimer.contains(source)){
            System.out.println("supprimer l'eleement "+listSupprimer.indexOf(source));
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            jfxDialogLayout.setBody(new Text("Vous voulez supprimer la fiche numéro "+ fiches.get(listSupprimer.indexOf(source)).getId()+ " de la liste des fiches ? "));
            JFXDialog jfxDialog = new JFXDialog(root,jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            JFXButton okay = new JFXButton();
            okay.setText("Supprimer");
            okay.setStyle("-fx-background-color: #FA2C56; -fx-background-radius: 7;");
            okay.setPrefHeight(30);
            okay.setPrefWidth(80);
            final Integer integer = listSupprimer.indexOf(source);
            okay.setOnAction(new EventHandler<ActionEvent>() {
                @SneakyThrows
                @Override
                public void handle(ActionEvent actionEvent) {
                    Fiche fiche = fiches.get(integer);
                    FicheService.getInstance().deleteFiche(fiche);
                    fiches.remove(fiche);
                    jfxDialog.close();
                    Node node = nodes.get(integer);
                    node.setVisible(false);
                    loadList();
                }
            });

            JFXButton annuler = new JFXButton();
            annuler.setText("Annuler");
            annuler.setStyle("-fx-background-color: #5f5e5e; -fx-background-radius: 7;");
            annuler.setPrefHeight(30);
            annuler.setPrefWidth(80);
            annuler.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    jfxDialog.close();
                }
            });

            HBox hBox = new HBox();

            hBox.getChildren().add(annuler);
            hBox.getChildren().add(okay);
            hBox.setSpacing(5);
            jfxDialogLayout.setActions(hBox);
            jfxDialog.show();

        }if(listImprimer.contains(source)){
            DirectoryChooser dirChooser = new DirectoryChooser();

            dirChooser.setTitle("Select a folder");

            String selectedDirPath = dirChooser.showDialog(((JFXButton)source).getScene().getWindow()).getAbsolutePath();
            Thread thread = new Thread(
                    new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            String path = FicheService.getInstance().imprimer(selectedDirPath, fiches.get(listImprimer.indexOf(source)));
                            Platform.setImplicitExit(false);
                            Platform.runLater(
                                    () -> {
                                        showDialog(source,path);
                                    }
                            );
                        }
                    }
            );
            thread.start();

        }if(listmodifier.contains(source)){
            FormController.setFiche(fiches.get(listmodifier.indexOf(source)));
            FormController.setSourcePage("../list.fxml");
            Constants.navigate("../emeteur.fxml",(Stage) pnItems.getScene().getWindow(),"Emeteur");
        }
        if(source == backButton){

            Constants.navigate("../main.fxml",(Stage) pnItems.getScene().getWindow(),"Gestion d'anomalies");
        }
    }
    private Node loadNode(Fiche fiche) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("../Item.fxml"));
        //give the items some effect
        node.setOnMouseEntered(event -> {
            node.setStyle("-fx-background-color : #0A0E3F");
        });
        node.setOnMouseExited(event -> {
            node.setStyle("-fx-background-color : #2F2B43");
        });

        getButtons((Pane) node);
        fullFillData(fiche, (Pane) node);
        return node;
    }
    private void fullFillData(Fiche fiche,Pane pane){
        ((Label)pane.lookup("#NumFiche")).setText(fiche.getId()!=null?fiche.getId()+"":"-");
        ((Label)pane.lookup("#dateFiche")).setText(fiche.getDateFormatted()!=null? fiche.getDateFormatted():"-");
        ((Label)pane.lookup("#process")).setText(fiche.getProcess()!=null?fiche.getProcess():"-");
        ((Label)pane.lookup("#emeteur")).setText(fiche.getEmeteur()!=null?(fiche.getEmeteur().getFullName()!=null?fiche.getEmeteur().getFullName():"-"):"-");
        ((Label)pane.lookup("#anomalie")).setText(fiche.getAnomalie()!=null?(fiche.getAnomalie().getDesc()!=null?fiche.getAnomalie().getDesc():"-")+"":"-");
        ((Label)pane.lookup("#nonConf")).setText(fiche.getNonConfirmite()!=null?(fiche.getNonConfirmite()?"oui":"non"):"-");
        ((Label)pane.lookup("#action")).setText(fiche.getAction()!=null?fiche.getAction().getName()!=null?fiche.getAction().getName():"-":"-");
    }
    private void loadList() throws IOException {
        pnItems.getChildren().clear();
        listSupprimer.clear();
        listImprimer.clear();
        listSupprimer.clear();
        for (Fiche fiche1 : fiches) {

            Node node = loadNode(fiche1);
            nodes.add(node);
            pnItems.getChildren().add(node);
        }
        listImprimer.forEach(s->s.setOnAction(this::handle));
        listmodifier.forEach(s->s.setOnAction(this::handle));
        listSupprimer.forEach(s->s.setOnAction(this::handle));
    }

    public void showDialog(Object source,String path){
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text("la fiche numéro  "+ fiches.get(listImprimer.indexOf(source)).getId()+ " a été sauvegardé ? "));
        JFXDialog jfxDialog = new JFXDialog(root,jfxDialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton ouvrir = new JFXButton();
        ouvrir.setText("Ouvrir le fichier");
        ouvrir.setStyle("-fx-background-color: #FA2C56; -fx-background-radius: 7;");
        ouvrir.setPrefHeight(30);
        ouvrir.setPrefWidth(80);
        ouvrir.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                jfxDialog.close();
                Desktop.getDesktop().open(new File(path));
            }
        });

        JFXButton annuler = new JFXButton();
        annuler.setText("Fermer");
        annuler.setStyle("-fx-background-color: #5f5e5e; -fx-background-radius: 7;");
        annuler.setPrefHeight(30);
        annuler.setPrefWidth(80);
        annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jfxDialog.close();
            }
        });
        HBox hBox = new HBox();

        hBox.getChildren().add(annuler);
        hBox.getChildren().add(ouvrir);
        hBox.setSpacing(5);
        jfxDialogLayout.setActions(hBox);
        jfxDialog.show();

    }

}
