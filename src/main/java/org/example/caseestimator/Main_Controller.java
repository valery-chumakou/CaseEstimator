package org.example.caseestimator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class Main_Controller implements Initializable {
    @FXML
    private Label name_lbl;
    @FXML
    private Label greeting_lbl;
    @FXML
    private Pane side_panel;
    @FXML
    private AnchorPane base_panel;
    @FXML
    private Pane main_panel;
    @FXML
    private Button about_btn;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button clients_btn;
    private boolean isAboutBtnSelected = false;
    private boolean isClientsBtnSelected = false;
    private boolean isDashboardBtnSelected = false;
    LocalTime currentTime = LocalTime.now();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        main_panel.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent dashboard = null;
        try {
            dashboard = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        main_panel.getChildren().add(dashboard);
        String greeting = "";
        if (currentTime.isBefore(LocalTime.of(12, 0)) &&
                currentTime.isAfter(LocalTime.of(6, 0))) {
            greeting = "Good morning";
        } else if (currentTime.isBefore(LocalTime.of(18, 0)) &&
                currentTime.isAfter(LocalTime.of(12, 0))) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }

        about_btn.setCursor(Cursor.HAND);
        clients_btn.setCursor(Cursor.HAND);
        dashboard_btn.setCursor(Cursor.HAND);


        greeting_lbl.setText(greeting);
        name_lbl.setText(UserStore.getLoggedInUser());

    }

    @FXML
    private void closeProgram(ActionEvent event) throws  IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Do you want to log out?");
        alert.showAndWait();

        Optional<ButtonType> result = Optional.ofNullable(alert.getResult());
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    private void openClientsTable(ActionEvent event) throws  IOException {
        main_panel.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clients.fxml"));
        Parent clients = loader.load();
        main_panel.getChildren().add(clients);
        isAboutBtnSelected = false;
        isDashboardBtnSelected = false;
        isClientsBtnSelected = true;

    }

    @FXML
    private void openAboutScene(ActionEvent event) throws  IOException {
        main_panel.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
        Parent about = loader.load();
        main_panel.getChildren().add(about);
        isAboutBtnSelected = true;
        isClientsBtnSelected = false;
        isDashboardBtnSelected = true;

        updateButtonStyles();
    }

    private void updateButtonStyles() {
        about_btn.setStyle(getButtonStyle(isAboutBtnSelected));
        clients_btn.setStyle(getButtonStyle(isClientsBtnSelected));
        dashboard_btn.setStyle(getButtonStyle(isDashboardBtnSelected));
    }

    private String getButtonStyle(boolean isSelected) {
        return "-fx-text-fill:linear-gradient("+(isSelected? "#19909b" : "#000000") + "," + (isSelected ? "#17798a" : "#000000") + ");";
    }

}




