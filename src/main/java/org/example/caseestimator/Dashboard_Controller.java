package org.example.caseestimator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Dashboard_Controller implements Initializable {
    @FXML
    private Button about_btn;

    @FXML
    private PieChart chapter_pie;

    @FXML
    private PieChart clients_pie;


    @FXML
    private Button dashboard_btn;

    @FXML
    private Button exit_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> chapterData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> clientsData = FXCollections.observableArrayList();


        // Create pie charts for chapter and clients
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT chapter, COUNT(*) AS count FROM clients GROUP BY chapter")) {
            while (rs.next()) {
                String name = rs.getString("chapter") == null ? "" : rs.getString("chapter"); // Convert object to string
                double value = rs.getDouble("count");
                PieChart.Data data = new PieChart.Data(name, value);
                chapterData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT type, COUNT(*) AS count FROM clients GROUP BY type")) {
            while (rs.next()) {
                String name;
                double value;
                name = rs.getString("type") == null ? "" : rs.getString("type"); // Convert object to string
                value = rs.getDouble("count");
                PieChart.Data data = new PieChart.Data(name, value);
                clientsData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chapter_pie.setData(chapterData);
        chapter_pie.setTitle("Chapters");

        clients_pie.setData(clientsData);
        clients_pie.setTitle("Clients");


    }
}
