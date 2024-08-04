package org.example.caseestimator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Payment_History_Controller {



    @FXML
    private TableColumn<?, ?> col_amount;

    @FXML
    private TableColumn<?, ?> col_date;

    @FXML
    private TableColumn<?, ?> col_time;

    @FXML
    private TableColumn<?, ?> col_type;

    @FXML
    private TableColumn<?, ?> col_user;
    @FXML
    private Button payment_btn;
    @FXML
    private TableView<Payments> payment_history;
    private String loggedInUser;




    public void makePayment(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment_info.fxml"));
            Parent root = loader.load();
            Payment_Info_Controller paymentInfoController = loader.getController();
            paymentInfoController.setLoggedInUser(loggedInUser);
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Payment");
            paymentStage.initModality(Modality.WINDOW_MODAL);
            paymentStage.initOwner(payment_btn.getScene().getWindow());
            paymentStage.setScene(new Scene(root));
            paymentStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLoggedInUser(String user) {
        this.loggedInUser = user;
    }


}
