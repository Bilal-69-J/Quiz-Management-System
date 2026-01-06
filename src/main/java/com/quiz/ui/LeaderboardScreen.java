package com.quiz.ui;

import com.quiz.dao.QuizAttemptDAO;
import com.quiz.dao.QuizDAO;
import com.quiz.model.Quiz;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Added for white text

import java.sql.ResultSet;

/**
 * LeaderboardScreen - Top participants (Dark Theme Styled).
 */
public class LeaderboardScreen {

    private BorderPane root;
    private ComboBox<Quiz> quizCombo;
    private TableView<javafx.collections.ObservableList<String>> table;

    public LeaderboardScreen() {
        createUI();
        loadQuizzes();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);
        root.setPadding(new Insets(20));

        // -- Quiz Selector --
        quizCombo = new ComboBox<>();
        quizCombo.setPromptText("Select a Quiz");
        quizCombo.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");
        quizCombo.setPrefWidth(300);

        // --- FIX START: Define how to display the Quiz item (Title + White Color) ---
        quizCombo.setCellFactory(lv -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                setStyle("-fx-text-fill: white; -fx-background-color: #333333;");
            }
        });
        quizCombo.setButtonCell(new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                setStyle("-fx-text-fill: white; -fx-background-color: transparent;");
            }
        });
        // --- FIX END ---

        quizCombo.setOnAction(e -> loadLeaderboard());

        // -- Table Setup --
        table = new TableView<>();
        table.setPlaceholder(new Label("No leaderboard data available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setStyle("-fx-background-color: transparent; " +
                "-fx-control-inner-background: #252525; " +
                "-fx-control-inner-background-alt: #1E1E1E; " +
                "-fx-text-fill: white; " +
                "-fx-table-cell-border-color: #333333;");

        Label lblSelect = new Label("Select Quiz for Leaderboard:");
        lblSelect.setTextFill(Color.WHITE);
        lblSelect.setFont(javafx.scene.text.Font.font("Segoe UI", 14));

        VBox center = new VBox(10, lblSelect, quizCombo, table);
        center.setPadding(new Insets(10));
        root.setCenter(center);
    }

    private void loadQuizzes() {
        try {
            quizCombo.getItems().addAll(new QuizDAO().getAll());
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void loadLeaderboard() {
        Quiz q = quizCombo.getValue();
        if (q == null) return;
        try {
            ResultSet rs = new QuizAttemptDAO().getLeaderboard(q.getQuizId());
            table.getColumns().clear();
            table.getItems().clear();
            table.getColumns().addAll(
                    createCol("Participant", 0),
                    createCol("Percentage", 1),
                    createCol("Score", 2),
                    createCol("Attempt Date", 3)
            );
            while (rs.next()) {
                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                row.add(rs.getString("name"));
                row.add(String.valueOf(rs.getDouble("percentage")));
                row.add(String.valueOf(rs.getInt("score")));
                row.add(String.valueOf(rs.getTimestamp("attempt_date")));
                table.getItems().add(row);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private TableColumn<javafx.collections.ObservableList<String>, String> createCol(String title, int idx) {
        TableColumn<javafx.collections.ObservableList<String>, String> col = new TableColumn<>(title);
        col.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(idx)));
        return col;
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    public BorderPane getRoot() { return root; }
}