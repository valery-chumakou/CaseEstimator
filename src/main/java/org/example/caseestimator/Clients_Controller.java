package org.example.caseestimator;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Clients_Controller implements Initializable {
    private Client selectedClient;
    @FXML
    private TableView<Client> clients_table;

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
    private Connection con;
    private Statement stmt;
    @FXML
    private TableColumn<Client, String> col_type;
    private ObservableList<Client>clients = FXCollections.observableArrayList();

    public void add_client_btn(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new_client.fxml"));
        Parent root = loader.load();
        New_Client_Controller newClientController = loader.getController();
        newClientController.setClientsController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refreshTable();
    }

    public void populateTable() {
        clients.clear();
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
            con.close();
            refreshTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            col_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Client, String> param) {
                    Client client = param.getValue();
                    if (client.getFirst_name() != null && client.getLast_name()!= null) {
                        return new SimpleStringProperty(client.getFirst_name() + " " +
                                client.getLast_name());
                    } else if (client.getBusiness_name()!= null) {
                        return new SimpleStringProperty(client.getBusiness_name());
                    } else {
                        return new SimpleStringProperty("");
                    }
                }
            });
            col_chapter.setCellValueFactory(new PropertyValueFactory<>("chapter"));
            col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
            col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
            col_filingDate.setCellValueFactory(new PropertyValueFactory<>("filing_date"));
            col_of_no.setCellValueFactory(new PropertyValueFactory<>("office_number"));
    }

    @FXML
    public void deleteClient(ActionEvent event) {
        Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
        if (selectedClient!=null) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                PreparedStatement pst = con.prepareStatement("DELETE FROM clients where first_name = ? and last_name = ?");
                pst.setString(1, selectedClient.getFirst_name());
                pst.setString(2, selectedClient.getLast_name());
                pst.executeUpdate();
                clients.remove(selectedClient);
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void editClient(ActionEvent event) throws IOException {
        Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Edit_Client_Controller editClientController = loader.getController();
            editClientController.setClient(selectedClient);
            stage.showAndWait();

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                PreparedStatement pst = con.prepareStatement("UPDATE clients SET first_name=?, last_name=?, business_name=?, filing_date=?, chapter=?, type=?, office_number=?, status=? WHERE first_name=? AND last_name=?");
                pst.setString(1, selectedClient.getFirst_name());
                pst.setString(2, selectedClient.getLast_name());
                pst.setString(3, selectedClient.getBusiness_name());
                pst.setString(4, selectedClient.getFiling_date());
                pst.setString(5, selectedClient.getChapter());
                pst.setString(6, selectedClient.getType());
                pst.setString(7, selectedClient.getOffice_number());
                pst.setString(8, selectedClient.getStatus());
                pst.setString(9, selectedClient.getFirst_name());
                pst.setString(10, selectedClient.getLast_name());
                pst.executeUpdate();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
                clients.clear();
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
    }
    public void refreshTable() {
        clients_table.setItems(clients);
    }

    public void addNewClient(Client newClient) {
        clients.add(newClient);
    }
}
