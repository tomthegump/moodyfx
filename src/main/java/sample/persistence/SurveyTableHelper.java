package sample.persistence;

import sample.Survey;

import java.sql.SQLException;

/**
 * Created by Ingo on 20.05.2016.
 */
final class SurveyTableHelper {

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
            database.executeInsert(SQL_CREATE_SURVEYS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void dropFrom(SQLiteDatabase database) {
        try {
            database.executeInsert(SQL_DROP_SURVEYS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String createInsertStatement(final Survey survey) {
        return "INSERT INTO " + SurveyTableHelper.TABLE_NAME +
                " (" + SurveyTableHelper.COLUMN_ID +
                ", " + SurveyTableHelper.COLUMN_QUESTION +
                ", " + SurveyTableHelper.COLUMN_ANSWER_TYPE + ") " +
                "VALUES (" + survey.getId() +
                ", '" + survey.getQuestion() +
                "', '" + survey.getAnswerType() + "')";
    }

}
