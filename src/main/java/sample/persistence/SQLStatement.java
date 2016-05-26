package sample.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ingo on 25.05.2016.
 */
public class SQLStatement {

    public static SelectStatementFromBuilder select(String whatToSelect) {
        return new SelectStatementFromBuilder(whatToSelect);
    }

    public static class SelectStatementFromBuilder {

        private String whatToSelect;

        private SelectStatementFromBuilder(final String whatToSelect) {
            this.whatToSelect = whatToSelect;
        }

        public SelectStatementWhereBuilder from(final String whereToSelect) {
            return new SelectStatementWhereBuilder(whatToSelect, whereToSelect);
        }

    }

    public static class SelectStatementWhereBuilder {

        private final String whatToSelect;
        private final String whereToSelect;

        private SelectStatementWhereBuilder(String whatToSelect, String whereToSelect) {
            this.whatToSelect = whatToSelect;
            this.whereToSelect = whereToSelect;
        }

        public SelectStatementWhereConcatenationBuilder where(String query) {
            return new SelectStatementWhereConcatenationBuilder(whatToSelect, whereToSelect, query);
        }

        @Override
        public String toString() {
            return "SELECT " + whatToSelect + " FROM " + whereToSelect;
        }
    }

    public static class SelectStatementWhereConcatenationBuilder {

        private final List<String> queries = new ArrayList<>();
        private final String whatToSelect;
        private final String whereToSelect;
        private String firstQuery;

        private SelectStatementWhereConcatenationBuilder(String whatToSelect, String whereToSelect, String firstQuery) {
            this.whatToSelect = whatToSelect;
            this.whereToSelect = whereToSelect;
            this.firstQuery = firstQuery;
        }

        public SelectStatementWhereConcatenationBuilder and(String query) {
            queries.add(" AND " + query);
            return this;
        }

        public SelectStatementWhereConcatenationBuilder or(String query) {
            queries.add(" OR " + query);
            return this;
        }

        @Override
        public String toString() {
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
