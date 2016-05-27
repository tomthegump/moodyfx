package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @FXML
    private AnchorPane contentPane;

    private SurveyDatabaseHelper surveyDatabaseHelper;

    private SurveyController surveyController;
    private Parent surveyView;
    private Survey survey;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            surveyDatabaseHelper = new SurveyDatabaseHelper();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("survey.fxml"));
            surveyView = fxmlLoader.load();
            surveyController = fxmlLoader.getController();

            surveyController.setVoteStore((surveyId, votedPoints) ->
                    surveyDatabaseHelper.insert(new Vote(surveyId, votedPoints, "C2-Team"),
                    result -> System.out.println("Voted Successfully"), System.err::println));

            AnchorPane.setTopAnchor(surveyView, 0.0);
            AnchorPane.setRightAnchor(surveyView, 0.0);
            AnchorPane.setLeftAnchor(surveyView, 0.0);
            AnchorPane.setBottomAnchor(surveyView, 0.0);
            contentPane.getChildren().setAll(surveyView);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void showSurvey(Survey survey) {
        this.survey = survey;
        surveyController.showSurvey(survey);
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
