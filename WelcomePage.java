package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WelcomePage {
    private Scene scene;
    private String username;

    public WelcomePage(Stage primaryStage) {
        // Create UI components
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setBackground(new Background(new BackgroundFill(Color.rgb(30, 30, 30), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label
        Label titleLabel = new Label("Welcome to Trivia!");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");

        // Enter Username label
        Label enterUsernameLabel = new Label("Enter Username:");
        enterUsernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Username input field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-font-size: 16px;");

        // Enter Game button
        Button enterGameButton = new Button("Enter Game");
        enterGameButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");
        enterGameButton.setOnAction(event -> {
            startGame(primaryStage, usernameField.getText().trim());
        });

        // Allow pressing Enter to start the game
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                startGame(primaryStage, usernameField.getText().trim());
            }
        });

        // Secret key combination for admin panel
        Scene welcomeScene = new Scene(layout, 600, 400);
        welcomeScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A && event.isControlDown() && event.isShiftDown()) {
                // Open Admin Panel
                openAdminPanel(primaryStage);
            }
        });

        // Add components to layout
        layout.getChildren().addAll(titleLabel, enterUsernameLabel, usernameField, enterGameButton);

        // Create scene
        scene = welcomeScene;
    }

    private void startGame(Stage primaryStage, String username) {
        if (!username.isEmpty()) {
            // Transition to the next page (Genre Selection Page)
            GenreSelectionPage genreSelectionPage = new GenreSelectionPage(primaryStage, username);
            primaryStage.setScene(genreSelectionPage.getScene());
        }
    }

    private void openAdminPanel(Stage primaryStage) {
        // Create the admin panel
        AdminPanel adminPanel = new AdminPanel(primaryStage);
        adminPanel.show(); // Show the admin panel
    }

    public Scene getScene() {
        return scene;
    }

    public String getUsername() {
        return username;
    }
}
