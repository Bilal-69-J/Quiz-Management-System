package com.quiz;

import javafx.scene.Parent;
import javafx.stage.Stage;

public final class AppNavigator {
    private static Stage primaryStage;

    private AppNavigator() {}

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void setRoot(javafx.scene.Parent root) {
        if (primaryStage == null) throw new IllegalStateException("AppNavigator not initialized. Call AppNavigator.init(stage) first.");
        if (primaryStage.getScene() != null) {
            primaryStage.getScene().setRoot(root);
        } else {
            primaryStage.setScene(new javafx.scene.Scene(root, 900, 600));
        }
    }
}