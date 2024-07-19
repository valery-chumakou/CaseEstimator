package org.example.caseestimator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class Edit_Client_Controller {
    @FXML
    private RadioButton business;
    @FXML
    private RadioButton ch11;
    @FXML
    private RadioButton ch7;
    @FXML
    private TextField corp_name;
    @FXML
    private DatePicker date;
    @FXML
    private TextField f_name;
    @FXML
    private TextField l_name;
    @FXML
    private RadioButton personal;
    @FXML
    private Button save_btn;
    private Client client;
    private Client selectedClient;
    private TableView<Client>clients_table;
    private ObservableList<Client>clients = FXCollections.observableArrayList();
    @FXML private TextField status;

    @FXML
    public void initialize() {
        save_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.setFirst_name(f_name.getText());
                client.setLast_name(l_name.getText());
                client.setBusiness_name(corp_name.getText());
                client.setFiling_date(date.getValue().toString());
                client.setChapter((ch7.isSelected()) ? "Ch 7" : ((ch11.isSelected()) ? "Ch 11" : ""));
                client.setType((business.isSelected()) ? "Business" : ((personal.isSelected()) ? "Personal" : ""));
                client.setStatus(status.getText());


                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                    PreparedStatement pst = con.prepareStatement("UPDATE clients SET first_name=?, last_name=?, business_name=?, filing_date=?, chapter=?, type=?, office_number=?, status=? WHERE first_name=? AND last_name=?");
                    pst.setString(1, client.getFirst_name());
                    pst.setString(2, client.getLast_name());
                    pst.setString(3, client.getBusiness_name());
                    pst.setString(4, client.getFiling_date());
                    pst.setString(5, client.getChapter());
                    pst.setString(6, client.getType());
                    pst.setString(7, client.getOffice_number());
                    pst.setString(8, client.getStatus());
                    pst.setString(9, client.getFirst_name());
                    pst.setString(10, client.getLast_name());
                    pst.executeUpdate();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                clients_table = new TableView<>();
                refreshTable();

                ((Stage) save_btn.getScene().getWindow()).close();
            }
        });
    }

    public void setClient(Client client) {
        this.client = client;
        f_name.setText(client.getFirst_name());
        l_name.setText(client.getLast_name());
        corp_name.setText(client.getBusiness_name());
        date.setValue(LocalDate.parse(client.getFiling_date()));

        if (client.getChapter()!=null) {
            if (client.getChapter().equals("Ch 7")) {
                ch7.setSelected(true);
                ch11.setSelected(false);
            } else if (client.getChapter().equals("Ch 11")) {
                ch11.setSelected(true);
                ch7.setSelected(false);
            } else {
                ch7.setSelected(false);
                ch11.setSelected(false);
            }
        }
        if (client.getType() != null) {
            if (client.getType().equals("Business")) {
                business.setSelected(true);
                personal.setSelected(false);
            } else if (client.getType().equals("Personal")) {
                personal.setSelected(true);
                business.setSelected(false);
            } else {
                business.setSelected(false);
                personal.setSelected(false);
            }
        }
        status.setText(client.getStatus());
    }

    public void save (ActionEvent event) {
        client.setFirst_name(f_name.getText());
        client.setLast_name(l_name.getText());
        client.setBusiness_name(corp_name.getText());
        client.setFiling_date(date.getValue().toString());
        client.setChapter((ch7.isSelected()) ? "Ch 7" : ((ch11.isSelected()) ? "Ch 11" : ""));
        client.setType((business.isSelected()) ? "Business" : ((personal.isSelected()) ? "Personal" : ""));
         client.setStatus(status.getText());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
            PreparedStatement pst = con.prepareStatement("UPDATE clients SET first_name=?, last_name=?, business_name=?, filing_date=?, chapter=?, type=?, office_number=?, status=? WHERE first_name=? AND last_name=?");
            pst.setString(1, client.getFirst_name());
            pst.setString(2, client.getLast_name());
            pst.setString(3, client.getBusiness_name());
            pst.setString(4, client.getFiling_date());
            pst.setString(5, client.getChapter());
            pst.setString(6, client.getType());
            pst.setString(7, client.getStatus());
            pst.setString(8, client.getFirst_name());
            pst.setString(9, client.getLast_name());
            pst.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ((Stage)save_btn.getScene().getWindow()).close();
    }

    public void refreshTable() {
        clients_table = new TableView<>();
        TableColumn<Client, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Client, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Client, String> businessNameCol = new TableColumn<>("Business Name");
        businessNameCol.setCellValueFactory(new PropertyValueFactory<>("businessName"));
        TableColumn<Client, String> filingDateCol = new TableColumn<>("Filing Date");
        filingDateCol.setCellValueFactory(new PropertyValueFactory<>("filingDate"));
        TableColumn<Client, String> chapterCol = new TableColumn<>("Chapter");
        chapterCol.setCellValueFactory(new PropertyValueFactory<>("chapter"));
        TableColumn<Client, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Client, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        clients_table.getColumns().addAll(firstNameCol, lastNameCol, businessNameCol, filingDateCol,chapterCol, typeCol, statusCol);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clients_table.setItems(clients);
    }
}
