package com.quiz.ui;

import com.quiz.dao.ParticipantDAO;
import com.quiz.dao.QuizDAO;
import com.quiz.model.Participant;
import com.quiz.model.Quiz;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Added for white text

import java.util.List;

/**
 * StartQuizScreen - Dark Theme Edition
 */
public class StartQuizScreen {

    private BorderPane root;
    private ListView<Quiz> listView;
    private TextField participantName;
    private TextField participantEmail;
    private TextField participantContact;
    private PasswordField participantPassword;

    public StartQuizScreen() {
        createUI();
        loadActiveQuizzes();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root); // Apply dark background
        root.setPadding(new Insets(20)); // Increased padding

        // -- Active Quizzes List --
        listView = new ListView<>();
        // Dark Theme CSS for ListView
        listView.setStyle("-fx-control-inner-background: #252525; " +
                "-fx-control-inner-background-alt: #1E1E1E; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: transparent;");

        listView.setCellFactory(lv -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                setTextFill(Color.WHITE); // Ensure text is white
                setStyle("-fx-background-color: transparent;");
            }
        });

        // -- Inputs Styled for Dark Mode --
        String fieldStyle = "-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;";

        participantName = new TextField();
        participantName.setPromptText("Participant Name");
        participantName.setStyle(fieldStyle);

        participantEmail = new TextField();
        participantEmail.setPromptText("Email");
        participantEmail.setStyle(fieldStyle);

        participantContact = new TextField();
        participantContact.setPromptText("Contact");
        participantContact.setStyle(fieldStyle);

        participantPassword = new PasswordField();
        participantPassword.setPromptText("Password (plain-text)");
        participantPassword.setStyle(fieldStyle);

        Button startBtn = UIUtil.createPrimaryButton("Start Quiz");
        startBtn.setOnAction(e -> {
            Quiz selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select Quiz", "Please select an active quiz.");
                return;
            }
            String name = participantName.getText().trim();
            String email = participantEmail.getText().trim();
            String password = participantPassword.getText().trim();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) { showAlert("Participant", "Please enter name, email and password."); return; }
            try {
                Participant p = new Participant();
                p.setName(name);
                p.setEmail(email);
                p.setContact(participantContact.getText().trim());
                p.setPassword(password);
                ParticipantDAO pdao = new ParticipantDAO();
                Participant existing = pdao.getByEmail(email);
                if (existing == null) {
                    int pid = pdao.insert(p);
                    p.setParticipantId(pid);
                } else {
                    existing.setName(name); existing.setContact(p.getContact()); existing.setPassword(password);
                    pdao.update(existing);
                    p = existing;
                }

                // Open QuizAttemptScreen
                com.quiz.ui.QuizAttemptScreen attemptScreen = new com.quiz.ui.QuizAttemptScreen(selected, p);
                boolean submitted = attemptScreen.showAndWait().orElse(false);
                if (submitted) {
                    showAlert("Done", "Quiz attempt finished and saved.");
                }
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // -- Labels with White Text --
        Label lblActive = new Label("Active Quizzes");
        lblActive.setTextFill(Color.WHITE);
        lblActive.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.BOLD, 14));

        Label lblDetails = new Label("Participant Details");
        lblDetails.setTextFill(Color.WHITE);
        lblDetails.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.BOLD, 14));

        VBox center = new VBox(15, lblActive, listView,
                lblDetails, participantName, participantEmail, participantContact, participantPassword, startBtn);
        center.setPadding(new Insets(10));
        root.setCenter(center);
    }

    private void loadActiveQuizzes() {
        try {
            List<Quiz> list = new QuizDAO().getActiveQuizzes();
            listView.setItems(FXCollections.observableArrayList(list));
        } catch (Exception ex) {
            showAlert("Error loading quizzes", ex.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    public BorderPane getRoot() { return root; }
}