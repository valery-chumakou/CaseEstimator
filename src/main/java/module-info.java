module org.example.caseestimator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.caseestimator to javafx.fxml;
    exports org.example.caseestimator;
}