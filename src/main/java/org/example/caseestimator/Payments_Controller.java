package org.example.caseestimator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Payments_Controller  {
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private Client selectedClient;
    @FXML
    private TableView<Payments> paymentsTableView;
    @FXML
    private Label amount_paid;

    @FXML
    private TableColumn<Payments, String> col_date;

    @FXML
    private TableColumn<Payments, String> col_name;

    @FXML
    private TableColumn<Payments, String> col_no;

    @FXML
    private TableColumn<Payments, Double> col_owned;

    @FXML
    private TableColumn<Payments, Double> col_paid;

    @FXML
    private TableColumn<Payments, String> col_user;
    private String getLoggedInUser;
    private String officeNo;
    private String loggedInUser;

    public Payments_Controller(String user) {
        this.loggedInUser = getLoggedInUser;
    }
    public void initialize(String officeNo, ObservableList<Payments> payments) throws IOException {
        this.officeNo = officeNo;
        this.loggedInUser = UserStore.getLoggedInUser();
        col_no.setCellValueFactory(new PropertyValueFactory<>("officeNo"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        col_paid.setCellValueFactory(new PropertyValueFactory<>("total_paid"));
        col_owned.setCellValueFactory(new PropertyValueFactory<>("total_owned"));


    }

    public void setLoggedInUser(String user) {
        this.loggedInUser = UserStore.getLoggedInUser();
    }
}
