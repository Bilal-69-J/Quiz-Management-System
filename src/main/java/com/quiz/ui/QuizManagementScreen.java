package com.quiz.ui;

import com.quiz.dao.QuizDAO;
import com.quiz.model.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import java.util.List;
import java.util.Optional;

/**
 * QuizManagementScreen - Dark Theme (All Blue Buttons)
 */
public class QuizManagementScreen {

    private BorderPane root;
    private TableView<Quiz> table;
    private ObservableList<Quiz> data;

    public QuizManagementScreen() {
        createUI();
        loadData();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);
        root.setPadding(new Insets(20));

        // -- Table Setup --
        table = new TableView<>();
        table.setPlaceholder(new Label("No quizzes available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Dark Theme CSS for TableView
        table.setStyle("-fx-background-color: transparent; " +
                "-fx-control-inner-background: #252525; " +
                "-fx-control-inner-background-alt: #1E1E1E; " +
                "-fx-text-fill: white; " +
                "-fx-table-cell-border-color: #333333;");

        TableColumn<Quiz, Number> idCol = new TableColumn<>("ID");
        idCol.setMaxWidth(80);
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuizId()));

        TableColumn<Quiz, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));

        TableColumn<Quiz, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));

        TableColumn<Quiz, Number> timeCol = new TableColumn<>("Time (s)");
        timeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTimeLimit()));

        TableColumn<Quiz, Number> passCol = new TableColumn<>("Passing");
        passCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPassingMarks()));

        TableColumn<Quiz, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        table.getColumns().addAll(idCol, titleCol, catCol, timeCol, passCol, statusCol);

        // -- Buttons (ALL BLUE NOW) --
        Button addBtn = UIUtil.createPrimaryButton("Add Quiz");

        // Changed these from Ghost to Primary so they are all Blue
        Button editBtn = UIUtil.createPrimaryButton("Edit");
        Button delBtn = UIUtil.createPrimaryButton("Delete");
        Button toggleBtn = UIUtil.createPrimaryButton("Activate/Deactivate");

        addBtn.setOnAction(e -> showEditDialog(null));
        editBtn.setOnAction(e -> { Quiz sel = table.getSelectionModel().getSelectedItem(); if (sel != null) showEditDialog(sel); });
        delBtn.setOnAction(e -> {
            Quiz sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete quiz '" + sel.getTitle() + "'?", ButtonType.YES, ButtonType.NO);
                a.showAndWait().ifPresent(bt -> { if (bt == ButtonType.YES) { try { new QuizDAO().delete(sel.getQuizId()); loadData(); } catch (Exception ex) { showAlert("Error", ex.getMessage()); } } });
            }
        });
        toggleBtn.setOnAction(e -> {
            Quiz sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    sel.setStatus("ACTIVE".equals(sel.getStatus()) ? "INACTIVE" : "ACTIVE");
                    new QuizDAO().update(sel);
                    loadData();
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });

        // -- Toolbar --
        HBox toolbar = new HBox(15, addBtn, editBtn, delBtn, toggleBtn);
        toolbar.setPadding(new Insets(15));
        toolbar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        toolbar.setBackground(new Background(new BackgroundFill(UIUtil.CARD, new CornerRadii(12), Insets.EMPTY)));

        VBox center = new VBox(20, table, toolbar);
        root.setCenter(center);
    }

    private void showEditDialog(Quiz quiz) {
        Dialog<Quiz> dialog = new Dialog<>();
        dialog.setTitle(quiz == null ? "Add Quiz" : "Edit Quiz");

        // Dark Theme Dialog Styling
        dialog.getDialogPane().setStyle("-fx-background-color: #252525; -fx-text-fill: white;");
        dialog.getDialogPane().getStylesheets().add("data:text/css,.label{ -fx-text-fill: white; }");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        String fieldStyle = "-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;";

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle(fieldStyle);

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        categoryField.setStyle(fieldStyle);

        Spinner<Integer> timeSpinner = new Spinner<>(30, 3600, 120);
        timeSpinner.setStyle(fieldStyle);

        Spinner<Integer> passingSpinner = new Spinner<>(0, 100, 1);
        passingSpinner.setStyle(fieldStyle);

        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("ACTIVE", "INACTIVE"));
        statusBox.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-mark-color: white;");

        if (quiz != null) {
            titleField.setText(quiz.getTitle());
            categoryField.setText(quiz.getCategory());
            timeSpinner.getValueFactory().setValue(quiz.getTimeLimit());
            passingSpinner.getValueFactory().setValue(quiz.getPassingMarks());
            statusBox.setValue(quiz.getStatus());
        } else {
            statusBox.setValue("INACTIVE");
        }

        Label lblTitle = new Label("Title"); lblTitle.setTextFill(Color.WHITE);
        Label lblCat = new Label("Category"); lblCat.setTextFill(Color.WHITE);
        Label lblTime = new Label("Time limit (seconds)"); lblTime.setTextFill(Color.WHITE);
        Label lblPass = new Label("Passing marks"); lblPass.setTextFill(Color.WHITE);
        Label lblStat = new Label("Status"); lblStat.setTextFill(Color.WHITE);

        VBox content = new VBox(10, lblTitle, titleField, lblCat, categoryField,
                lblTime, timeSpinner, lblPass, passingSpinner,
                lblStat, statusBox);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveBtn) {
                Quiz q = quiz == null ? new Quiz() : quiz;
                q.setTitle(titleField.getText());
                q.setCategory(categoryField.getText());
                q.setTimeLimit(timeSpinner.getValue());
                q.setPassingMarks(passingSpinner.getValue());
                q.setStatus(statusBox.getValue());
                return q;
            }
            return null;
        });

        Optional<Quiz> result = dialog.showAndWait();
        result.ifPresent(q -> {
            try {
                QuizDAO dao = new QuizDAO();
                if (quiz == null) dao.insert(q);
                else dao.update(q);
                loadData();
            } catch (Exception ex) {
                showAlert("Error saving quiz", ex.getMessage());
            }
        });
    }

    private void loadData() {
        try {
            List<Quiz> list = new QuizDAO().getAll();
            data = FXCollections.observableArrayList(list);
            table.setItems(data);
        } catch (Exception ex) {
            showAlert("Error loading quizzes", ex.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }
}