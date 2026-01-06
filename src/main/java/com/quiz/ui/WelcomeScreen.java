package com.quiz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * WelcomeScreen - Wider Card & Neutral Dark Theme
 */
public class WelcomeScreen {

    private BorderPane root;
    private Runnable openAdmin;
    private Runnable openStudent;

    public WelcomeScreen(Runnable openAdmin, Runnable openStudent) {
        this.openAdmin = openAdmin;
        this.openStudent = openStudent;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root);

        VBox center = new VBox(25); // Increased spacing between elements
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.CENTER);

        // Main Title - Larger
        Label title = new Label("Quiz Competition");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        title.setTextFill(UIUtil.TEXT_PRIMARY);

        Label subText = new Label("Secure Portal Access");
        subText.setFont(Font.font("Segoe UI", 18));
        subText.setTextFill(UIUtil.MUTED);

        // Buttons - Wider
        Button studentBtn = UIUtil.createPrimaryButton("Student Entrance");
        studentBtn.setPrefWidth(300); // Increased width
        studentBtn.setOnAction(e -> openStudent.run());

        Button adminBtn = UIUtil.createGhostButton("Administrator Access");
        adminBtn.setPrefWidth(300); // Increased width
        adminBtn.setOnAction(e -> openAdmin.run());

        VBox box = new VBox(20, title, subText, studentBtn, adminBtn);
        box.setAlignment(Pos.CENTER);

        // This padding is inside the card content
        box.setPadding(new Insets(10));

        Node card = UIUtil.createCard(box);

        // INCREASED CARD WIDTH LIMIT
        if (card instanceof Region) {
            ((Region) card).setMaxWidth(550);
        }

        center.getChildren().add(card);
        root.setCenter(center);
    }

    public BorderPane getRoot() {
        return root;
    }
}