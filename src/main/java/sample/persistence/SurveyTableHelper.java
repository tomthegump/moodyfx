package sample.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.data.Survey;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.jooq.impl.DSL.*;
import static org.jooq.util.sqlite.SQLiteDataType.*;


/**
 * A helper to access the Survey Table.
 *
 * @author ISchwarz
 */
final class SurveyTableHelper {

    private static final Logger LOGGER = LogManager.getLogger(SurveyTableHelper.class);

    public static final String SURVEYS = "surveys";
    public static final String ID = "id";
    public static final String QUESTION = "question";
    public static final String ANSWER_TYPE = "answerType";

    static void createIn(SQLiteDatabase database) {
        String sql = createTableIfNotExists(table(SURVEYS))
                .column(field(ID, INT))
                .column(field(QUESTION, TEXT))
                .column(field(ANSWER_TYPE, VARCHAR.length(10)))
                .constraints(
                        constraint("PK_SURVEYS").primaryKey(field(ID))
                )
                .getSQL();
        try {
            database.executeSqlStatement(sql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static void dropFrom(SQLiteDatabase database) {
        String sql = dropTableIfExists(table(SURVEYS)).getSQL();
        try {
            database.executeSqlStatement(sql);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static String createSelectAllStatement() {
        return select().from(table(SURVEYS))
                .getSQL();
    }

    static Survey mapResultSetToSurvey(final ResultSet resultSet) {
        try {
            final int id = resultSet.getInt(ID);
            final String question = resultSet.getString(QUESTION);
            final Survey.AnswerType answerType = Survey.AnswerType.valueOf(resultSet.getString(ANSWER_TYPE));
            return new Survey(id, question, answerType, null);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    static ContentValues createContentValues(final Survey survey) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ID, survey.getId());
        contentValues.put(QUESTION, survey.getQuestion());
        contentValues.put(ANSWER_TYPE, survey.getIconType()
                .toString());
        return contentValues;
    }

}
