package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import sample.persistence.SurveyDatabaseHelper;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Survey survey = new Survey(1, "How are you today?", Survey.AnswerType.HAND);
        
        SurveyDatabaseHelper surveyDatabaseHelper = new SurveyDatabaseHelper();
        surveyDatabaseHelper.insert(survey, System.out::println, System.err::println);
        surveyDatabaseHelper.queryAllSurveys().subscribe(System.out::println);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.showSurvey(survey);

        primaryStage.setTitle("Moody");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
