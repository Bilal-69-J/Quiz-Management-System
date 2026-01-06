package com.quiz.ui;

import com.quiz.dao.QuestionDAO;
import com.quiz.dao.QuizAttemptDAO;
import com.quiz.model.AttemptAnswer;
import com.quiz.model.Participant;
import com.quiz.model.Question;
import com.quiz.model.Quiz;
import com.quiz.model.QuizAttempt;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizAttemptScreen {

    private Quiz quiz;
    private Participant participant;
    private List<Question> questions;
    private int currentIndex = 0;
    private List<String> selectedOptions;
    private Label timerLabel;
    private Timeline timeline;
    private int remainingSeconds;
    private Dialog<Boolean> dialog;
    private ToggleGroup toggleGroup;
    private VBox contentBox;
    private Button submitBtn; // Reference to disable it during save

    public QuizAttemptScreen(Quiz quiz, Participant participant) throws Exception {
        this.quiz = quiz;
        this.participant = participant;
        // FIX 1: Get 10 RANDOM questions instead of all
        this.questions = new QuestionDAO().getRandomQuestions(quiz.getQuizId(), 10);

        this.selectedOptions = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) selectedOptions.add(null);
        this.remainingSeconds = Math.max(quiz.getTimeLimit(), 30);
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Quiz: " + quiz.getTitle());
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.getDialogPane().setPrefSize(900, 600);
        dialog.getDialogPane().setStyle("-fx-background-color: #121212;");
        dialog.getDialogPane().getStylesheets().add("data:text/css,.label{ -fx-text-fill: white; }");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
        dialog.setResultConverter(btn -> false);

        BorderPane root = new BorderPane();
        UIUtil.applyAppBackground(root);
        root.setPadding(new Insets(30));

        HBox top = new HBox(20);
        VBox titleBox = new VBox(5);
        Label title = new Label(quiz.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        Label subTitle = new Label("Question " + (currentIndex + 1) + " of " + questions.size());
        subTitle.setFont(Font.font("Segoe UI", 14));
        subTitle.setTextFill(UIUtil.MUTED);
        titleBox.getChildren().addAll(title, subTitle);

        timerLabel = new Label();
        timerLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setPadding(new Insets(10, 20, 10, 20));
        timerLabel.setBackground(new Background(new BackgroundFill(Color.web("#EF4444"), new CornerRadii(30), Insets.EMPTY)));

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        top.getChildren().addAll(titleBox, spacer, timerLabel);

        contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER_LEFT);
        StackPane questionCard = UIUtil.createCard(contentBox);
        questionCard.setMaxWidth(Double.MAX_VALUE);
        questionCard.setMaxHeight(Double.MAX_VALUE);

        updateCenter();

        HBox controls = new HBox(20);
        controls.setPadding(new Insets(20, 0, 0, 0));
        Button prev = UIUtil.createGhostButton("Previous");
        Button next = UIUtil.createPrimaryButton("Next");
        submitBtn = UIUtil.createPrimaryButton("Submit Quiz");
        submitBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");

        Region controlSpacer = new Region(); HBox.setHgrow(controlSpacer, Priority.ALWAYS);

        prev.setOnAction(e -> {
            saveSelection();
            if (currentIndex > 0) {
                currentIndex--;
                subTitle.setText("Question " + (currentIndex + 1) + " of " + questions.size());
                updateCenter();
            }
        });

        next.setOnAction(e -> {
            saveSelection();
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                subTitle.setText("Question " + (currentIndex + 1) + " of " + questions.size());
                updateCenter();
            }
        });

        submitBtn.setOnAction(e -> { saveSelection(); evaluateAndSave(); });

        controls.getChildren().addAll(prev, controlSpacer, next, submitBtn);
        root.setTop(top); root.setCenter(questionCard); root.setBottom(controls);
        dialog.getDialogPane().setContent(root);
        startTimer();
    }

    private void updateCenter() {
        contentBox.getChildren().clear();
        toggleGroup = new ToggleGroup();
        if (questions.isEmpty()) { contentBox.getChildren().add(new Label("No questions available.")); return; }

        Question q = questions.get(currentIndex);
        Label qLabel = new Label(q.getQuestionText());
        qLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 22));
        qLabel.setTextFill(Color.WHITE);
        qLabel.setWrapText(true);

        String style = "-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10;";
        RadioButton ra = new RadioButton("A. " + q.getOptionA()); ra.setStyle(style);
        RadioButton rb = new RadioButton("B. " + q.getOptionB()); rb.setStyle(style);
        RadioButton rc = new RadioButton("C. " + q.getOptionC()); rc.setStyle(style);
        RadioButton rd = new RadioButton("D. " + q.getOptionD()); rd.setStyle(style);

        ra.setToggleGroup(toggleGroup); rb.setToggleGroup(toggleGroup); rc.setToggleGroup(toggleGroup); rd.setToggleGroup(toggleGroup);

        String prevSel = selectedOptions.get(currentIndex);
        if ("A".equals(prevSel)) ra.setSelected(true);
        if ("B".equals(prevSel)) rb.setSelected(true);
        if ("C".equals(prevSel)) rc.setSelected(true);
        if ("D".equals(prevSel)) rd.setSelected(true);

        contentBox.getChildren().addAll(qLabel, new VBox(15, ra, rb, rc, rd));
    }

    private void saveSelection() {
        if (toggleGroup == null) return;
        Toggle sel = toggleGroup.getSelectedToggle();
        if (sel instanceof RadioButton) {
            String txt = ((RadioButton) sel).getText();
            String val = null;
            if (txt.startsWith("A.")) val = "A"; else if (txt.startsWith("B.")) val = "B"; else if (txt.startsWith("C.")) val = "C"; else if (txt.startsWith("D.")) val = "D";
            selectedOptions.set(currentIndex, val);
        }
    }

    private void startTimer() {
        updateTimerLabel();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                timeline.stop();
                saveSelection();
                Platform.runLater(this::evaluateAndSave);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimerLabel() {
        timerLabel.setText(String.format("TIME: %02d:%02d", remainingSeconds / 60, remainingSeconds % 60));
    }

    // FIX 2: Run DB operation in background thread to avoid freezing UI
    private void evaluateAndSave() {
        if (timeline != null) timeline.stop();
        submitBtn.setDisable(true);
        submitBtn.setText("Submitting...");

        new Thread(() -> {
            try {
                int correct = 0;
                List<AttemptAnswer> answers = new ArrayList<>();
                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);
                    String sel = selectedOptions.get(i);
                    if (sel != null && sel.equalsIgnoreCase(q.getCorrectOption())) correct++;
                    answers.add(new AttemptAnswer(0, q.getQuestionId(), sel == null ? "" : sel));
                }
                int score = correct;
                double percentage = (double) score / questions.size() * 100;
                String status = score >= quiz.getPassingMarks() ? "Pass" : "Fail";

                QuizAttempt attempt = new QuizAttempt();
                attempt.setParticipantId(participant.getParticipantId());
                attempt.setQuizId(quiz.getQuizId());
                attempt.setScore(score);
                attempt.setPercentage(percentage);
                attempt.setStatus(status);

                new QuizAttemptDAO().insertAttemptAndAnswers(attempt, answers);

                Platform.runLater(() -> {
                    dialog.setResult(Boolean.TRUE);
                    dialog.close();
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    submitBtn.setDisable(false);
                    submitBtn.setText("Submit Quiz");
                    Alert a = new Alert(Alert.AlertType.ERROR, "Error submitting: " + ex.getMessage(), ButtonType.OK);
                    a.showAndWait();
                });
            }
        }).start();
    }

    public Optional<Boolean> showAndWait() { return dialog.showAndWait(); }
}