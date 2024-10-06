module org.example.sortinggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sortinggame to javafx.fxml;
    exports org.example.sortinggame;
}