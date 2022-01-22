package com.codecrafters.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ingo on 25.05.2016.
 */
public class SQLStatement {

    public static SelectStatementFromBuilder select(final String whatToSelect) {
        return new SelectStatementFromBuilder(whatToSelect);
    }

    public static void insertInto(final String whereToInsert) {

    }

    public static final class SelectStatementFromBuilder {

        private final String whatToSelect;

        private SelectStatementFromBuilder(final String whatToSelect) {
            this.whatToSelect = whatToSelect;
        }

        public final SelectStatementWhereBuilder from(final String whereToSelect) {
            return new SelectStatementWhereBuilder(whatToSelect, whereToSelect);
        }
    }

    public static final class SelectStatementWhereBuilder {

        private final String whatToSelect;
        private final String whereToSelect;

        private SelectStatementWhereBuilder(final String whatToSelect, final String whereToSelect) {
            this.whatToSelect = whatToSelect;
            this.whereToSelect = whereToSelect;
        }

        public final SelectStatementWhereConcatenationBuilder where(final String query) {
            return new SelectStatementWhereConcatenationBuilder(whatToSelect, whereToSelect, query);
        }

        @Override
        public final String toString() {
            return "SELECT " + whatToSelect + " FROM " + whereToSelect;
        }
    }

    public static final class SelectStatementWhereConcatenationBuilder {

        private final List<String> queries = new ArrayList<>();
        private final String whatToSelect;
        private final String whereToSelect;
        private final String firstQuery;

        private SelectStatementWhereConcatenationBuilder(final String whatToSelect, final String whereToSelect, final String firstQuery) {
            this.whatToSelect = whatToSelect;
            this.whereToSelect = whereToSelect;
            this.firstQuery = firstQuery;
        }

        public final SelectStatementWhereConcatenationBuilder and(final String query) {
            queries.add(" AND " + query);
            return this;
        }

        public final SelectStatementWhereConcatenationBuilder or(final String query) {
            queries.add(" OR " + query);
            return this;
        }

        @Override
        public final String toString() {
            final StringBuilder selectStatement = new StringBuilder();
            selectStatement.append("SELECT ").append(whatToSelect);
            selectStatement.append(" FROM ").append(whereToSelect);
            selectStatement.append(" WHERE ").append(firstQuery);
            queries.forEach(selectStatement::append);
            selectStatement.append(";");
            return selectStatement.toString();
        }
    }

}
