package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import sample.data.Survey;
import sample.data.Vote;
import sample.export.JsonExportFile;
import sample.persistence.SurveyDatabaseHelper;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Survey survey = new Survey(1, "How are you today?", Survey.AnswerType.HAND);

        SurveyDatabaseHelper surveyDatabaseHelper = new SurveyDatabaseHelper();
//        surveyDatabaseHelper.insert(survey, System.out::println, System.err::println);
//        surveyDatabaseHelper.selectAllSurveys().subscribe(System.out::println);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setVoteStore((surveyId, votedPoints) ->
                surveyDatabaseHelper.insert(new Vote(surveyId, votedPoints, "C2-Team"),
                        result -> System.out.println("Voted Successfully"), System.err::println));
        controller.showSurvey(survey);

        Scene scene = new Scene(root, 1000, 600);
        scene.setOnKeyPressed(event -> {
            if (KeyCodeCombination.keyCombination("Ctrl+E").match(event)) {
                exportVotes(survey.getId(), surveyDatabaseHelper);
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

    private void exportVotes(int surveyId, SurveyDatabaseHelper surveyDatabaseHelper) {
        try {
            JsonExportFile exportFile = JsonExportFile.createExportFile("votes", new File("export.json"));
            surveyDatabaseHelper.selectAllVotesForSurvey(surveyId).map(Main::mapToJson).subscribe(exportFile::append);
            exportFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String mapToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
