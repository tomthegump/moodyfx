package com.codecrafters;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import com.codecrafters.data.IconSet;
import com.codecrafters.data.Survey;

import java.net.MalformedURLException;
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

    public void showSurvey(Survey survey, IconSet iconSet) throws MalformedURLException {
        this.survey = survey;
        setQuestion(survey.getQuestion());
        setAnswerPossibilities(survey.getAnswerType(), iconSet);
    }

    private void setQuestion(String question) {
        moodQuestionLabel.setText(question);
    }

    private void setAnswerPossibilities(Survey.AnswerType answerType, IconSet iconType) throws MalformedURLException {
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

    private void setAnswerPossibilitiesRating(IconSet iconType) throws MalformedURLException {
        moodSelectionArea.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(iconType.getImageUrl(i).toString());
            HBox.setHgrow(imageView, Priority.ALWAYS);
            moodSelectionArea.getChildren().add(imageView);
            imageView.setOnMouseClicked(new VotingListener(i));
        }
    }

    private void setAnswerPossibilitiesYesNo(IconSet iconType) throws MalformedURLException {
        moodSelectionArea.getChildren().clear();

        ImageView imageView1 = new ImageView(iconType.getImageUrl(1).toString());
        HBox.setHgrow(imageView1, Priority.ALWAYS);
        moodSelectionArea.getChildren().add(imageView1);
        imageView1.setOnMouseClicked(new VotingListener(1));

        ImageView imageView3 = new ImageView(iconType.getImageUrl(3).toString());
        HBox.setHgrow(imageView3, Priority.ALWAYS);
        moodSelectionArea.getChildren().add(imageView3);
        imageView3.setOnMouseClicked(new VotingListener(3));
    }

    private void setAnswerPossibilitiesYesNoMeeh(IconSet iconType) throws MalformedURLException {
        moodSelectionArea.getChildren().clear();

        for (int i = 1; i < 4; i++) {
            ImageView imageView = new ImageView(iconType.getImageUrl(i).toString());
            imageView.setPreserveRatio(true);
            imageView.maxWidth(Double.MAX_VALUE);
            imageView.maxHeight(Double.MAX_VALUE);
            imageView.setOnMouseClicked(new VotingListener(i));
            HBox.setHgrow(imageView, Priority.ALWAYS);
            moodSelectionArea.getChildren().add(imageView);

            if (i < 3) {
                Region spacer = new Region();
                moodSelectionArea.getChildren().add(spacer);
            }
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
