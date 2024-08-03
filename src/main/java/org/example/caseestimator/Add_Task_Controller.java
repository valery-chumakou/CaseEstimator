package org.example.caseestimator;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Add_Task_Controller implements Initializable {
    private int rate;
    private String tasks;
    private LocalTime timeSpent;
    private String user;
    private String officeNo;
    private LocalDate date;
    private Double sum;
    private String loggedInUser;
    private final ObservableList<Billing> billingList = FXCollections.observableArrayList();

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
    private TableView<Billing> billing_table_records;
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
    private Button add_task;

    @FXML
    private Button start_btn;

    @FXML
    private Button stop_btn;
    private String getLoggedInUser;
    @FXML
    private Button add_btn;
    private String selectedMenuItem;
    @FXML
    Button time_btn;
    @FXML
    private Label time_label;
    private ScheduledExecutorService scheduler;
    private Stopwatch stopwatch = new Stopwatch();
    private Instant startTime;
    private Billing_Table_Records billingTableRecords;


    public void closeWindow() {
        ((Stage) add_btn.getScene().getWindow()).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInUser = UserStore.getLoggedInUser();
        if (loggedInUser == null) {
            System.out.println("Logged in user is null");
        } else {
            System.out.println("Logged in User:" + loggedInUser);
            fetchUserRate();
        }
        col_no = new TableColumn<>("OfficeNo");
        col_rate = new TableColumn<>("Rate");
        col_tasks = new TableColumn<>("Tasks");
        col_date = new TableColumn<>("Date");
        col_user = new TableColumn<>("User");
        col_time = new TableColumn<>("Time Spent");
        col_sum = new TableColumn<>("Sum");


        col_no.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOfficeNo()));
        col_rate.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getRate()).asObject());
        col_tasks.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTasks()));
        col_date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        col_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser()));
        col_time.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeSpent().toString()));
        col_sum.setCellValueFactory(new PropertyValueFactory<>("sum"));
        billingTableRecords = new Billing_Table_Records(user);


        billing_table_records = new TableView<>(billingList);
        billing_table_records.getColumns().addAll(col_no, col_rate, col_tasks, col_time, col_user, col_date, col_sum);
        stopwatch = new Stopwatch();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        startTime = Instant.now();
        menu_btn.setItems(FXCollections.observableArrayList("Option1", "Option2", "Option3"));
    }

    private void fetchUserRate() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT rate from users where username = ?")) {
            preparedStatement.setString(1, loggedInUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.rate = resultSet.getInt("rate");
                UserStore.setLoggedInRate(this.rate);
                System.out.println("Fetched rate: " + this.rate + "for user" + loggedInUser);
            } else {
                System.err.println("No rate found for the user" + loggedInUser);
                this.rate = 0;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user rate" + e.getMessage());
        }
    }

    @FXML
    public void insertBillingInfo(ActionEvent actionEvent) throws SQLException {
        stopwatch.stopTimer();
        int totalSeconds = (int) ChronoUnit.SECONDS.between(startTime, Instant.now());
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        String timeSpentString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        LocalTime timeSpent = LocalTime.of(totalSeconds/3600,(totalSeconds%3600)/60, totalSeconds%60);
        String tasks = menu_btn.getValue();
        if (tasks == null) {
            System.out.println("No task selected from the menu");
            return;
        }

        int currentRate = rate;
        double sum = currentRate * totalSeconds; // Use the already fetched rate
        System.out.println("Inserting with Rate" + currentRate);
        System.out.println("Sum : " + sum);
        if (loggedInUser != null && officeNo != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO billing (rate, tasks, time_spent, office_number, user, date, sum) VALUES (?, ?, ?, ?, ?, ?,?)")) {

                preparedStatement.setInt(1, currentRate);
                preparedStatement.setString(2, tasks);
                preparedStatement.setString(3, timeSpentString);
                preparedStatement.setString(4, officeNo); // Ensure officeNo is set appropriately earlier.
                preparedStatement.setString(5, loggedInUser);
                preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
                preparedStatement.setDouble(7,sum);
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Billing information inserted successfully.");
                    retrieveBillingData(officeNo);
                    Billing newBilling = new Billing(rate, tasks, LocalTime.parse(timeSpentString, DateTimeFormatter.ofPattern("HH:mm:ss")), loggedInUser, officeNo, LocalDate.now(), sum);
                    Platform.runLater(() -> {
                        billing_table_records.setItems(billingList);
                        populateBillingTable(); // Refresh the table view.
                    });
                }
            } catch (SQLException e) {
                System.err.println("Error inserting billing data: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User or office number not set");

            User user = new User(loggedInUser, rate);
            Billing newBilling = new Billing(rate, tasks, timeSpent, loggedInUser, officeNo, LocalDate.now(), (double) sum);
            billingList.add(newBilling);
            billing_table_records.setItems(billingList);
            populateBillingTable();


        }
    }
    public void retrieveBillingData(String officeNumber) throws IOException {
        // Clear the existing billing records in the list
        billingList.clear();

        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");

            // Prepare the SQL query
            String query = "SELECT * FROM billing WHERE office_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, officeNumber);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                int rate = resultSet.getInt("rate");
                String tasks = resultSet.getString("tasks");
                String timeSpentString = resultSet.getString("time_spent");
                String userName = resultSet.getString("user");
                LocalDate date = resultSet.getDate("date").toLocalDate();  // Ensure you are getting the correct date
                double sum = resultSet.getDouble("sum");

                System.out.println("Retrieved from DB - Task: " + tasks + ", User: " + userName + ", Time Spent: " + timeSpentString + ", Rate: " + rate + ", Date: " + date + ", Sum: " + sum);
                // Parse the time spent into a LocalTime object
                LocalTime timeSpent = LocalTime.parse(timeSpentString, DateTimeFormatter.ofPattern("HH:mm:ss"));

                // Create a Billing object and add it to the billingList
                Billing billingRecord = new Billing(rate, tasks, timeSpent, userName, officeNumber, date, sum);
                billingList.add(billingRecord);
                System.out.println("Added to billing" + billingRecord);
            }

            // Use Platform.runLater to update the UI on the JavaFX Application Thread
            Platform.runLater(() -> {
                billing_table_records.setItems(billingList);
                populateBillingTable();  // Ensure to refresh the UI with the new data
            });


        } catch (SQLException e) {
            System.err.println("Error retrieving billing data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
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

    public void populateBillingTable() {
        billing_table_records.setItems(billingList);

    }


    public void setBilling_table_records() {
        this.billingTableRecords = billingTableRecords;
    }



    public void setUser(String user) {
        this.user = user;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }


}
