package sample;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import sample.data.Config;
import sample.data.Survey;
import sample.data.Vote;
import sample.export.VoteExportFormat;
import sample.export.VoteExporter;
import sample.export.VoteExporterFactory;
import sample.persistence.SurveyDatabaseHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class MainController implements Initializable {

    private static final int VIEW_TRANSITION_DURATION_IN_SECONDS = 3;
    private static final int MILLIS_PER_SECOND = 1000;

    @FXML
    private AnchorPane contentPane;

    private SurveyDatabaseHelper surveyDatabaseHelper;

    private SurveyController surveyController;
    private Parent surveyView;
    private Parent thanksView;

    private Survey survey;
    private Config config;

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

    public void setConfig(Config config) {
        this.config = config;
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

    public void showSurvey(Survey survey) throws MalformedURLException {
        this.survey = survey;
        surveyController.showSurvey(survey, config.getIconSet());
    }

    private void storeVotesAndShowThanksView(int surveyId, int votedPoints) {
        showThanksView(config.getThanksDisplayDuration());
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
        surveyDatabaseHelper.insert(new Vote(surveyId, votedPoints, config.getSite()),
                result -> System.out.println("Voted Successfully"), System.err::println);
    }

    public void exportSurveyWithVotes(VoteExportFormat exportFormat) {
        VoteExporter voteExporter;
        try {
            voteExporter = VoteExporterFactory.createExporterFor(exportFormat, "export");
            surveyDatabaseHelper.selectAllVotesForSurvey(survey.getId()).subscribe(
                    vote -> wrapConsumer(voteExporter::append).accept(vote),
                    Throwable::printStackTrace,
                    () -> wrapRunnable(voteExporter::close).run()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }

    static <T> Consumer<T> wrapConsumer(ThrowingConsumer<T> throwingConsumer) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public interface ThrowingRunnable {
        void accept() throws Exception;
    }

    static Runnable wrapRunnable(ThrowingRunnable throwingConsumer) {

        return () -> {
            try {
                throwingConsumer.accept();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
