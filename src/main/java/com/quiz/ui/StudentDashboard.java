package com.quiz.ui;

import com.quiz.AppNavigator;
import com.quiz.dao.QuizAttemptDAO;
import com.quiz.dao.QuizDAO;
import com.quiz.model.Participant;
import com.quiz.model.Quiz;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.sql.ResultSet;

/**
 * StudentDashboard - Dark Theme Fixed (Table Text Visibility Solved)
 */
public class StudentDashboard {

    private BorderPane root;
    private Participant participant;
    private VBox cardsBox;

    public StudentDashboard(Participant participant) {
        this.participant = participant;
        createUI();
        loadActiveQuizzes();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);

        // Header Account Label
        Label account = new Label("Signed in: " + participant.getName());
        account.setFont(Font.font("Segoe UI", 12));
        account.setTextFill(UIUtil.MUTED);

        HBox header = (HBox) UIUtil.createHeader("Quiz Competition", "Student Portal", account).getChildren().get(0);
        root.setTop(header);

        VBox center = new VBox(20);
        center.setPadding(new Insets(20));

        HBox top = new HBox(15);
        Label welcome = new Label("Welcome, " + participant.getName());
        welcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        welcome.setTextFill(Color.WHITE);

        Button myAttempts = UIUtil.createGhostButton("My Attempts");
        myAttempts.setOnAction(e -> showMyAttempts());

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logout = UIUtil.createGhostButton("Logout");
        logout.setTextFill(Color.web("#EF4444"));

        logout.setOnAction(e -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(
                    () -> AppNavigator.setRoot(new LoginScreen(admin -> AppNavigator.setRoot(new DashboardScreen(admin).getRoot())).getRoot()),
                    () -> AppNavigator.setRoot(new StudentLoginScreen(p -> AppNavigator.setRoot(new StudentDashboard(p).getRoot())).getRoot())
            );
            AppNavigator.setRoot(welcomeScreen.getRoot());
        });

        top.getChildren().addAll(welcome, spacer, myAttempts, logout);
        top.setAlignment(Pos.CENTER_LEFT);

        cardsBox = new VBox(15);
        center.getChildren().addAll(top, cardsBox);

        root.setCenter(UIUtil.createCard(center));
    }

    private void loadActiveQuizzes() {
        try {
            java.util.List<Quiz> list = new QuizDAO().getActiveQuizzes();
            cardsBox.getChildren().clear();

            if (list.isEmpty()) {
                Label emptyLbl = new Label("No active quizzes available at the moment.");
                emptyLbl.setTextFill(UIUtil.MUTED);
                cardsBox.getChildren().add(emptyLbl);
            }

            for (Quiz q : list) {
                HBox card = new HBox(15);
                VBox info = new VBox(6);

                Label title = new Label(q.getTitle());
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
                title.setTextFill(Color.WHITE);

                Label cat = new Label(q.getCategory());
                cat.setTextFill(UIUtil.ACCENT);

                Label meta = new Label("Time: " + q.getTimeLimit() + "s • Passing: " + q.getPassingMarks());
                meta.setTextFill(UIUtil.MUTED);

                info.getChildren().addAll(title, cat, meta);
                Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

                Button start = UIUtil.createPrimaryButton("Start");
                start.setOnAction(ev -> {
                    try {
                        com.quiz.ui.QuizAttemptScreen qs = new com.quiz.ui.QuizAttemptScreen(q, participant);
                        boolean submitted = qs.showAndWait().orElse(false);
                        if (submitted) {
                            showAlert("Attempt submitted. You can view it in My Attempts.");
                        }
                    } catch (Exception ex) {
                        showAlert("Error: " + ex.getMessage());
                    }
                });

                card.getChildren().addAll(info, spacer, start);
                card.setPadding(new Insets(15));
                card.setAlignment(Pos.CENTER_LEFT);
                card.setBackground(new Background(new BackgroundFill(Color.web("#333333"), new CornerRadii(10), Insets.EMPTY)));
                card.setBorder(new Border(new BorderStroke(Color.web("#444444"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));

                cardsBox.getChildren().add(card);
            }
        } catch (Exception ex) {
            showAlert("Error loading quizzes: " + ex.getMessage());
        }
    }

    private void showMyAttempts() {
        try {
            ResultSet rs = new QuizAttemptDAO().getAttemptsByParticipant(participant.getParticipantId());
            TableView<javafx.collections.ObservableList<String>> table = new TableView<>();

            // FIX: Added specific styling for .table-cell and .text to force WHITE color
            String tableCSS = "data:text/css," +
                    ".table-view { -fx-background-color: transparent; }" +
                    ".table-view .column-header-background { -fx-background-color: #1E1E1E; }" +
                    ".table-view .column-header { -fx-background-color: #333333; -fx-size: 40px; -fx-border-color: #1E1E1E; }" +
                    ".table-view .column-header .label { -fx-text-fill: white; -fx-font-weight: bold; }" +
                    ".table-row-cell { -fx-background-color: #252525; }" +
                    ".table-row-cell:odd { -fx-background-color: #1E1E1E; }" +
                    ".table-view .table-cell { -fx-text-fill: white; }" +  // <--- THIS LINE FIXES THE INVISIBLE TEXT
                    ".table-row-cell:filled:hover { -fx-background-color: #444444; }";

            table.getStylesheets().add(tableCSS);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            table.getColumns().addAll(
                    createCol("Attempt ID", 0),
                    createCol("Quiz Title", 1),
                    createCol("Score", 2),
                    createCol("Percentage", 3),
                    createCol("Status", 4),
                    createCol("Attempt Date", 5)
            );
            while (rs.next()) {
                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("attempt_id")));
                row.add(rs.getString("quiz_title"));
                row.add(String.valueOf(rs.getInt("score")));
                row.add(String.valueOf(rs.getDouble("percentage")));
                row.add(rs.getString("status"));
                row.add(String.valueOf(rs.getTimestamp("attempt_date")));
                table.getItems().add(row);
            }
            rs.getStatement().getConnection().close();

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("My Attempts");

            dialog.setGraphic(null);
            dialog.setHeaderText(null);

            dialog.getDialogPane().setStyle("-fx-background-color: #121212;");

            VBox layout = new VBox(20);
            layout.setPadding(new Insets(20));

            Label title = new Label("My Quiz History");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
            title.setTextFill(Color.WHITE);

            Button closeBtn = UIUtil.createPrimaryButton("Close");
            closeBtn.setPrefWidth(150);
            closeBtn.setOnAction(e -> dialog.close());

            HBox btnBox = new HBox(closeBtn);
            btnBox.setAlignment(Pos.CENTER_RIGHT);

            layout.getChildren().addAll(title, table, btnBox);

            dialog.getDialogPane().setContent(layout);
            dialog.getDialogPane().setPrefSize(800, 500);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);

            dialog.showAndWait();
        } catch (Exception ex) {
            showAlert("Error retrieving attempts: " + ex.getMessage());
        }
    }

    private TableColumn<javafx.collections.ObservableList<String>, String> createCol(String title, int idx) {
        TableColumn<javafx.collections.ObservableList<String>, String> col = new TableColumn<>(title);
        col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(idx)));
        return col;
    }

    private void showAlert(String s) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }
}