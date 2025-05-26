module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires mysql.connector.j;
    requires com.google.gson;
    requires unirest.java;
    requires json;
    requires java.sql;
    requires javafx.graphics;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1.client.models;
    opens com.example.demo1.client.models to javafx.fxml;
    exports com.example.demo1.client.controllers;
    opens com.example.demo1.client.controllers to javafx.fxml;
    exports com.example.demo1.server.repositories;
    opens com.example.demo1.server.repositories to javafx.fxml;
    exports com.example.demo1.common.models;
    opens com.example.demo1.common.models to javafx.fxml;
    exports com.example.demo1.client.viewControllers;
    opens com.example.demo1.client.viewControllers to javafx.fxml;
}