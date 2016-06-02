package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sample.data.Survey;

import java.net.URL;
import java.util.ResourceBundle;

public class SurveyController implements Initializable {

    @FXML
    private Label moodQuestionLabel;
    @FXML
    private HBox moodSelectionArea;

    private VoteStore voteStore;
    private Survey survey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moodQuestionLabel.setText("");
        moodSelectionArea.getChildren().clear();
    }

    public void showSurvey(Survey survey) {
        this.survey = survey;
        setQuestion(survey.getQuestion());
        setAnswerPossibilities(survey.getAnswerType(), survey.getIconType());
    }

    private void setQuestion(String question) {
        moodQuestionLabel.setText(question);
    }

    private void setAnswerPossibilities(Survey.AnswerType answerType, Survey.IconType iconType) {
        switch (answerType) {
            case YES_NO:
                setAnswerPossibilitiesYesNo(iconType);
                break;
            case YES_NO_MEEH:
                setAnswerPossibilitiesYesNoMeeh(iconType);
                break;
            case RATING:
                setAnswerPossibilitiesRating(iconType);
                break;
        }
    }

    private void setAnswerPossibilitiesRating(Survey.IconType iconType) {
        moodSelectionArea.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            String resourceName = iconType.getResourceName(i);
            ImageView imageView = new ImageView(getClass().getResource(resourceName).toString());
            moodSelectionArea.getChildren().add(imageView);
            imageView.setOnMouseClicked(new VotingListener(i));
        }
    }

    private void setAnswerPossibilitiesYesNo(Survey.IconType iconType) {
        moodSelectionArea.getChildren().clear();

        String resourceName1 = iconType.getResourceName(1);
        ImageView imageView1 = new ImageView(getClass().getResource(resourceName1).toString());
        moodSelectionArea.getChildren().add(imageView1);
        imageView1.setOnMouseClicked(new VotingListener(1));

        String resourceName3 = iconType.getResourceName(3);
        ImageView imageView3 = new ImageView(getClass().getResource(resourceName3).toString());
        moodSelectionArea.getChildren().add(imageView3);
        imageView3.setOnMouseClicked(new VotingListener(3));
    }

    private void setAnswerPossibilitiesYesNoMeeh(Survey.IconType iconType) {
        moodSelectionArea.getChildren().clear();

        for (int i = 1; i < 4; i++) {
            String resourceName = iconType.getResourceName(i);
            ImageView imageView = new ImageView(getClass().getResource(resourceName).toString());
            moodSelectionArea.getChildren().add(imageView);
            imageView.setOnMouseClicked(new VotingListener(i));
        }
    }

    public void setVoteStore(VoteStore voteStore) {
        this.voteStore = voteStore;
    }

    private class VotingListener implements EventHandler<MouseEvent> {

        private int votedPoints;

        public VotingListener(int votedPoints) {
            this.votedPoints = votedPoints;
        }

        @Override
        public void handle(MouseEvent event) {
            if (voteStore != null) {
                voteStore.saveVote(survey.getId(), votedPoints);
            }
        }
    }

    public interface VoteStore {
        void saveVote(int surveyId, int votedPoints);
    }

}
