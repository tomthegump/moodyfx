package sample.persistence;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A helper class for the {@link SQLiteDatabase} that manages creation, upgrade and downgrade procedures.
 *
 * @author ISchwarz
 */
public abstract class SQLiteDatabaseHelper {

    private final SQLiteDatabase database;

    public SQLiteDatabaseHelper(final String dbName, final int dbVersion) throws SQLException {
        final boolean existsAlready = new File(dbName + ".db").exists();

        database = SQLiteDatabase.connectTo(dbName);

        if (!existsAlready) {
            onCreate(database);
        }

        final ResultSet resultSet = database.select("PRAGMA user_version");
        final int currentDbVersion = resultSet.getInt("user_version");
        if (currentDbVersion < dbVersion) {
            upgradeDatabase(currentDbVersion, dbVersion);
        } else if (currentDbVersion > dbVersion) {
            downgradeDatabase(currentDbVersion, dbVersion);
        }

        onOpen(database);
    }

    private void upgradeDatabase(int currentDbVersion, int goalDbVersion) throws SQLException {
        while (currentDbVersion < goalDbVersion) {
            onUpgrade(database, currentDbVersion, ++currentDbVersion);
            database.executeSqlStatement("PRAGMA user_version = " + currentDbVersion);
        }
    }

    private void downgradeDatabase(int currentDbVersion, int goalDbVersion) throws SQLException {
        while (currentDbVersion > goalDbVersion) {
            onDowngrade(database, currentDbVersion, --currentDbVersion);
            database.executeSqlStatement("PRAGMA user_version = " + currentDbVersion);
        }
    }

    public final SQLiteDatabase getDatabaseAccess() {
        return database;
    }

    public abstract void onCreate(final SQLiteDatabase database);

    public abstract void onUpgrade(final SQLiteDatabase database, final int currentVersion, final int newVersion);

    public void onDowngrade(final SQLiteDatabase database, final int currentVersion, final int newVersion) {

    }

    public void onOpen(final SQLiteDatabase database) {

    }

}
