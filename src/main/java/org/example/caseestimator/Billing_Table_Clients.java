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

public class Billing_Table_Clients implements Initializable {
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Billing> billingList = FXCollections.observableArrayList();
    private Client selectedClient;
    @FXML
    private TableView<Client> billing_clients_table;

    @FXML
    private TableColumn<Client, String> col_filingDate;

    @FXML
    private TableColumn<Client, String> col_name;

    @FXML
    private TableColumn<Client, String> col_of_no;

    @FXML
    private TableColumn<Client, String> col_status;

    @FXML
    private TableColumn<Client, String> col_chapter;

    @FXML
    private TableColumn<Client, String> col_type;
    private Connection con;
    private Statement stmt;


    public void populateTable() {
        billing_clients_table.getItems().clear();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

            while (rs.next()) {
                Client client = new Client();
                client.setFirst_name(rs.getString("first_name"));
                client.setLast_name(rs.getString("last_name"));
                client.setBusiness_name(rs.getString("business_name"));
                client.setFiling_date(rs.getString("filing_date"));
                client.setChapter(rs.getString("chapter"));
                client.setType(rs.getString("type"));
                client.setOffice_number(rs.getString("office_number"));
                client.setStatus(rs.getString("status"));

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        billing_clients_table.setItems(clients);
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
        populateTable(); // move this line here
        refreshTable(); // and this line

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
        billing_clients_table.setOnMouseClicked(event->handleDoubleClick(event));
        col_chapter.setCellValueFactory(new PropertyValueFactory<>("chapter"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_filingDate.setCellValueFactory(new PropertyValueFactory<>("filing_date"));
        col_of_no.setCellValueFactory(new PropertyValueFactory<>("office_number"));

    }

    private void openBillingRecords(Client client) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("billing_table_records.fxml"));
        Parent root = loader.load();
        Billing_Table_Records billingRecordsController = loader.getController();
        billingRecordsController.initialize(client.getOffice_number(), billingList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) &&
                event.getClickCount() == 2) {
            selectedClient = billing_clients_table.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                try {
                    openBillingRecords(selectedClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}