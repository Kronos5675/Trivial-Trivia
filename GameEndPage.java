package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class GameEndPage {
    private Scene scene;
    private String genre;

    public GameEndPage(Stage primaryStage, String username, int finalScore, String genre) {
        this.genre = genre;

        // Create UI components
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Username label
        Label usernameLabel = new Label("Username: " + username);
        usernameLabel.setStyle("-fx-font-size: 18px;");

        // Final score label
        Label scoreLabel = new Label("Final Score: " + finalScore);
        scoreLabel.setStyle("-fx-font-size: 18px;");

        // See Leaderboard button
        Button leaderboardButton = new Button("See Leaderboard");
        leaderboardButton.setOnAction(event -> displayLeaderboard(primaryStage));
        leaderboardButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");

        // Back to Main Menu button
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> {
            GenreSelectionPage genreSelectionPage = new GenreSelectionPage(primaryStage,username);
            primaryStage.setScene(genreSelectionPage.getScene());
        });
        backButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");

        // Add components to layout
        layout.getChildren().addAll(usernameLabel, scoreLabel, leaderboardButton, backButton);

        // Create scene
        scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add("style.css");
    }

    public Scene getScene() {
        return scene;
    }

    private void displayLeaderboard(Stage primaryStage) {
        LeaderboardPage leaderboardPage = new LeaderboardPage(primaryStage, genre);
        leaderboardPage.display();
    }
}
