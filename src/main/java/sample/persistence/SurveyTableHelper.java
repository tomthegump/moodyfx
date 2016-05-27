package sample.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.data.Survey;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * A helper to access the Survey Table.
 *
 * @author ISchwarz
 */
final class SurveyTableHelper {

    private static final Logger LOGGER = LogManager.getLogger(SurveyTableHelper.class);

    public static final String TABLE_NAME = "surveys";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER_TYPE = "answerType";

    private static final String SQL_CREATE_SURVEYS_TABLE = "CREATE TABLE " + TABLE_NAME + " " +
            "(" + COLUMN_ID + "            INT         PRIMARY KEY     NOT NULL," +
            " " + COLUMN_QUESTION + "      TEXT                        NOT NULL," +
            " " + COLUMN_ANSWER_TYPE + "   VARCHAR(10)                 NOT NULL )";

    private static final String SQL_DROP_SURVEYS_TABLE = "DROP TABLE " + TABLE_NAME;

    static void createIn(SQLiteDatabase database) {
        try {
            database.executeSqlStatement(SQL_CREATE_SURVEYS_TABLE);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static void dropFrom(SQLiteDatabase database) {
        try {
            database.executeSqlStatement(SQL_DROP_SURVEYS_TABLE);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static String createSelectAllStatement() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    static Survey mapResultSetToSurvey(final ResultSet resultSet) {
        try {
            final int id = resultSet.getInt(COLUMN_ID);
            final String question = resultSet.getString(COLUMN_QUESTION);
            final Survey.AnswerType answerType = Survey.AnswerType.valueOf(resultSet.getString(COLUMN_ANSWER_TYPE));
            return new Survey(id, question, answerType);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    static ContentValues createContentValues(final Survey survey) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, survey.getId());
        contentValues.put(COLUMN_QUESTION, survey.getQuestion());
        contentValues.put(COLUMN_ANSWER_TYPE, survey.getAnswerType().toString());
        return contentValues;
    }

}
