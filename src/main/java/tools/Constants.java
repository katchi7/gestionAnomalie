package tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Constants {
    public static void navigate(String fxmlPath, Stage stage,String sceneTitle) throws IOException {
        Parent parent = FXMLLoader.load(Constants.class.getResource(fxmlPath));
        if(parent!=null){
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(sceneTitle);
            stage.show();
        }
    }
}
