package com.codecrafters;

import com.codecrafters.data.Config;
import com.codecrafters.data.Survey;
import com.codecrafters.export.VoteExportFormat;
import com.codecrafters.load.SurveyLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;

import java.util.Optional;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Survey survey = SurveyLoader.loadSurveyFromJson(Files.SURVEY);
        Config config = SurveyLoader.loadConfigFromJson(Files.CONFIG);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setConfig(config);
        mainController.showSurvey(survey);

        Scene scene = new Scene(root, 1000, 600);
        scene.setOnKeyPressed(event -> {
            if (KeyCodeCombination.keyCombination("Ctrl+E").match(event)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Export Format Selection");
                alert.setHeaderText("Export Votes");
                alert.setContentText("Choose the export format. The export result will be located in the MoodyFX install directory.");

                ButtonType buttonTypeJson = new ButtonType("JSON");
                ButtonType buttonTypeCsv = new ButtonType("CSV");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeJson, buttonTypeCsv, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == buttonTypeJson) {
                        mainController.exportSurveyWithVotes(VoteExportFormat.JSON);
                    } else if (result.get() == buttonTypeCsv) {
                        mainController.exportSurveyWithVotes(VoteExportFormat.CSV);
                    } else {
                        alert.close();
                    }
                }
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
