package org.example.caseestimator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Payments_Controller implements Initializable {
    private ObservableList<Payments>clientPayments = FXCollections.observableArrayList();

     @FXML
    private TableView<Payments> paymentsTableView;
    @FXML
    private Label amount_paid;


    @FXML
    private TableColumn<Payments, String> col_name;

    @FXML
    private TableColumn<Payments, String> col_no;

    @FXML
    private TableColumn<Payments, Double> col_owned;

    @FXML
    private TableColumn<Payments, Double> col_paid;


    private String getLoggedInUser;
    private String officeNo;
    private String loggedInUser;
    private Connection con;
    private Statement stmt;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_name.setCellValueFactory(param -> {
            Client client = param.getValue().getClient();
            return new SimpleStringProperty(client.getFirst_name() + " " + client.getLast_name());
        });
        col_no.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClient().getOffice_number()));
        col_owned.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getTotalOwned()).asObject());
        col_paid.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTotal_paid()));

        paymentsTableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Payments selectedPayment = paymentsTableView.getSelectionModel().getSelectedItem();
                if (selectedPayment != null) {
                    openPaymentHistory();
                }
            }
        });
        populateTable(); // Populate table on initialization

    }

    public void populateTable() {
        clientPayments.clear(); // Use clientPayments to store the Payments objects
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

            while (rs.next()) {
                Client client = new Client();
                client.setFirst_name(rs.getString("first_name"));
                client.setLast_name(rs.getString("last_name"));
                client.setBusiness_name(rs.getString("business_name"));
                client.setOffice_number(rs.getString("office_number"));

                // Calculate the total amount owned for this client
                double totalOwned = getTotalOwnedAmountForClient(con, client.getOffice_number());
                double totalPaid = 0.0;
                Payments payment = new Payments(client, totalOwned, totalPaid);

                clientPayments.add(payment); // Add Payments object to the list
                savePayment(client.getFirst_name(), client.getLast_name(),client.getBusiness_name() , client.getOffice_number(),totalPaid, totalOwned);
            }
            refreshTable(); // Refresh the table with new data

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private double getTotalOwnedAmountForClient(Connection conn, String officeNumber) throws SQLException {
        String query = "SELECT SUM(sum) FROM billing WHERE office_number = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, officeNumber);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            Double total = resultSet.getDouble(1);
            return  total!=null ? total: 0.0; // Get the total amount from the result
        }
        return 0.0; // Default to 0 if no records found
    }
    public void savePayment(String firstName, String lastName,String business_name, String officeNo, double totalOwned, double totalPaid) throws SQLException {
        String query = "INSERT INTO payments (office_number, total_owned, total_paid, business_name,first_name, last_name) VALUES (?,?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, officeNo);
            pstmt.setDouble(2, totalOwned);
            pstmt.setDouble(3, totalPaid);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, business_name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openPaymentHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment_history.fxml"));
            Pane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Payment Info");
            stage.setScene(new Scene(pane));
            Payment_History_Controller controller = loader.getController();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTable() {
        paymentsTableView.setItems(clientPayments);
    }
}