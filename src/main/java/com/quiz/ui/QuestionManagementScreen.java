package com.quiz.ui;

import com.quiz.dao.QuestionDAO;
import com.quiz.dao.QuizDAO;
import com.quiz.model.Question;
import com.quiz.model.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class QuestionManagementScreen {

    private BorderPane root;
    private ComboBox<Quiz> quizCombo;
    private TableView<Question> table;
    private ObservableList<Question> data;

    public QuestionManagementScreen() {
        createUI();
        loadQuizzes();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);
        root.setPadding(new Insets(20));

        // -- Quiz Selector --
        quizCombo = new ComboBox<>();
        quizCombo.setPromptText("Select Quiz");
        quizCombo.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-mark-color: white; -fx-font-size: 14px;");
        quizCombo.setPrefWidth(300);

        // --- FIX: Display Quiz Titles Correctly in Dropdown ---
        Callback<ListView<Quiz>, ListCell<Quiz>> cellFactory = lv -> new ListCell<Quiz>() {
            @Override
            protected void updateItem(Quiz item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle() + " (" + item.getCategory() + ")");
                setStyle("-fx-text-fill: white; -fx-background-color: #333333;");
            }
        };
        quizCombo.setCellFactory(cellFactory);
        quizCombo.setButtonCell(cellFactory.call(null)); // Apply to the selected item view too
        // -----------------------------------------------------

        quizCombo.setOnAction(e -> loadQuestions());

        table = new TableView<>();
        table.setPlaceholder(new Label("No questions available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #252525; -fx-control-inner-background-alt: #1E1E1E; -fx-text-fill: white; -fx-table-cell-border-color: #333333;");

        TableColumn<Question, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuestionId()));
        idCol.setMaxWidth(60);

        TableColumn<Question, String> textCol = new TableColumn<>("Question");
        textCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getQuestionText()));

        table.getColumns().addAll(idCol, textCol);

        Button addBtn = UIUtil.createPrimaryButton("Add Question");
        Button editBtn = UIUtil.createPrimaryButton("Edit");
        Button delBtn = UIUtil.createPrimaryButton("Delete");

        addBtn.setOnAction(e -> showQuestionDialog(null));
        editBtn.setOnAction(e -> { Question sel = table.getSelectionModel().getSelectedItem(); if (sel != null) showQuestionDialog(sel); });
        delBtn.setOnAction(e -> {
            Question sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try { new QuestionDAO().delete(sel.getQuestionId()); loadQuestions(); } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
            }
        });

        HBox btns = new HBox(15, addBtn, editBtn, delBtn);
        btns.setPadding(new Insets(15));
        btns.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        btns.setBackground(new Background(new BackgroundFill(UIUtil.CARD, new CornerRadii(12), Insets.EMPTY)));

        VBox center = new VBox(20, quizCombo, table, btns);
        root.setCenter(center);
    }

    private void showQuestionDialog(Question q) {
        Quiz selectedQuiz = quizCombo.getValue();
        if (selectedQuiz == null) { showAlert("Select Quiz", "Please select a quiz first."); return; }

        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle(q == null ? "Add Question" : "Edit Question");
        dialog.getDialogPane().setStyle("-fx-background-color: #252525; -fx-text-fill: white;");
        dialog.getDialogPane().getStylesheets().add("data:text/css,.label{ -fx-text-fill: white; }");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        String fieldStyle = "-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;";
        TextArea qText = new TextArea(); qText.setPromptText("Question text"); qText.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: white;"); qText.setPrefHeight(100);

        TextField a = new TextField(); a.setPromptText("Option A"); a.setStyle(fieldStyle);
        TextField b = new TextField(); b.setPromptText("Option B"); b.setStyle(fieldStyle);
        TextField c = new TextField(); c.setPromptText("Option C"); c.setStyle(fieldStyle);
        TextField d = new TextField(); d.setPromptText("Option D"); d.setStyle(fieldStyle);

        ToggleGroup tg = new ToggleGroup();
        RadioButton ra = new RadioButton("A"); ra.setToggleGroup(tg); ra.setTextFill(Color.WHITE);
        RadioButton rb = new RadioButton("B"); rb.setToggleGroup(tg); rb.setTextFill(Color.WHITE);
        RadioButton rc = new RadioButton("C"); rc.setToggleGroup(tg); rc.setTextFill(Color.WHITE);
        RadioButton rd = new RadioButton("D"); rd.setToggleGroup(tg); rd.setTextFill(Color.WHITE);

        if (q != null) {
            qText.setText(q.getQuestionText());
            a.setText(q.getOptionA()); b.setText(q.getOptionB()); c.setText(q.getOptionC()); d.setText(q.getOptionD());
            switch (q.getCorrectOption()) { case "A": ra.setSelected(true); break; case "B": rb.setSelected(true); break; case "C": rc.setSelected(true); break; case "D": rd.setSelected(true); break; }
        }

        VBox content = new VBox(10, new Label("Question"), qText, new Label("Options"), a, b, c, d, new Label("Correct"), new HBox(15, ra, rb, rc, rd));
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Question qq = q == null ? new Question() : q;
                qq.setQuizId(selectedQuiz.getQuizId());
                qq.setQuestionText(qText.getText());
                qq.setOptionA(a.getText()); qq.setOptionB(b.getText()); qq.setOptionC(c.getText()); qq.setOptionD(d.getText());
                RadioButton sel = (RadioButton) tg.getSelectedToggle();
                qq.setCorrectOption(sel == null ? "A" : sel.getText());
                return qq;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(qq -> {
            try { QuestionDAO dao = new QuestionDAO(); if (q == null) dao.insert(qq); else dao.update(qq); loadQuestions(); } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
    }

    private void loadQuizzes() {
        try { quizCombo.setItems(FXCollections.observableArrayList(new QuizDAO().getAll())); } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
    }

    private void loadQuestions() {
        Quiz q = quizCombo.getValue();
        if (q == null) return;
        try { data = FXCollections.observableArrayList(new QuestionDAO().getAllByQuiz(q.getQuizId())); table.setItems(data); } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
    }

    private void showAlert(String title, String msg) { Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK); a.setHeaderText(title); a.showAndWait(); }
    public BorderPane getRoot() { return root; }
}