package com.example.chatapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;



public class LoginController {
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private PasswordField loginPasswordPasswordField;
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private TextField signUpUsernameTextField;
    @FXML
    private TextField signUpEmailTextField;
    @FXML
    private PasswordField signUpPasswordPasswordField;
    @FXML
    private PasswordField signUpRepeatPasswordPasswordField;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private void onLoginButtonClick() {
        String username = loginUsernameTextField.getText();
        String password = loginPasswordPasswordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            invalidLoginCredentials.setText("Please enter both username and password.");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/userlogin", "root", "1234");
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Stage s = (Stage) signUpUsernameTextField.getScene().getWindow();
                FXMLLoader fx=new FXMLLoader(HelloApplication.class.getResource("Server.fxml"));
                Scene sc2 = new Scene(fx.load());
                s.setScene(sc2);
                System.out.println("Login successful. Redirecting to ServerConnect.fxml");
            } else {
                invalidLoginCredentials.setText("Invalid username or password.");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onSignUpButtonClick() {
        String username = signUpUsernameTextField.getText();
        String email = signUpEmailTextField.getText();
        String password = signUpPasswordPasswordField.getText();
        String repeatPassword = signUpRepeatPasswordPasswordField.getText();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            invalidSignupCredentials.setText("Please fill in all required fields.");
            return;
        }
        if (!password.equals(repeatPassword)) {
            invalidSignupCredentials.setText("Passwords do not match.");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/userlogin", "root", "1234");
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();

            Stage s = (Stage) signUpUsernameTextField.getScene().getWindow();
            FXMLLoader fx=new FXMLLoader(HelloApplication.class.getResource("Server.fxml"));
            Scene sc2 = new Scene(fx.load());
            s.setScene(sc2);
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Other methods and event handlers as needed
}