package com.quiz.ui;

import com.quiz.dao.QuizAttemptDAO;
import com.quiz.dao.QuizDAO;
import com.quiz.model.Quiz;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Crucial import for text color
import javafx.util.Callback;     // Crucial import for the cell factory

import java.sql.ResultSet;

/**
 * ResultScreen - Fixed Dropdown Text & Dark Theme
 */
public class ResultScreen {

    private BorderPane root;
    private ComboBox<Quiz> quizCombo;
    private TableView<javafx.collections.ObservableList<String>> table;

    public ResultScreen() {
        createUI();
        loadQuizzes();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);
        root.setPadding(new Insets(20));

        // -- Quiz Selection --
        quizCombo = new ComboBox<>();
        quizCombo.setPromptText("Select a Quiz");
        quizCombo.setPrefWidth(300);

        // Style the box itself
        quizCombo.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");

        // FIX: Cell Factory to make the dropdown list text White and Visible
        Callback<ListView<Quiz>, ListCell<Quiz>> cellFactory = lv -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                // CSS for the individual rows in the dropdown
                setStyle("-fx-text-fill: white; -fx-background-color: #333333;");
            }
        };

        quizCombo.setCellFactory(cellFactory);

        // FIX: Button Cell (The text displayed AFTER you select an item)
        quizCombo.setButtonCell(new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                setStyle("-fx-text-fill: white; -fx-background-color: transparent;");
            }
        });

        quizCombo.setOnAction(e -> loadResults());

        // -- Table Setup --
        table = new TableView<>();
        table.setPlaceholder(new Label("No results to display"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Dark Theme CSS for TableView
        table.setStyle("-fx-background-color: transparent; " +
                "-fx-control-inner-background: #252525; " +
                "-fx-control-inner-background-alt: #1E1E1E; " +
                "-fx-text-fill: white; " +
                "-fx-table-cell-border-color: #333333;");

        Label lblSelect = new Label("Select Quiz to view results:");
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

    private void loadResults() {
        Quiz q = quizCombo.getValue();
        if (q == null) return;
        try {
            ResultSet rs = new QuizAttemptDAO().getResultsByQuiz(q.getQuizId());
            table.getColumns().clear();
            table.getItems().clear();

            table.getColumns().addAll(
                    createCol("Attempt ID", 0),
                    createCol("Participant", 1),
                    createCol("Quiz Title", 2),
                    createCol("Score", 3),
                    createCol("Percentage", 4),
                    createCol("Status", 5),
                    createCol("Attempt Date", 6)
            );

            while (rs.next()) {
                javafx.collections.ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("attempt_id")));
                row.add(rs.getString("participant"));
                row.add(rs.getString("quiz_title"));
                row.add(String.valueOf(rs.getInt("score")));
                row.add(String.valueOf(rs.getDouble("percentage")));
                row.add(rs.getString("status"));
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