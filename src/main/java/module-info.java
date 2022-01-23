open module com.codecrafters {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;
    requires rxjava;
    requires jackson.databind;
    requires log4j.api;
    requires jooq;
    requires sqlite.jdbc;

    exports com.codecrafters;
}