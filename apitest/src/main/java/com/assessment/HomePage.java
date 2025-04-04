package com.assessment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage extends Application {

    private Stage stage;

    // Override the start method
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initializeUI();
    }

    // Initialize UI with fields and buttons
    private void initializeUI() {

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(20));

        // Welcome message
        Label welcomeLabel = new Label("Create User API Request");

        // User Input fields for create user
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        
        Label jobLabel = new Label("Job:");
        TextField jobField = new TextField();

        // Button to make the API request
        Button createUserButton = new Button("Create User");
        
        // API Response Section for create user
        Label responseLabel = new Label("Response will appear here.");
        TextArea responseTextArea = new TextArea();
        responseTextArea.setEditable(false);
        responseTextArea.setPrefHeight(100);
        
        createUserButton.setOnAction(e -> {
            String name = nameField.getText();
            String job = jobField.getText();
            
            if (name.isEmpty() || job.isEmpty()) {
                responseLabel.setText("Please fill in both fields.");
            } else {

                createUser(name, job, responseTextArea);
            }
        });

        // Button to navigate back to the Home Page
        Button backToHomeButton = new Button("Back to Home");
        backToHomeButton.setOnAction(e -> navigateBackToHome());

        // Button to get list of users
        Button getUsersButton = new Button("Get Users List");
        getUsersButton.setOnAction(e -> getUsers(responseTextArea));  // Call method to get users
        
        // Button to navigate to support page
        Button supportButton = new Button("Go to Support");
        supportButton.setOnAction(e -> navigateToSupportPage());
        
        layout.getChildren().addAll(welcomeLabel, nameLabel, nameField, jobLabel, jobField,
                createUserButton, responseLabel, responseTextArea, getUsersButton, supportButton, backToHomeButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Home Page");
        stage.show();
    }

    // API Request to create user
    private void createUser(String name, String job, TextArea responseTextArea) {
        // API endpoint
        String apiUrl = "https://reqres.in/api/users";

        // Prepare the JSON payload
        String payload = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job);

        // Send POST request to the API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        // Send the request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    String responseBody = res.body();
                    int statusCode = res.statusCode();

                    // Format the response for readability
                    String formattedResponse = formatResponse(responseBody);
                    
                    // Display the response in the TextArea
                    responseTextArea.setText("Status: " + statusCode + "\n" + formattedResponse);
                })
                .exceptionally(e -> {
                    responseTextArea.setText("Error: " + e.getMessage());
                    return null;
                });
    }

    // API Request to get a list of users
    private void getUsers(TextArea responseTextArea) {

        String apiUrl = "https://reqres.in/api/users?page=1";

        // Send GET request to the API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        // Send the request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    String responseBody = res.body();
                    int statusCode = res.statusCode();

                    // Format the response for readability
                    String formattedResponse = formatResponse(responseBody);

                    // Display the response in the TextArea
                    responseTextArea.setText("Status: " + statusCode + "\n" + formattedResponse);
                })
                .exceptionally(e -> {
                    responseTextArea.setText("Error: " + e.getMessage());
                    return null;
                });
    }

    // Helper method to format the response JSON
    private String formatResponse(String response) {

        return response.replace(",", ",\n").replace("{", "{\n").replace("}", "\n}");
    }

    // Navigate back to HomePage
    private void navigateBackToHome() {

        new HomePage().start(stage);
    }

    // Navigate to the Support Page
    private void navigateToSupportPage() {
        // Navigate to the SupportPage
        SupportPage supportPage = new SupportPage();
        supportPage.start(stage);
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
