package org.example.caseestimator;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Billing_Table_Records {
    private String loggedInUser;
    private final ObservableList<Billing> billingList = FXCollections.observableArrayList();
    @FXML
    private TableView<Billing> billing_table_records;
    @FXML
    private Button save_btn;
    @FXML
    private ComboBox<String> menu_btn;
    @FXML
    private TableColumn<Billing, String> col_no;
    @FXML
    private TableColumn<Billing, Integer> col_rate;
    @FXML
    private TableColumn<Billing, String> col_tasks;
    @FXML
    private TableColumn<Billing, String> col_time;
    @FXML
    private TableColumn<Billing, String> col_user;
    @FXML
    private TableColumn<Billing, String> col_date;
    @FXML
    private TableColumn<Billing, Double>col_sum;
    @FXML
    private TextField rate_field;
    private Timeline timer;
    @FXML
    private TextField time_spent_field;
    @FXML
    private Button calc_button;
    @FXML
    private Label outst_amount;
    @FXML
    private Button delete_btn;
    @FXML
    private Label attorney_time;
    @FXML
    private Label paralegal_time;

    private int sum;
    @FXML
    private Button start_btn;

    @FXML
    private Button stop_btn;
    private String officeNo;
    private String getLoggedInUser;
    @FXML
    private Button add_btn;
    @FXML
    Button time_btn;
    @FXML
    private Label time_label;
    private ScheduledExecutorService scheduler;
    private final Stopwatch stopwatch = new Stopwatch();
    private Instant startTime;

    public Billing_Table_Records() {

    }
    public Billing_Table_Records(String user) {
        this.loggedInUser = getLoggedInUser;
    }

    public void initialize(String officeNo, ObservableList<Billing> billingList) throws IOException {
         this.officeNo = officeNo;
        this.loggedInUser = UserStore.getLoggedInUser();
        col_no.setCellValueFactory(new PropertyValueFactory<>("officeNo"));
        col_rate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        col_tasks.setCellValueFactory(new PropertyValueFactory<>("tasks"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        col_time.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeSpent().toString()));
        col_sum.setCellValueFactory(new PropertyValueFactory<>("Sum"));
        retrieveBillingData(officeNo); //
        populateBillingTable();
        // Clear existing data


        save_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {

                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
    }


    public void populateBillingTable() {
        billing_table_records.setItems(billingList);

    }

    @FXML
    public void insertBillingInfo() {
        stopwatch.stopTimer();
        int totalSeconds = (int) ChronoUnit.SECONDS.between(startTime, Instant.now());
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        String timeSpentString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        String rateString = rate_field.getText();
        try {
            int rate = Integer.parseInt(rate_field.getText());

            if (rate <= 0) {
                System.out.println("Invalid rate. Please enter a positive integer.");
                return;
            }
            String tasks = menu_btn.getValue();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO billing (rate, tasks, time_spent, office_number, user, date) VALUES (?, ?, ?, ?, ?, ?);")) {
                preparedStatement.setInt(1, rate);
                preparedStatement.setString(2, tasks);
                preparedStatement.setString(3, timeSpentString);
                preparedStatement.setString(4, officeNo);
                preparedStatement.setString(5, loggedInUser);
                preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));

                LocalTime timeSpent = LocalTime.parse(timeSpentString, DateTimeFormatter.ofPattern("HH:mm:ss"));

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Billing information inserted successfully.");
                    Platform.runLater(() -> {
                        // Update your UI here
                        // For example:
                        billingList.add(new Billing(rate, tasks, timeSpent, loggedInUser, officeNo, LocalDate.now(), (double) sum));
                    });
                }
            }
            LocalTime timeSpent = LocalTime.parse(timeSpentString, DateTimeFormatter.ofPattern("HH:mm:ss"));
            User user = new User(loggedInUser);
            Billing newBilling = new Billing(rate, tasks, timeSpent, loggedInUser, officeNo, LocalDate.now(), (double) sum);
            billingList.add(newBilling);
            billing_table_records.setItems(billingList);
            populateBillingTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void retrieveBillingData(String officeNumber) throws IOException {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");

            String query = "SELECT * FROM billing WHERE office_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, officeNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int rate = resultSet.getInt("rate");
                String tasks = resultSet.getString("tasks");
                String timeSpentString = resultSet.getString("time_spent");
                String userName = resultSet.getString("user");
                String date = resultSet.getString("date");
                LocalTime timeSpent = LocalTime.parse(timeSpentString, DateTimeFormatter.ofPattern("HH:mm:ss"));
                User user = new User(userName);

                if (date != null) {
                    Billing billing = new Billing(rate, tasks, timeSpent, userName, officeNumber, LocalDate.parse(date), (double) sum);
                    billingList.add(billing); // Add the Billing object to the observable list
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving billing data: " + e.getMessage());
        }
        billing_table_records.setItems(billingList); // Set the updated list to the table view
        populateBillingTable();
    }
    public void refreshTable() {
        billing_table_records.setItems(billingList);
    }


    public void setLoggedInUser(String user) {
        this.loggedInUser = UserStore.getLoggedInUser();
    }
}