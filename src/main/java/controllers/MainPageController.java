package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tools.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML Button listeSignalements;
    @FXML Button creerSignalement;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void handleClick(ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==creerSignalement){
            Constants.navigate("../emeteur.fxml",(Stage) creerSignalement.getScene().getWindow(),"Gestion d'anomalie");
        }
        if(actionEvent.getSource()==listeSignalements){
            Constants.navigate("../list.fxml",(Stage) creerSignalement.getScene().getWindow(),"Gestion d'anomalie");
        }


    }

}
