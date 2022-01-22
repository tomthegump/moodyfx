package com.codecrafters.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Tests for the {@link SQLiteDatabaseHelper}.
 *
 * @author ISchwarz
 */
public class SQLiteDatabaseHelperTest {

    private static final String EXISTING_DB_NAME = "foo";
    private static final String NOT_EXISTING_DB_NAME = "bar";

    private SQLiteDatabase existingDatabase;
    private StubSQLiteDbHelper cut;

    @Before
    public void setUp() throws Exception {
        existingDatabase = SQLiteDatabase.connectTo(EXISTING_DB_NAME);
    }

    @After
    public void tearDown() throws Exception {
        cut.getDatabaseAccess().close();
        new File(NOT_EXISTING_DB_NAME + ".db").delete();
        existingDatabase.close();
        new File(EXISTING_DB_NAME + ".db").delete();
    }

    @Test
    public void shouldCallOnCreateIfDbIsNotExisting() throws Exception {
        // when
        cut = new StubSQLiteDbHelper(NOT_EXISTING_DB_NAME, 0);

        // then
        assertTrue(cut.wasOnCreateCalled);
        assertFalse(cut.wasOnUpdateCalled);
        assertFalse(cut.wasOnDowngradeCalled);
        assertTrue(cut.wasOnOpenCalled);
    }

    @Test
    public void shouldNotCallOnCreateIfDbIsExisting() throws Exception {
        // when
        cut = new StubSQLiteDbHelper(EXISTING_DB_NAME, 0);

        // then
        assertFalse(cut.wasOnCreateCalled);
        assertFalse(cut.wasOnUpdateCalled);
        assertFalse(cut.wasOnDowngradeCalled);
        assertTrue(cut.wasOnOpenCalled);
    }

    @Test
    public void shouldCallOnUpdateIfNewerVersionIsGiven() throws Exception {
        // when
        cut = new StubSQLiteDbHelper(EXISTING_DB_NAME, 2);

        // then
        assertFalse(cut.wasOnCreateCalled);
        assertTrue(cut.wasOnUpdateCalled);
        assertEquals(2, cut.numberOfCallsToOnUpgrade);
        assertFalse(cut.wasOnDowngradeCalled);
        assertTrue(cut.wasOnOpenCalled);
    }

    @Test
    public void shouldCallOnDowngradeIfOlderVersionIsGiven() throws Exception {
        // when
        cut = new StubSQLiteDbHelper(EXISTING_DB_NAME, -2);

        // then
        assertFalse(cut.wasOnCreateCalled);
        assertFalse(cut.wasOnUpdateCalled);
        assertTrue(cut.wasOnDowngradeCalled);
        assertEquals(2, cut.numberOfCallsToOnDowngrade);
        assertTrue(cut.wasOnOpenCalled);
    }


    private class StubSQLiteDbHelper extends SQLiteDatabaseHelper {

        private boolean wasOnCreateCalled;
        private boolean wasOnUpdateCalled;
        private boolean wasOnDowngradeCalled;
        private boolean wasOnOpenCalled;

        private int numberOfCallsToOnUpgrade;
        private int numberOfCallsToOnDowngrade;

        public StubSQLiteDbHelper(String dbName, int dbVersion) throws SQLException {
            super(dbName, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            wasOnCreateCalled = true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int currentVersion, int newVersion) {
            wasOnUpdateCalled = true;
            numberOfCallsToOnUpgrade++;
        }

        @Override
        public void onDowngrade(SQLiteDatabase database, int currentVersion, int newVersion) {
            wasOnDowngradeCalled = true;
            numberOfCallsToOnDowngrade++;
        }

        @Override
        public void onOpen(SQLiteDatabase database) {
            wasOnOpenCalled = true;
        }

    }

}