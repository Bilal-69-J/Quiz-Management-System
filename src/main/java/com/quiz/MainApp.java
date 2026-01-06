package com.quiz;

import com.quiz.ui.LoginScreen;
import com.quiz.ui.StudentDashboard;
import com.quiz.ui.StudentLoginScreen;
import com.quiz.ui.WelcomeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
                AppNavigator.init(primaryStage);

        // Create welcome screen with actions that use AppNavigator to switch root
        WelcomeScreen welcome = new WelcomeScreen(
                // Admin portal
                () -> {
                    LoginScreen login = new LoginScreen(admin -> {
                        AppNavigator.setRoot(new com.quiz.ui.DashboardScreen(admin).getRoot());
                    });
                    AppNavigator.setRoot(login.getRoot());
                },
                // Student portal
                () -> {
                    StudentLoginScreen studentLogin = new StudentLoginScreen(participant -> {
                        StudentDashboard sd = new StudentDashboard(participant);
                        AppNavigator.setRoot(sd.getRoot());
                    });
                    AppNavigator.setRoot(studentLogin.getRoot());
                }
        );

        Scene scene = new Scene(welcome.getRoot(), 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz Competition & Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}