package sample.persistence;

import java.sql.SQLException;

/**
 * Created by Ingo on 20.05.2016.
 */
final class VotesTableHelper {

    public static final String TABLE_NAME = "votes";

    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SURVEY_ID = "surveyId";

    public static final String[] ALL_COLUMNS = {
            COLUMN_TIMESTAMP,
            COLUMN_SURVEY_ID};

    private static final String SQL_CREATE_VOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " " +
            "(" + COLUMN_TIMESTAMP + "      DATETIME    PRIMARY KEY     DEFAULT CURRENT_TIMESTAMP," +
            " " + COLUMN_SURVEY_ID + "      INT         PRIMARY KEY )";

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


}
