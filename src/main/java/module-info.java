module org.example.caseestimator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.caseestimator to javafx.fxml;
    exports org.example.caseestimator;
}