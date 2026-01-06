package com.quiz.ui;

import com.quiz.AppNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority; // Added for button stretching

/**
 * DashboardScreen - Dark Theme, Fixed Sidebar Width
 */
public class DashboardScreen {

    private BorderPane root;
    private String adminUsername;

    public DashboardScreen(String adminUsername) {
        this.adminUsername = adminUsername;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);


        Label userLabel = new Label("Signed in: " + adminUsername);
        userLabel.setTextFill(UIUtil.TEXT_PRIMARY);
        userLabel.setFont(javafx.scene.text.Font.font("Segoe UI", 14));

        HBox header = (HBox) UIUtil.createHeader("Quiz Competition", "Admin Dashboard", userLabel).getChildren().get(0);
        root.setTop(header);

        VBox menu = new VBox(12);
        menu.setPadding(new Insets(20)); // More padding
        menu.setPrefWidth(260); // FIX: Increased width so text isn't cut off
        menu.setAlignment(Pos.TOP_LEFT);

        // Optional: Add a subtle border to the right of the menu for separation
        menu.setStyle("-fx-border-color: #333333; -fx-border-width: 0 1 0 0;");

        // Create buttons
        Button manageQuizzesBtn = UIUtil.createGhostButton("Manage Quizzes");
        Button manageQuestionsBtn = UIUtil.createGhostButton("Manage Questions");
        Button manageParticipantsBtn = UIUtil.createGhostButton("Manage Participants");
        Button startQuizBtn = UIUtil.createGhostButton("Start Quiz");
        Button viewResultsBtn = UIUtil.createGhostButton("View Results");
        Button leaderboardBtn = UIUtil.createGhostButton("Leaderboard");
        Button logoutBtn = UIUtil.createGhostButton("Logout");


        manageQuizzesBtn.setMaxWidth(Double.MAX_VALUE);
        manageQuestionsBtn.setMaxWidth(Double.MAX_VALUE);
        manageParticipantsBtn.setMaxWidth(Double.MAX_VALUE);
        startQuizBtn.setMaxWidth(Double.MAX_VALUE);
        viewResultsBtn.setMaxWidth(Double.MAX_VALUE);
        leaderboardBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        manageQuestionsBtn.setOnAction(e -> root.setCenter(UIUtil.createCard(new QuestionManagementScreen().getRoot()).getChildren().get(0)));
        manageParticipantsBtn.setOnAction(e -> root.setCenter(UIUtil.createCard(new ParticipantManagementScreen().getRoot()).getChildren().get(0)));
        startQuizBtn.setOnAction(e -> root.setCenter(UIUtil.createCard(new StartQuizScreen().getRoot()).getChildren().get(0)));
        viewResultsBtn.setOnAction(e -> root.setCenter(UIUtil.createCard(new ResultScreen().getRoot()).getChildren().get(0)));
        leaderboardBtn.setOnAction(e -> root.setCenter(UIUtil.createCard(new LeaderboardScreen().getRoot()).getChildren().get(0)));

        logoutBtn.setOnAction(e -> {
            WelcomeScreen welcome = new WelcomeScreen(
                    () -> { AppNavigator.setRoot(new LoginScreen(admin -> AppNavigator.setRoot(new DashboardScreen(admin).getRoot())).getRoot()); },
                    () -> { AppNavigator.setRoot(new StudentLoginScreen(p -> AppNavigator.setRoot(new StudentDashboard(p).getRoot())).getRoot()); }
            );
            AppNavigator.setRoot(welcome.getRoot());
        });

        menu.getChildren().addAll(manageQuizzesBtn, manageQuestionsBtn, manageParticipantsBtn, startQuizBtn, viewResultsBtn, leaderboardBtn, logoutBtn);
        root.setLeft(menu);


        root.setCenter(UIUtil.createCard(new QuizManagementScreen().getRoot()));
    }

    public BorderPane getRoot() {
        return root;
    }
}