package org.example.caseestimator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Payment_Info_Controller {
    @FXML
    private TextField amount_fld;

    @FXML
    private TextField card_number_fld;

    @FXML
    private TextField cash_fld;

    @FXML
    private Button close_btn;

    @FXML
    private TextField cvv_fld;

    @FXML
    private TextField exp_fld;

    @FXML
    private Button pay_btn;
    private LocalTime time;
    private LocalDate date;
    private String loggedInUser;
    private String getLoggedInUser;

    public void setLoggedInUser(String user) {
        this.loggedInUser = user;
    }

    @FXML
    private void handlePayButtonAction() {
        double amount = Double.parseDouble(amount_fld.getText());
        String cardNumber = card_number_fld.getText();
        String cvv = cvv_fld.getText();
        String cash = cash_fld.getText();
        String expiryDate = exp_fld.getText();


        time = LocalTime.now();
        date = LocalDate.now();
        try {
            savePaymentToDatabase(time, date, loggedInUser, amount);
        } catch (SQLException e) {
            System.err.println("Error saving payment" + e.getMessage());
        }

        System.out.println("Processing payment of $" + amount + "with card" + cardNumber);
        Stage stage = (Stage)pay_btn.getScene().getWindow();
        stage.close();
    }

    private void savePaymentToDatabase(LocalTime time, LocalDate date, String loggedInUser, double amount) throws SQLException {
        String sql = "INSERT INTO payments_history (time, date, user, amount) VALUES (?,?, ?, ?)";
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setTime(1, Time.valueOf(time));
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, loggedInUser);
            pstmt.setDouble(4, amount);
            pstmt.executeUpdate();
        }

    }
    @FXML
    private void handleCloseButtonAction() {
        Stage stage = (Stage)close_btn.getScene().getWindow();
        stage.close();
    }
}
