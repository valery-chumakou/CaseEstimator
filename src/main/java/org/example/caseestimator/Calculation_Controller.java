package org.example.caseestimator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Calculation_Controller implements Initializable {
    @FXML
    private Label attorney_time;

    @FXML
    private Label paralegal_time;

    @FXML
    private Label total_amount;
    private Client selectedClient;

    @FXML
    private Label total_hours;
   private Connection con;
    public Calculation_Controller() {
       paralegal_time = new Label();
   }

   public void calculateAttorneyTime(String attorney_time) {
       this.attorney_time.setText(String.valueOf(attorney_time));
   }

   public void calculateParalegalTime(String paralegal_time) {
       this.paralegal_time.setText(String.valueOf(paralegal_time));
   }



       public void calculateTotalHours(String total_hours) {
       this.total_hours.setText(String.valueOf(total_hours));
       }

       public void calculateTotalAmount(String total_amount) {
        this.total_amount.setText(String.valueOf(total_amount));
       }
       @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
