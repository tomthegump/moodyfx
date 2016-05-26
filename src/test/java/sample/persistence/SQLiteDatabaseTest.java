package sample.persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;

/**
 * Tests for the {@link SQLiteDatabase}.
 * @author ISchwarz
 */
public class SQLiteDatabaseTest {

    private static final String DB_NAME = "test";
    private SQLiteDatabase cut;

    @Before
    public void setUp() throws Exception {
        cut = SQLiteDatabase.connectTo(DB_NAME);
        cut.setStatementExecutor(new MainThreadExecutor());
        cut.executeInsert("CREATE TABLE testTable " +
                "( " +
                "ID    INT   PRIMARY KEY     NOT NULL, " +
                "NAME  TEXT                  NOT NULL  " +
                ")");
    }

    @After
    public void tearDown() throws Exception {
        cut.close();
        cut.delete();
    }

    @Test
    public void shouldInsertGivenData() throws Exception {
        // given
        final String insertStatement = "INSERT INTO testTable (ID, NAME) VALUES (1, 'Heisenberg')";

        // when
        final boolean result = cut.executeInsert(insertStatement);

        // then
        assertFalse(result);
    }

    @Test
    public void shouldInsertGivenDataAsync() throws Exception {
        // given
        final String insertStatement = "INSERT INTO testTable (ID, NAME) VALUES (1, 'Heisenberg')";

        // when
        cut.executeInsertAsync(insertStatement, Assert::assertFalse, SQLiteDatabaseTest::printErrorAndFail);
    }

    @Test
    public void shouldGiveQueryResults() throws Exception {
        // given
        final String insertStatement1 = "INSERT INTO testTable (ID, NAME) VALUES (1, 'Heisenberg')";
        cut.executeInsert(insertStatement1);
        final String insertStatement2 = "INSERT INTO testTable (ID, NAME) VALUES (2, 'Saul')";
        cut.executeInsert(insertStatement2);
        final String queryStatement = "SELECT * FROM testTable";

        // when
        final ResultSet resultSet = cut.executeQuery(queryStatement);

        Counter resultCount = new Counter();
        while (resultSet.next()) {
            resultCount.increment();
        }
        resultSet.close();

        // then
        assertEquals(2, resultCount.getValue());
    }

    @Test
    public void shouldGiveQueryResultsAsync() throws Exception {
        // given
        final String insertStatement1 = "INSERT INTO testTable (ID, NAME) VALUES (1, 'Heisenberg');";
        cut.executeInsert(insertStatement1);
        final String insertStatement2 = "INSERT INTO testTable (ID, NAME) VALUES (2, 'Saul');";
        cut.executeInsert(insertStatement2);
        final String queryStatement = "SELECT * FROM testTable;";

        // when
        final Counter resultCount = new Counter();
        cut.executeQueryAsync(queryStatement, r -> "")
                .subscribe(r -> resultCount.increment(), SQLiteDatabaseTest::printErrorAndFail);

        // then
        assertEquals(2, resultCount.getValue());
    }


    private static void printErrorAndFail(Throwable error) {
        error.printStackTrace();
        fail();
    }

    private static class Counter {

        private int value;

        public Counter() {
            this(0);
        }

        public Counter(int initialValue) {
            value = initialValue;
        }

        public void increment() {
            value++;
        }

        public int getValue() {
            return value;
        }
    }

    private static class MainThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

}