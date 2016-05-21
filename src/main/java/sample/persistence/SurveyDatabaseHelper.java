package sample.persistence;

import rx.Observable;
import sample.data.Survey;
import sample.data.Vote;

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
        String queryStatement = SurveyTableHelper.createSelectAllStatement();
        return getDatabaseAccess().executeQueryAsync(queryStatement, SurveyTableHelper::mapResultSetToSurvey);
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

    public final Observable<Vote> queryAllVotes() {
        String queryStatement = VotesTableHelper.createSelectAllStatement();
        return getDatabaseAccess().executeQueryAsync(queryStatement, VotesTableHelper::mapResultSetToVote);
    }

    public final void insert(final Vote vote, final SQLiteDatabase.ResultCallback<Boolean> resultCallback) {
        final String insertStatement = VotesTableHelper.createInsertStatement(vote);
        getDatabaseAccess().executeInsertAsync(insertStatement, resultCallback);
    }

    public final void insert(final Vote vote, final SQLiteDatabase.ResultCallback<Boolean> resultCallback,
                             final SQLiteDatabase.ErrorCallback errorCallback) {
        final String insertStatement = VotesTableHelper.createInsertStatement(vote);
        getDatabaseAccess().executeInsertAsync(insertStatement, resultCallback, errorCallback);
    }

}
