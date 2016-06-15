package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import rx.Observable;
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

    private static final int THANKS_VIEW_DISPLAY_DURATION_IN_SECONDS = 15;
    private static final int VIEW_TRANSITION_DURATION_IN_SECONDS = 3;
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
        Node node = null;
        int viewTransitionDuration = VIEW_TRANSITION_DURATION_IN_SECONDS * MILLIS_PER_SECOND;
        try {
            node = contentPane.getChildren()
                    .get(0);
            viewTransitionDuration /= 2;
        } catch (IndexOutOfBoundsException ignored) {
        }

        // fade out
        if (node != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(viewTransitionDuration), node);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
        }

        // switch
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        view.setVisible(false);

        // fade in
        contentPane.getChildren()
                .setAll(view);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(viewTransitionDuration), view);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        view.setVisible(true);
        fadeIn.play();
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

    public void exportSurveyWithVotes() {
        surveyToJson().concatWith(votesToJsonArray())
                .reduce((survey, votes) -> survey + ", " + votes)
                .map(json -> "{" + json + "}")
                .subscribe(this::exportToFile);
    }

    private Observable<String> surveyToJson() {
        return Observable.just(survey)
                .map(MainController::toJson)
                .map(survey -> "\"survey\":" + survey);
    }

    private Observable<String> votesToJsonArray() {
        return surveyDatabaseHelper.selectAllVotesForSurvey(survey.getId())
                .map(MainController::toJson)
                .toList()
                .map(votes -> "[" + Joiner.on(", ")
                        .join(votes) + "]")
                .map(votesArray -> "\"votes\":" + votesArray);
    }

    private static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void exportToFile(String json) {
        try {
            JsonExportFile.createExportFile(new File("export.json"))
                    .append(json)
                    .close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
