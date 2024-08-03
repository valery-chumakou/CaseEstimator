package org.example.caseestimator;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;

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
    private TableColumn<Billing, Double> col_sum;
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
        col_sum.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSum()).asObject());        retrieveBillingData(officeNo); //
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

    @FXML
    public void addTask(ActionEvent actionEvent) throws IOException {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_task.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Add_Task_Controller addTaskController = loader.getController();
            addTaskController.setUser(loggedInUser);
            addTaskController.setOfficeNo(officeNo);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }

    public void showCurrentAmount() throws IOException {
        double totalAmount = 0.0;
        for (Billing billing : billingList) {
            totalAmount+= billing.getSum();
        }
        outst_amount.setText(String.format("%.2f",totalAmount));


    }
    public void populateBillingTable() {
        billing_table_records.setItems(billingList);

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
                User user = new User(userName, rate);
                double sum = resultSet.getDouble("sum");


                if (date != null) {
                    Billing billing = new Billing(rate, tasks, timeSpent, userName, officeNumber, LocalDate.parse(date), (double) sum);
                    billingList.add(billing); // Add the Billing object to the observable list
                }
            }
            Platform.runLater(() -> {
                billing_table_records.setItems(billingList);
                populateBillingTable(); // Ensure to refresh the UI with the new data
            });
        } catch (SQLException e) {
            billing_table_records.setItems(billingList); // Set the updated list to the table view
            populateBillingTable();
        }
        showCurrentAmount();
    }
    public void refreshTable() {
        billing_table_records.setItems(billingList);
    }


    public void setLoggedInUser(String user) {
        this.loggedInUser = UserStore.getLoggedInUser();
    }
}