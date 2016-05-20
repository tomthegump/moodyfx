package sample.persistence;

import rx.Observable;
import sample.Survey;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A {@link SQLiteDatabaseHelper} implementation which manages the surves database.
 *
 * @author ISchwarz
 */
public class SurveyDatabaseHelper extends SQLiteDatabaseHelper {

    private static final String DB_NAME = "surveys";
    private static final int DB_VERSION = 0;

    private static final String SQL_CREATE_SURVEYS_TABLE = "CREATE TABLE surveys " +
            "(id            INT         PRIMARY KEY     NOT NULL," +
            " question      TEXT                        NOT NULL," +
            " answerType    VARCHAR(10)                 NOT NULL )";
    private static final String SQL_CREATE_VOTES_TABLE = "CREATE TABLE votes " +
            "(timestamp     DATETIME    PRIMARY KEY     DEFAULT CURRENT_TIMESTAMP," +
            " surveyId      INT         PRIMARY KEY )";

    private static final String SQL_DROP_SURVEYS_TABLE = "DROP TABLE surveys";
    private static final String SQL_DROP_VOTES_TABLE = "DROP TABLE votes";

    public SurveyDatabaseHelper() throws SQLException {
        super(DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.executeInsert(SQL_CREATE_SURVEYS_TABLE);
            database.executeInsert(SQL_CREATE_VOTES_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int currentVersion, int newVersion) {
        dropTables(database);
        onCreate(database);
    }

    public Observable<Survey> queryAllSurveys() {
        return getDatabaseAccess().executeQueryAsync("SELECT * FROM surveys", SurveyDatabaseHelper::mapToSurvey);
    }

    private static Survey mapToSurvey(ResultSet resultSet) {
        try {
            return new Survey(resultSet.getInt("id"), resultSet.getString("question"),
                    Survey.AnswerType.valueOf(resultSet.getString("answerType")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Survey(1, "Test");
    }

    public void insert(Survey survey, SQLiteDatabase.ResultCallback<Boolean> resultCallback) {
        final String statement = "INSERT INTO surveys (id, question, answerType) " +
                "VALUES (" + survey.getId() + ", '" + survey.getQuestion() + "', '" + survey.getAnswerType() + "')";
        getDatabaseAccess().executeInsertAsync(statement, resultCallback);
    }

    private void dropTables(SQLiteDatabase database) {
        try {
            database.executeInsert(SQL_DROP_SURVEYS_TABLE);
            database.executeInsert(SQL_DROP_VOTES_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
