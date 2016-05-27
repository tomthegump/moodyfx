package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.data.Survey;
import sample.data.Vote;
import sample.persistence.SurveyDatabaseHelper;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Survey survey = new Survey(1, "How are you today?", Survey.AnswerType.HAND);

        SurveyDatabaseHelper surveyDatabaseHelper = new SurveyDatabaseHelper();
//        surveyDatabaseHelper.insert(survey, System.out::println, System.err::println);
//        surveyDatabaseHelper.selectAllSurveys().subscribe(System.out::println);

//        Vote vote = new Vote(survey.getId(), 4, "C2-Team");
//        surveyDatabaseHelper.insert(vote, System.out::println, System.err::println);
//        surveyDatabaseHelper.selectAllVotes().subscribe(System.out::println);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setVoteStore((surveyId, votedPoints) ->
                        surveyDatabaseHelper.insert(new Vote(surveyId, votedPoints, "C2-Team"),
                        result -> System.out.println("Voted Successfully"), System.err::println));
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
