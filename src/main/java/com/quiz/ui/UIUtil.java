package com.quiz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Cursor;

/**
 * UIUtil - "True Dark" Theme.
 * Neutral dark tones (no blue tint) for a cleaner, high-contrast look.
 */
public final class UIUtil {

    // Palette - True Dark (Neutral)
    public static final Color BG = Color.web("#121212");           // True Jet Black background
    public static final Color CARD = Color.web("#252525");         // Dark Grey Surface (Neutral)
    public static final Color ACCENT = Color.web("#3B82F6");       // Bright Blue (High contrast against black)
    public static final Color ACCENT_HOVER = Color.web("#2563EB"); // Slightly darker blue for hover
    public static final Color TEXT_PRIMARY = Color.web("#FFFFFF"); // Pure White
    public static final Color MUTED = Color.web("#A0A0A0");        // Neutral Light Grey

    private UIUtil() {}

    public static VBox createHeader(String title, String subtitle, Node rightNode) {
        HBox header = new HBox();
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setSpacing(12);
        header.setAlignment(Pos.CENTER_LEFT);

        // Header matches Card color (Neutral Dark Grey)
        header.setBackground(new Background(new BackgroundFill(CARD, CornerRadii.EMPTY, Insets.EMPTY)));
        // Border is slightly lighter grey
        header.setBorder(new Border(new BorderStroke(Color.web("#333333"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));

        VBox titleBox = new VBox(4);
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        titleLbl.setTextFill(TEXT_PRIMARY);

        Label subt = new Label(subtitle);
        subt.setFont(Font.font("Segoe UI", 13));
        subt.setTextFill(MUTED);

        titleBox.getChildren().addAll(titleLbl, subt);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().add(titleBox);
        if (rightNode != null) {
            HBox rightWrap = new HBox(rightNode);
            rightWrap.setAlignment(Pos.CENTER_RIGHT);
            header.getChildren().add(rightWrap);
        }

        VBox container = new VBox();
        container.getChildren().add(header);
        return container;
    }

    public static StackPane createCard(Node content) {
        StackPane card = new StackPane(content);
        // INCREASED PADDING: Makes the card feel bigger internally (40px)
        card.setPadding(new Insets(40));
        card.setBackground(new Background(new BackgroundFill(CARD, new CornerRadii(16), Insets.EMPTY)));

        // Sharp black shadow for depth
        DropShadow shadow = new DropShadow(30, 0, 15, Color.BLACK);
        card.setEffect(shadow);

        StackPane.setMargin(content, new Insets(0));
        return card;
    }

    public static Button createPrimaryButton(String text) {
        Button b = new Button(text);
        // Slightly larger font for buttons
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        b.setTextFill(Color.WHITE);

        Background normal = new Background(new BackgroundFill(ACCENT, new CornerRadii(8), Insets.EMPTY));
        Background hover = new Background(new BackgroundFill(ACCENT_HOVER, new CornerRadii(8), Insets.EMPTY));

        b.setBackground(normal);
        // Increased button padding
        b.setPadding(new Insets(12, 24, 12, 24));
        b.setCursor(Cursor.HAND);

        b.setOnMouseEntered(e -> b.setBackground(hover));
        b.setOnMouseExited(e -> b.setBackground(normal));

        return b;
    }

    public static Button createGhostButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        b.setTextFill(TEXT_PRIMARY);

        Background normal = new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(8), Insets.EMPTY));
        Background hover = new Background(new BackgroundFill(Color.web("#333333"), new CornerRadii(8), Insets.EMPTY));

        b.setBackground(normal);
        b.setPadding(new Insets(12, 24, 12, 24));
        b.setCursor(Cursor.HAND);

        b.setOnMouseEntered(e -> b.setBackground(hover));
        b.setOnMouseExited(e -> b.setBackground(normal));

        return b;
    }

    public static void applyAppBackground(Region root) {
        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}