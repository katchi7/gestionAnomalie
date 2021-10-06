
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import model.Anomalie;
import services.FicheService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class main extends Application {

    public static void main(String [] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        initData();
        primaryStage.setTitle("Hello World!");
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        Scene main = new Scene(parent);
        primaryStage.setScene(main);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
    @SneakyThrows
    public void initData(){
        List<Anomalie> anomalies =  FicheService.getInstance().getFreqAnomalie();
        if(anomalies.isEmpty()){
            List<String> names = getFreqAnomaliesNames();
            for (String name : names) {
                Anomalie anomalie = new Anomalie();
                anomalie.setDesc(name);
                anomalie.setFreq(true);
                anomalies.add(anomalie);
            }
            FicheService.getInstance().saveAllAnomalies(anomalies);
        }
    }
    public List<String> getFreqAnomaliesNames() throws IOException {
        List<String> names = new ArrayList<>();
        BufferedInputStream inputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/Assets/AnomaliesFreq.txt")));
        byte[] buffer;
        buffer = inputStream.readAllBytes();
        String input = new String(buffer);
        String[] inputSplitted = input.split("\n");
        for (String s : inputSplitted) {
            s=s.replace("\n","");
            names.add(s);
        }
        return names;
    }
}
