package com.codecrafters.persistence;

import rx.Observable;
import com.codecrafters.data.Survey;
import com.codecrafters.data.Vote;

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

    public final Observable<Survey> selectAllSurveys() {
        String queryStatement = SurveyTableHelper.createSelectAllStatement();
        return getDatabaseAccess().select(queryStatement, SurveyTableHelper::mapResultSetToSurvey);
    }

    public final void insert(final Survey survey, final SQLiteDatabase.ResultCallback<Integer> resultCallback) {
        final ContentValues valuesToInsert = SurveyTableHelper.createContentValues(survey);
        getDatabaseAccess().insert(SurveyTableHelper.SURVEYS, valuesToInsert, resultCallback);
    }

    public final void insert(final Survey survey, final SQLiteDatabase.ResultCallback<Integer> resultCallback,
                             final SQLiteDatabase.ErrorCallback errorCallback) {
        final ContentValues valuesToInsert = SurveyTableHelper.createContentValues(survey);
        getDatabaseAccess().insert(SurveyTableHelper.SURVEYS, valuesToInsert, resultCallback, errorCallback);
    }

    public final Observable<Vote> selectAllVotes() {
        String selectStatement = VotesTableHelper.createSelectAllStatement();
        return getDatabaseAccess().select(selectStatement, VotesTableHelper::mapResultSetToVote);
    }

    public final Observable<Vote> selectAllVotesForSurvey(int surveyId) {
        String selectStatement = VotesTableHelper.createSelectBySurveyStatement(surveyId);
        return getDatabaseAccess().select(selectStatement, VotesTableHelper::mapResultSetToVote);
    }

    public final void insert(final Vote vote, final SQLiteDatabase.ResultCallback<Integer> resultCallback) {
        final ContentValues valuesToInsert = VotesTableHelper.createContentValues(vote);
        getDatabaseAccess().insert(VotesTableHelper.VOTES, valuesToInsert, resultCallback);
    }

    public final void insert(final Vote vote, final SQLiteDatabase.ResultCallback<Integer> resultCallback,
                             final SQLiteDatabase.ErrorCallback errorCallback) {
        final ContentValues valuesToInsert = VotesTableHelper.createContentValues(vote);
        getDatabaseAccess().insert(VotesTableHelper.VOTES, valuesToInsert, resultCallback, errorCallback);
    }
}
