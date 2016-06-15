package sample.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.data.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.jooq.impl.DSL.*;
import static org.jooq.util.sqlite.SQLiteDataType.*;


/**
 * A helper to acces the Votes table.
 *
 * @author ISchwarz
 */
final class VotesTableHelper {

    private static final Logger LOGGER = LogManager.getLogger(VotesTableHelper.class);

    public static final String VOTES = "votes";
    public static final String TIMESTAMP = "timestamp";
    public static final String SURVEY_ID = "surveyId";
    public static final String SITE = "site";
    public static final String VOTED_POINTS = "votedPoints";

    static void createIn(SQLiteDatabase database) {
        String sql = createTableIfNotExists(table(VOTES))
                .column(field(TIMESTAMP, DATETIME))
                .column(field(SURVEY_ID, INT))
                .column(field(SITE, TEXT))
                .column(field(VOTED_POINTS, INT))
                .constraints(
                        constraint("PK_VOTES").primaryKey(field(TIMESTAMP), field(SURVEY_ID), field(SITE))
                )
                .getSQL();
        try {
            database.executeSqlStatement(sql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static void dropFrom(SQLiteDatabase database) {
        String sql = dropTableIfExists(table(VOTES)).getSQL();
        try {
            database.executeSqlStatement(sql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    public static Vote mapResultSetToVote(ResultSet resultSet) {
        try {
            final int surveyId = resultSet.getInt(SURVEY_ID);
            final int votedPoints = resultSet.getInt(VOTED_POINTS);
            final String site = resultSet.getString(SITE);
            final Date timestamp = new Date(resultSet.getLong(TIMESTAMP));
            return new Vote(surveyId, votedPoints, site, timestamp);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    static String createSelectAllStatement() {
        return select().from(table(VOTES))
                .getSQL();
    }

    public static String createSelectBySurveyStatement(int surveyId) {
        return select().from(table(VOTES))
                .where(field(SURVEY_ID).eq(surveyId))
                .getSQL();
    }

    static ContentValues createContentValues(Vote vote) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TIMESTAMP, vote.getTimestamp()
                .getTime());
        contentValues.put(SURVEY_ID, vote.getSurveyId());
        contentValues.put(SITE, vote.getSite());
        contentValues.put(VOTED_POINTS, vote.getVotedPoints());
        return contentValues;
    }
}
