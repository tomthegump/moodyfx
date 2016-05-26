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

public class Controller implements Initializable {

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
        setAnswerPossibilities(survey.getAnswerType());
    }

    private void setQuestion(String question) {
        moodQuestionLabel.setText(question);
    }

    private void setAnswerPossibilities(Survey.AnswerType answerType) {
        moodSelectionArea.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            String resourceName = answerType.getResourceName(i);
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
