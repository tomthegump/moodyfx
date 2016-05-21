package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sample.data.Survey;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label moodQuestionLabel;

    @FXML
    private HBox moodSelectionArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moodQuestionLabel.setText("");
        moodSelectionArea.getChildren().clear();
    }

    public void showSurvey(Survey survey) {
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
            moodSelectionArea.getChildren().add(new ImageView(getClass().getResource(resourceName).toString()));
        }
    }

}
