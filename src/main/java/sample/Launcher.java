package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import sample.data.Config;
import sample.data.Survey;
import sample.load.SurveyLoader;

import java.io.File;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Survey survey = SurveyLoader.loadSurveyFromJson(new File("survey.json"));
        Config config = SurveyLoader.loadConfigFromJson(new File("config.json"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setConfig(config);
        mainController.showSurvey(survey);

        Scene scene = new Scene(root, 1000, 600);
        scene.setOnKeyPressed(event -> {
            if (KeyCodeCombination.keyCombination("Ctrl+E").match(event)) {
                mainController.exportSurveyWithVotes();
            }
        });

        primaryStage.setTitle("Moody");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
