package sample.persistence;

import sample.data.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Ingo on 20.05.2016.
 */
final class VotesTableHelper {

    public static final String TABLE_NAME = "votes";

    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SURVEY_ID = "surveyId";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_VOTED_POINTS = "votedPoints";

    public static final String[] ALL_COLUMNS = {
            COLUMN_TIMESTAMP,
            COLUMN_SURVEY_ID,
            COLUMN_SITE,
            COLUMN_VOTED_POINTS};

    private static final String SQL_CREATE_VOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " " +
            "(" + COLUMN_TIMESTAMP + "      DATETIME," +
            " " + COLUMN_SURVEY_ID + "      INT," +
            " " + COLUMN_SITE + "           TEXT," +
            " " + COLUMN_VOTED_POINTS + "   INT," +
            " PRIMARY KEY (" + COLUMN_TIMESTAMP + ", " + COLUMN_SURVEY_ID + ", " + COLUMN_SITE + ")" +
            " )";

    private static final String SQL_DROP_VOTES_TABLE = "DROP TABLE " + TABLE_NAME;

    static void createIn(SQLiteDatabase database) {
        try {
            database.executeInsert(SQL_CREATE_VOTES_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void dropFrom(SQLiteDatabase database) {
        try {
            database.executeInsert(SQL_DROP_VOTES_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String createInsertStatement(final Vote vote) {
        return "INSERT INTO " + TABLE_NAME +
                " (" + COLUMN_TIMESTAMP +
                ", " + COLUMN_SURVEY_ID +
                ", " + COLUMN_SITE +
                ", " + COLUMN_VOTED_POINTS + ") " +
                "VALUES (" + vote.getTimestamp().getTime() +
                ", " + vote.getSurveyId() +
                ", '" + vote.getSite() + "'" +
                ", " + vote.getVotedPoints() + ")";
    }

    public static Vote mapResultSetToVote(ResultSet resultSet) {
        try {
            final int surveyId = resultSet.getInt(COLUMN_SURVEY_ID);
            final int votedPoints = resultSet.getInt(COLUMN_VOTED_POINTS);
            final String site = resultSet.getString(COLUMN_SITE);
            final Date timestamp = new Date(resultSet.getLong(COLUMN_TIMESTAMP));
            return new Vote(surveyId, votedPoints, site, timestamp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createSelectAllStatement() {
        return "SELECT * FROM " + TABLE_NAME;
    }
}
