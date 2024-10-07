module org.example.sortingpuzzle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    opens SortingTable to javafx.fxml, com.fasterxml.jackson.databind;
    exports SortingTable;
    exports solver;
    opens solver to com.fasterxml.jackson.databind, javafx.fxml;
    requires org.apache.logging.log4j;
}