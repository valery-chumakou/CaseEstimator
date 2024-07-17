package org.example.caseestimator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;

public class CheckUser {
    @FXML
    private TextField existing_username;
    @FXML
    private PasswordField existing_password;


    @FXML
    public User handleExistingUser() throws IOException {
        String name = existing_username.getText();
        String password = existing_password.getText();
        boolean userFound = false;

        if (!name.isEmpty() && !password.isEmpty()) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CaseEstimator", "root", "BostonVenyaGlobe9357");
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

                ps.setString(1, name);
                ps.setString(2, password);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        userFound = true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (userFound) {
                User authenticatedUser = new User(name);
                UserStore.setLoggedInUser(authenticatedUser.getName());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Dashboard_Controller controller = loader.getController();
                Parent root = loader.load();
                Stage stage = new Stage();
                ScrollPane scrollPane = new ScrollPane(root);

                stage.setScene(new Scene(root));

                stage.show();

                Stage currentStage = (Stage) existing_username.getScene().getWindow();
                currentStage.close();

                return authenticatedUser;
            } else {
                System.out.println("User not found");
                return null;
            }
        } else {
            System.out.println("Username or password is empty");
            return null;
        }
    }
}