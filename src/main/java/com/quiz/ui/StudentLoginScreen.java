package com.quiz.ui;

import com.quiz.dao.ParticipantDAO;
import com.quiz.model.Participant;
import com.quiz.AppNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class StudentLoginScreen {

    private BorderPane root;
    private Consumer<Participant> onLogin;

    public StudentLoginScreen(Consumer<Participant> onLogin) {
        this.onLogin = onLogin;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);

        // FIX: Add Back Button
        Button backBtn = UIUtil.createGhostButton("Back");
        backBtn.setOnAction(e -> {
            WelcomeScreen welcome = new WelcomeScreen(
                    () -> AppNavigator.setRoot(new LoginScreen(admin -> AppNavigator.setRoot(new DashboardScreen(admin).getRoot())).getRoot()),
                    () -> AppNavigator.setRoot(new StudentLoginScreen(p -> AppNavigator.setRoot(new StudentDashboard(p).getRoot())).getRoot())
            );
            AppNavigator.setRoot(welcome.getRoot());
        });

        VBox header = UIUtil.createHeader("Quiz Competition", "Student Portal", backBtn);
        root.setTop(header.getChildren().get(0));

        GridPane form = new GridPane();
        form.setHgap(15); form.setVgap(15); form.setAlignment(Pos.CENTER);

        Label title = new Label("Student Login / Register");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(UIUtil.TEXT_PRIMARY);

        String fieldStyle = "-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;";
        TextField nameField = new TextField(); nameField.setPromptText("Full name"); nameField.setStyle(fieldStyle); nameField.setPrefWidth(300);
        TextField emailField = new TextField(); emailField.setPromptText("you@example.com"); emailField.setStyle(fieldStyle); emailField.setPrefWidth(300);
        TextField contactField = new TextField(); contactField.setPromptText("Contact"); contactField.setStyle(fieldStyle); contactField.setPrefWidth(300);
        PasswordField passField = new PasswordField(); passField.setPromptText("Password"); passField.setStyle(fieldStyle); passField.setPrefWidth(300);

        form.addRow(0, label("Name"), nameField);
        form.addRow(1, label("Email"), emailField);
        form.addRow(2, label("Contact"), contactField);
        form.addRow(3, label("Password"), passField);

        Label note = new Label("Enter email & password to login. New users: Fill all fields to register.");
        note.setWrapText(true); note.setTextFill(UIUtil.MUTED); note.setMaxWidth(400);

        Button loginBtn = UIUtil.createPrimaryButton("Login / Register");
        loginBtn.setPrefWidth(400);

        loginBtn.setOnAction(e -> {
            String name = nameField.getText().trim(), email = emailField.getText().trim(), contact = contactField.getText().trim(), pass = passField.getText();
            if (email.isEmpty() || pass.isEmpty()) { showAlert("Email and password required."); return; }
            try {
                ParticipantDAO dao = new ParticipantDAO();
                Participant existing = dao.getByEmail(email);
                if (existing == null) {
                    if (name.isEmpty()) { showAlert("Name required for registration."); return; }
                    Participant np = new Participant(0, name, email, contact, pass);
                    np.setParticipantId(dao.insert(np));
                    onLogin.accept(np);
                } else {
                    Participant valid = dao.validateByEmailAndPassword(email, pass);
                    if (valid != null) onLogin.accept(valid); else showAlert("Invalid password.");
                }
            } catch (Exception ex) { showAlert("Error: " + ex.getMessage()); }
        });

        VBox formBox = new VBox(20, title, form, note, loginBtn);
        formBox.setAlignment(Pos.CENTER); formBox.setPadding(new Insets(10));
        Node card = UIUtil.createCard(formBox);
        if (card instanceof Region) ((Region) card).setMaxWidth(550);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(18));
        root.setCenter(center);
    }

    private Label label(String t) { Label l = new Label(t); l.setTextFill(UIUtil.TEXT_PRIMARY); return l; }
    private void showAlert(String s) { Alert a = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    public BorderPane getRoot() { return root; }
}