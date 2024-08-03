package org.example.caseestimator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Payments_Controller implements  Initializable {
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Payments> clientPayments = FXCollections.observableArrayList();
    private Client selectedClient;
    @FXML
    private TableView<Client> paymentsTableView;
    @FXML
    private Label amount_paid;


    @FXML
    private TableColumn<Client, String> col_name;

    @FXML
    private TableColumn<Client, String> col_no;

    @FXML
    private TableColumn<Payments, Double> col_owned;

    @FXML
    private TableColumn<Payments, Double> col_paid;


    private String getLoggedInUser;
    private String officeNo;
    private String loggedInUser;
    private Connection con;
    private Statement stmt;


    public void populateTable() {
        paymentsTableView.getItems().clear();
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

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void refreshTable() {
        paymentsTableView.setItems(clients);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt = con.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateTable();
        refreshTable();

        col_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Client, String> param) {
                Client client = param.getValue();
                if (client.getFirst_name() != null && client.getLast_name() != null) {
                    return new SimpleStringProperty(client.getFirst_name() + " " +
                            client.getLast_name());
                } else if (client.getBusiness_name() != null) {
                    return new SimpleStringProperty(client.getBusiness_name());
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });
        paymentsTableView.setOnMouseClicked(event -> handleDoubleClick(event));
        col_no.setCellValueFactory(new PropertyValueFactory<>("office_number"));
    }

    private void openPaymentRecords(Client client) throws IOException {

    }

    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) &&
                event.getClickCount() == 2) {
            selectedClient = paymentsTableView.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                try {
                    openPaymentRecords(selectedClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}