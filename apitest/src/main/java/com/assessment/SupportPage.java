package com.assessment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SupportPage extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initializeSupportUI();
    }

    // Initialize Support UI
    private void initializeSupportUI() {
        // Layout for SupportPage
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(20));

        // Label for support options
        Label supportLabel = new Label("Support Options");

        // Support Details
        Label oneTimeSupportLabel = new Label("One-time Support: $50");
        Label monthlySupportLabel = new Label("Monthly Support: $200");

        // Upgrade option
        Label upgradeLabel = new Label("Upgrade to Premium for additional features.");

        // Back button to Home Page
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> navigateBackToHome());

        layout.getChildren().addAll(supportLabel, oneTimeSupportLabel, monthlySupportLabel, upgradeLabel, backButton);

        // Set up scene and stage
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Support Page");
        stage.show();
    }

    // Navigate back to HomePage
    private void navigateBackToHome() {
        // Reinitialize HomePage when going back to Home
        HomePage homePage = new HomePage();
        homePage.start(stage);  // Call start() for the HomePage
    }

    public static void main(String[] args) {
        launch(args);  // Launch the SupportPage application
    }
}
