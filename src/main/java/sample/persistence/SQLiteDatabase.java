package sample.persistence;

import org.sqlite.SQLiteConfig;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

import java.io.File;
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

    private static final String JDBC_SQLITE_PREFIX = "jdbc:sqlite:";
    private static final String DB_FILE_ENDING = ".db";

    private final Connection dbConnection;
    private final File dbFile;
    private Executor statementExecutor = Executors.newFixedThreadPool(10);

    public static SQLiteDatabase connectTo(final String dbName) throws SQLException {
        return connectTo(dbName, false);
    }

    public static SQLiteDatabase connectTo(final String dbName, final File directory ) throws SQLException {
        return connectTo(dbName, directory, false);
    }

    public static SQLiteDatabase connectTo(final String dbName, boolean readOnly) throws SQLException {
        return connectTo(dbName, new File("."), readOnly);
    }

    public static SQLiteDatabase connectTo(final String dbName, final File directory, final boolean readOnly) throws SQLException {
        final SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(readOnly);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        if(!directory.isDirectory()) {
            throw new SQLException("The given file must be a directory! ["+ directory.getAbsolutePath() + "]" );
        }

        final File dbFile = new File(directory, dbName + DB_FILE_ENDING);
        final String connectionUri = JDBC_SQLITE_PREFIX + dbFile.getAbsolutePath();
        final Connection dbConnection = DriverManager.getConnection(connectionUri, config.toProperties());
        return new SQLiteDatabase(dbConnection, dbFile);
    }

    private SQLiteDatabase(final Connection dbConnection, final File dbFile) {
        this.dbConnection = dbConnection;
        this.dbFile = dbFile;
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

    public <T> Observable<T> executeQueryAsync(final String queryStatement, Func1<ResultSet, T> mapper) {
        final ReplaySubject<T> resultObservable = ReplaySubject.create();
        statementExecutor.execute(new QueryRunnable<>(queryStatement, resultObservable, mapper));
        return resultObservable;
    }

    public void close() throws SQLException {
        dbConnection.close();
    }

    public boolean isReadOnly() throws SQLException {
        return dbConnection.isReadOnly();
    }

    public File getDbFile() {
        return dbFile;
    }

    public boolean delete() {
        return dbFile.delete();
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

    private final class QueryRunnable<T> implements Runnable {

        private final ReplaySubject<T> resultObservable;
        private final Func1<ResultSet, T> mapper;
        private final String queryStatement;

        QueryRunnable(final String queryStatement, final ReplaySubject<T> resultObservable, Func1<ResultSet, T> mapper) {
            this.queryStatement = queryStatement;
            this.resultObservable = resultObservable;
            this.mapper = mapper;
        }

        @Override
        public void run() {
            try {
                final ResultSet resultSet = executeQuery(queryStatement);
                while (resultSet.next()) {
                    T mappedData = mapper.call(resultSet);
                    resultObservable.onNext(mappedData);
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
