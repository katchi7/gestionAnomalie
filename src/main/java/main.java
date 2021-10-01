import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.DataBroker;

import java.io.IOException;

public class main extends Application {

    public static void main(String [] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        DataBroker dataBroker = DataBroker.getInstance();
        primaryStage.setTitle("Hello World!");
        Parent parent = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene main = new Scene(parent);
        primaryStage.setScene(main);
        primaryStage.show();
    }
}
