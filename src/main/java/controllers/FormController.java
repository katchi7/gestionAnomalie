package controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import tools.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormController implements Initializable {
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
    JFXButton annuler;
    @FXML
    JFXListView<JFXRadioButton> listAnomalies;
    @FXML
    JFXTextArea autre;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(listAnomalies!=null){
            for(int i = 0;i<50;i++){
                JFXRadioButton radioButton = new JFXRadioButton();
                radioButton.setText("item "+i);
                radioButton.getStyleClass().add("listeViewItem");
                radioButton.setSelectedColor(Color.valueOf("#FA2C56"));
                radioButton.setUnSelectedColor(Color.valueOf("#d4d0cf"));
                radioButton.setTextFill(Paint.valueOf("#d4d0cf"));
                System.out.println("item "+i +" Added");
                listAnomalies.getItems().add(radioButton);
            }
        }
    }
    public void onClickListener(ActionEvent event) throws IOException {
        Object source = event.getSource();
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
            System.out.println("Name : " + fName.getText());
            System.out.println("Last Name : "+ lName.getText() );
            System.out.println("Email : " + email.getText());
            System.out.println("Status "+(interne.isSelected()?"interne":(externe.isSelected()?"externe":"null")));
            //go to next page
            Constants.navigate("../ConstatAnomalie.fxml",(Stage) suivant.getScene().getWindow(),"Gestion d'anomalies");
        }
        if(source == annuler){
            Constants.navigate("../main.fxml",(Stage) annuler.getScene().getWindow(),"Gestion d'anomalies");
        }


    }
}
