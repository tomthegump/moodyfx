package sample;

import rx.Observable;
import rx.subjects.ReplaySubject;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Access to a SQLite database. Provides sync and async methods for reading and manipulating the data in the db.
 *
 * @author ISchwarz
 */
public class SQLiteDatabase {

    public interface ResultCallback<T> {
        void onResult(T result);
    }

    public interface ErrorCallback {
        void onError(Throwable e);
    }

    private final Connection dbConnection;
    private Executor statementExecutor = Executors.newFixedThreadPool(10);

    public static SQLiteDatabase connectTo(String dbName) throws SQLException {
        return new SQLiteDatabase(DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db"));
    }

    private SQLiteDatabase(final Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void setStatementExecutor(final Executor newStatementExecutor) {
        statementExecutor = newStatementExecutor;
    }

    public boolean executeInsert(final String insertStatement) throws SQLException {
        final Statement statement = dbConnection.createStatement();
        final boolean result = statement.execute(insertStatement);
        statement.close();
        return result;
    }

    public void executeInsertAsync(final String insertStatement, final ResultCallback<Boolean> resultCallback) {
        executeInsertAsync(insertStatement, resultCallback, new DefaultErrorCallback());
    }

    public void executeInsertAsync(final String insertStatement, final ResultCallback<Boolean> resultCallback,
                                   final ErrorCallback errorCallback) {
        statementExecutor.execute(new InsertRunnable(insertStatement, resultCallback, errorCallback));
    }

    public ResultSet executeQuery(final String queryStatement) throws SQLException {
        final Statement statement = dbConnection.createStatement();
        return statement.executeQuery(queryStatement);
    }

    public Observable<ResultSet> executeQueryAsync(final String queryStatement) {
        final ReplaySubject<ResultSet> resultObservable = ReplaySubject.create();
        statementExecutor.execute(new QueryRunnable(queryStatement, resultObservable));
        return resultObservable;
    }

    public void close() throws SQLException {
        dbConnection.close();
    }

    private final class InsertRunnable implements Runnable {

        private final String insertStatement;
        private final ResultCallback<Boolean> resultCallback;
        private final ErrorCallback errorCallback;

        InsertRunnable(final String insertStatement, final ResultCallback<Boolean> resultCallback, final ErrorCallback errorCallback) {
            this.insertStatement = insertStatement;
            this.resultCallback = resultCallback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void run() {
            try {
                boolean result = executeInsert(insertStatement);
                resultCallback.onResult(result);
            } catch (Throwable e) {
                errorCallback.onError(e);
            }
        }
    }

    private final class QueryRunnable implements Runnable {

        private final ReplaySubject<ResultSet> resultObservable;
        private final String queryStatement;

        QueryRunnable(final String queryStatement, final ReplaySubject<ResultSet> resultObservable) {
            this.queryStatement = queryStatement;
            this.resultObservable = resultObservable;
        }

        @Override
        public void run() {
            try {
                final ResultSet resultSet = executeQuery(queryStatement);
                while (resultSet.next()) {
                    resultObservable.onNext(resultSet);
                }
                resultSet.close();
                resultObservable.onCompleted();
            } catch (Throwable e) {
                resultObservable.onError(e);
            }
        }
    }

    private final class DefaultErrorCallback implements ErrorCallback {

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    }
}