package org.example.caseestimator;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Billing_Table_Records {
    private String loggedInUser;
    private ObservableList<String> billingList = FXCollections.observableArrayList();
    @FXML
    private TableView<String> billing_table_records;
    @FXML
    private Button save_btn;
    @FXML
    private ComboBox<String> menu_btn;
    @FXML
    private TableColumn<Billing, String> col_no;
    @FXML
    private TableColumn<Billing, String> col_rate;
    @FXML
    private TableColumn<Billing, String> col_tasks;
    @FXML
    private TableColumn<Billing, String> col_time;
    @FXML
    private TableColumn<Billing, String> col_user;
    @FXML
    private TableColumn<Billing,String> col_date;
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

    @FXML
    private Button start_btn;

    @FXML
    private Button stop_btn;
    private String officeNo;
    private String getLoggedInUser;
    @FXML
    private Button add_btn;
    @FXML Button time_btn;
    @FXML
    private Label time_label;
    private ScheduledExecutorService scheduler;
    private final Stopwatch stopwatch = new Stopwatch();
    private Instant startTime;

    public void initialize(String officeNo, ObservableList<String> billingList) throws IOException {
        menu_btn.setItems(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        this.officeNo= officeNo;
        this.billingList = billingList;
        this.loggedInUser = UserStore.getLoggedInUser();
        col_rate.setCellValueFactory(cellData -> new SimpleStringProperty());
        col_tasks.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTasks()));
        col_time.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeSpent()));
        col_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser()));
        col_date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        col_no.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRate())));        retrieveBillingData(officeNo);
        billing_table_records.setItems(FXCollections.observableArrayList(billingList));
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


    @FXML
    public void startTimer() {
        stopwatch.startTimer();
        startTime = Instant.now();
        timer = new Timeline();
        timer.play();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            updateLabel();
        }), 0, 1000, TimeUnit.MILLISECONDS);
    }

    @FXML
    public void stopTimer() {
        stopwatch.stopTimer();
        timer.stop();
        scheduler.shutdownNow();
    }

    @FXML
    public void updateLabel() {
        if (startTime == null) return;
        long seconds = ChronoUnit.SECONDS.between(startTime, Instant.now());
        long minutes = seconds / 60;
        long hours = minutes / 60;

        String timeElapsed = String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
        time_label.setText(timeElapsed);
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
            int rate = Integer.parseInt(rateString);
            String tasks = menu_btn.getValue();
            Billing newBilling = new Billing(rate , tasks, timeSpentString, loggedInUser, officeNo, LocalDate.now());
            // Insert into the database
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO billing (rate, tasks, time_spent, office_number, user) VALUES (?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, rate);
                preparedStatement.setString(2, tasks);
                preparedStatement.setString(3, timeSpentString);
                preparedStatement.setString(4, officeNo);
                preparedStatement.setString(5, loggedInUser);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Billing information inserted successfully.");
                }
                billingList.add(newBilling.toString());
                billing_table_records.setItems(billingList);

            } catch (SQLException e) {
                System.err.println("Error inserting billing information: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid rate format. Please enter a valid integer.");
        }
    }

    public void populateBillingTable() {
        billing_table_records.setItems(FXCollections.observableArrayList(billingList));

    }

    public void retrieveBillingData(String officeNumber) throws IOException {
        billingList.clear(); // Clear existing billing data

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");

            String query = "SELECT * FROM billing WHERE office_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, officeNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int rate = resultSet.getInt("rate");
                String tasks = resultSet.getString("tasks");
                int timeSpent = resultSet.getInt("time_spent");
                String userName = resultSet.getString("user");
                String date = resultSet.getString("date");

                Billing billing = new Billing(rate, tasks, String.valueOf(timeSpent), userName, officeNumber, LocalDate.parse(date));
                billingList.add(billing.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving billing data: " + e.getMessage());
        }
    }
}
