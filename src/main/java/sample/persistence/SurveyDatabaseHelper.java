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

    public SurveyDatabaseHelper() throws SQLException {
        super(DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        SurveyTableHelper.createIn(database);
        VotesTableHelper.createIn(database);
    }

    private void dropTables(final SQLiteDatabase database) {
        SurveyTableHelper.dropFrom(database);
        VotesTableHelper.dropFrom(database);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase database, final int currentVersion, final int newVersion) {
        dropTables(database);
        onCreate(database);
    }

    public final Observable<Survey> queryAllSurveys() {
        String statement = "SELECT * FROM " + SurveyTableHelper.TABLE_NAME;
        return getDatabaseAccess().executeQueryAsync(statement, SurveyDatabaseHelper::mapToSurvey);
    }

    private static Survey mapToSurvey(final ResultSet resultSet) {
        try {
            final int id = resultSet.getInt(SurveyTableHelper.COLUMN_ID);
            final String question = resultSet.getString(SurveyTableHelper.COLUMN_QUESTION);
            final Survey.AnswerType answerType = Survey.AnswerType.valueOf(resultSet.getString(SurveyTableHelper.COLUMN_ANSWER_TYPE));
            return new Survey(id, question, answerType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void insert(final Survey survey, final SQLiteDatabase.ResultCallback<Boolean> resultCallback) {
        final String insertStatement = SurveyTableHelper.createInsertStatement(survey);
        getDatabaseAccess().executeInsertAsync(insertStatement, resultCallback);
    }

    public final void insert(final Survey survey, final SQLiteDatabase.ResultCallback<Boolean> resultCallback,
                             final SQLiteDatabase.ErrorCallback errorCallback) {
        final String insertStatement = SurveyTableHelper.createInsertStatement(survey);
        getDatabaseAccess().executeInsertAsync(insertStatement, resultCallback, errorCallback);
    }

}
