package com.codecrafters.persistence;

import com.codecrafters.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Field;
import org.jooq.conf.ParamType;
import org.sqlite.SQLiteConfig;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.jooq.impl.DSL.*;

/**
 * Access to a SQLite database. Provides sync and async methods for reading and manipulating the data in the db.
 *
 * @author ISchwarz
 */
public class SQLiteDatabase {

    private static final Logger LOGGER = LogManager.getLogger(SQLiteDatabase.class);
    private static final String JDBC_SQLITE_PREFIX = "jdbc:sqlite:";
    private static final String DB_FILE_ENDING = ".db";
    private final Connection dbConnection;
    private final File dbFile;
    private Executor statementExecutor = Executors.newFixedThreadPool(10);

    private SQLiteDatabase(final Connection dbConnection, final File dbFile) {
        this.dbConnection = dbConnection;
        this.dbFile = dbFile;
    }

    public static SQLiteDatabase connectTo(final String dbName) throws SQLException {
        return connectTo(dbName, false);
    }

    public static SQLiteDatabase connectTo(final String dbName, final File directory) throws SQLException {
        return connectTo(dbName, directory, false);
    }

    public static SQLiteDatabase connectTo(final String dbName, boolean readOnly) throws SQLException {
        return connectTo(dbName, Files.PERSISTENCE_DIR, readOnly);
    }

    public static SQLiteDatabase connectTo(final String dbName, final File directory, final boolean readOnly) throws SQLException {
        final SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(readOnly);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!directory.isDirectory()) {
            throw new SQLException("The given file must be a directory! [" + directory.getAbsolutePath() + "]");
        }

        final File dbFile = new File(directory, dbName + DB_FILE_ENDING);
        final String connectionUri = JDBC_SQLITE_PREFIX + dbFile.getAbsolutePath();
        final Connection dbConnection = DriverManager.getConnection(connectionUri, config.toProperties());
        return new SQLiteDatabase(dbConnection, dbFile);
    }

    public void setStatementExecutor(final Executor newStatementExecutor) {
        statementExecutor = newStatementExecutor;
    }

    public boolean executeSqlStatement(final String insertStatement) throws SQLException {
        final Statement statement = dbConnection.createStatement();
        final boolean result = statement.execute(insertStatement);
        statement.close();
        return result;
    }

    public ResultSet select(final String selectStatement) throws SQLException {
        final Statement statement = dbConnection.createStatement();
        return statement.executeQuery(selectStatement);
    }

    public <T> Observable<T> select(final String queryStatement, final Func1<ResultSet, T> mapper) {
        final ReplaySubject<T> resultObservable = ReplaySubject.create();
        statementExecutor.execute(new QueryRunnable<>(queryStatement, resultObservable, mapper));
        return resultObservable;
    }

    public int insert(final String tableName, final ContentValues values) throws SQLException {
        final Statement statement = dbConnection.createStatement();
        int updatedRow = statement.executeUpdate(insertInto(table(tableName)).set(toFieldMap(values))
                .getSQL(ParamType.INLINED));
        statement.close();
        return updatedRow;
    }

    private Map<? extends Field<?>, ?> toFieldMap(ContentValues values) {
        Map<Field<Object>, Object> map = new HashMap<>();
        for (String key : values.keySet()) {
            map.put(field(key), values.get(key));
        }
        return map;
    }

    public void insert(final String tableName, final ContentValues values, final ResultCallback<Integer> resultCallback) {
        insert(tableName, values, resultCallback, new DefaultErrorCallback());
    }

    public void insert(final String tableName, final ContentValues values, final ResultCallback<Integer> resultCallback,
                       final ErrorCallback errorCallback) {
        statementExecutor.execute(new InsertRunnable(tableName, values, resultCallback, errorCallback));
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

    public interface ResultCallback<T> {
        void onResult(T result);
    }

    public interface ErrorCallback {
        void onError(Throwable e);
    }

    private final class InsertRunnable implements Runnable {

        private final ResultCallback<Integer> resultCallback;
        private final ErrorCallback errorCallback;
        private final ContentValues values;
        private final String tableName;

        InsertRunnable(final String tableName, final ContentValues values, final ResultCallback<Integer> resultCallback, final ErrorCallback errorCallback) {
            this.resultCallback = resultCallback;
            this.errorCallback = errorCallback;
            this.tableName = tableName;
            this.values = values;
        }

        @Override
        public void run() {
            try {
                int result = insert(tableName, values);
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
                final ResultSet resultSet = select(queryStatement);
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
            LOGGER.error(e.getMessage(), e);
        }
    }
}
