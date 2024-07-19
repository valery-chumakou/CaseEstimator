package org.example.caseestimator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class New_Client_Controller{
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
    private TextField of_number;
    @FXML
    private RadioButton personal;
    @FXML
    private TextField status;
    @FXML
    private Button save_btn;
    private Clients_Controller clientsController;

    public void setClientsController(Clients_Controller clientsController) {
        this.clientsController = clientsController;
    }

    public void closeWindow() {
        ((Stage) save_btn.getScene().getWindow()).close();
    }

    public void saveClient() {
        String first_name = f_name.getText();
        String last_name = l_name.getText();
        String business_name = corp_name.getText();

        if (first_name.isEmpty() && last_name.isEmpty() && !business_name.isEmpty()) {
            first_name = business_name;
            last_name = "";
        }

        if (!first_name.isEmpty() && !last_name.isEmpty() && business_name.isEmpty()) {
            business_name = "";
        }

        if (date.getValue() == null) {
            System.out.println("Date value is null, cannot save client");
            return;
        }

        Client newClient = new Client();
        newClient.setFirst_name(first_name);
        newClient.setLast_name(last_name);
        newClient.setBusiness_name(business_name);
        newClient.setFiling_date(date.getValue().toString());
        newClient.setChapter(ch11.isSelected() ? "Ch11" : "Ch7");
        newClient.setType(business.isSelected() ? "Business" : "Personal");
        newClient.setOffice_number(of_number.getText());
        newClient.setStatus(status.getText());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
            PreparedStatement pst = con.prepareStatement("INSERT INTO clients " +
                    "(first_name, last_name, business_name, filing_date, chapter, type, status) VALUES (?,?,?,?,?,?,?)");
            pst.setString(1, newClient.getFirst_name());
            pst.setString(2, newClient.getLast_name());
            pst.setString(3, newClient.getBusiness_name());
            pst.setString(4, newClient.getFiling_date());
            pst.setString(5, newClient.getChapter());
            pst.setString(6, newClient.getType());
            pst.setString(7, newClient.getStatus());
            pst.executeUpdate();
            con.close();

            f_name.clear();
            l_name.clear();
            corp_name.clear();
            date.setValue(null);
            ch11.setSelected(false);
            ch7.setSelected(false);
            business.setSelected(false);
            personal.setSelected(false);
            status.clear();
            clientsController.addNewClient(newClient);
        } catch (SQLException e) {
            throw new RuntimeException(e);


        }
    }
}
