package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import sample.data.Survey;
import sample.data.Vote;
import sample.export.JsonExportFile;
import sample.persistence.SurveyDatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final int THANKS_VIEW_DISPLAY_DURATION_IN_SECONDS = 5;
    private static final int MILLIS_PER_SECOND = 1000;

    @FXML
    private AnchorPane contentPane;

    private SurveyDatabaseHelper surveyDatabaseHelper;

    private SurveyController surveyController;
    private Parent surveyView;
    private Parent thanksView;

    private Survey survey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            surveyDatabaseHelper = new SurveyDatabaseHelper();
            initializeSurveyView();
            initializeThanksView();
            setView(surveyView);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeSurveyView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("survey.fxml"));
        surveyView = fxmlLoader.load();
        surveyController = fxmlLoader.getController();
        surveyController.setVoteStore(this::storeVotesAndShowThanksView);
    }

    private void initializeThanksView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thanks.fxml"));
        thanksView = fxmlLoader.load();
    }

    private void setView(Parent view) {
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        contentPane.getChildren().setAll(view);
    }

    public void showSurvey(Survey survey) {
        this.survey = survey;
        surveyController.showSurvey(survey);
    }

    private void storeVotesAndShowThanksView(int surveyId, int votedPoints) {
        showThanksView(THANKS_VIEW_DISPLAY_DURATION_IN_SECONDS);
        storeVotes(surveyId, votedPoints);
    }

    private void showThanksView(int seconds) {
        setView(thanksView);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(seconds * MILLIS_PER_SECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> setView(surveyView));
            }
        }.start();
    }

    private void storeVotes(int surveyId, int votedPoints) {
        surveyDatabaseHelper.insert(new Vote(surveyId, votedPoints, "C2-Team"),
                result -> System.out.println("Voted Successfully"), System.err::println);
    }

    public void exportVotes() {
        try {
            JsonExportFile exportFile = JsonExportFile.createExportFile("votes", new File("export.json"));
            surveyDatabaseHelper.selectAllVotesForSurvey(survey.getId()).map(MainController::mapToJson).subscribe(exportFile::append);
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
}
