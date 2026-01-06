package com.quiz.ui;

import com.quiz.dao.AdminDAO;
import com.quiz.AppNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.util.function.Consumer;

public class LoginScreen {

    private BorderPane root;
    private Consumer<String> onLoginSuccess;

    public LoginScreen(Consumer<String> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);

        // FIX: Add Back Button to Header
        Button backBtn = UIUtil.createGhostButton("Back");
        backBtn.setOnAction(e -> {
            // Go back to WelcomeScreen
            WelcomeScreen welcome = new WelcomeScreen(
                    () -> AppNavigator.setRoot(new LoginScreen(admin -> AppNavigator.setRoot(new DashboardScreen(admin).getRoot())).getRoot()),
                    () -> AppNavigator.setRoot(new StudentLoginScreen(p -> AppNavigator.setRoot(new StudentDashboard(p).getRoot())).getRoot())
            );
            AppNavigator.setRoot(welcome.getRoot());
        });

        VBox header = UIUtil.createHeader("Quiz Competition", "Admin Login", backBtn);
        root.setTop(header.getChildren().get(0));

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setAlignment(Pos.CENTER);

        Label title = new Label("Administrator");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(UIUtil.TEXT_PRIMARY);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(280);
        usernameField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(280);
        passwordField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888;");

        Label errorLabel = new Label();
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#EF4444"));

        Button loginBtn = UIUtil.createPrimaryButton("Sign in");
        loginBtn.setPrefWidth(280);

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            try {
                if (new AdminDAO().validate(username, password)) {
                    onLoginSuccess.accept(username);
                } else {
                    errorLabel.setText("Invalid credentials.");
                }
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        formBox.getChildren().addAll(new VBox(15, title, usernameField, passwordField, loginBtn, errorLabel));
        Node card = UIUtil.createCard(formBox);
        if (card instanceof Region) ((Region) card).setMaxWidth(450);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(30));
        root.setCenter(center);
    }

    public BorderPane getRoot() { return root; }
}