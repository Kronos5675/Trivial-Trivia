package ui;

import data.DatabaseHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.util.List;

public class LeaderboardPage implements LeaderboardDisplay {
    private Stage primaryStage;
    private String username;
    private List<String> leaderboardData;

    public LeaderboardPage(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
        fetchLeaderboardData();
    }

    private void fetchLeaderboardData() {
        // Fetch leaderboard data
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        try {
            leaderboardData = databaseHandler.getLeaderboardData(username);
        } catch (SQLException e)     {
            e.printStackTrace();
        }
    }

    public void display() {
        Stage leaderboardStage = new Stage();
        leaderboardStage.initModality(Modality.APPLICATION_MODAL);
        leaderboardStage.initStyle(StageStyle.UNDECORATED);
        leaderboardStage.initOwner(primaryStage);

        // Create UI components
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #121212;");

        // Leaderboard title
        Label titleLabel = new Label("Leaderboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox leaderboardBox = new VBox(10);
        leaderboardBox.setAlignment(Pos.TOP_CENTER);
        leaderboardBox.setPadding(new Insets(20));
        leaderboardBox.setStyle("-fx-background-color: #212121; -fx-border-color: #1DB954; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Add leaderboard data to layout
        if (leaderboardData != null && !leaderboardData.isEmpty()) {
            int rank = 1;
            for (String data : leaderboardData) {
                String[] parts = data.split(" - Score: ");
                Label rankLabel = new Label(String.format("%-5s", rank + "."));
                rankLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                Label nameLabel = new Label(String.format("%-20s", parts[0]));
                nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                Label scoreLabel = new Label(parts[1]);
                scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                HBox entry = new HBox(20);
                entry.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
                leaderboardBox.getChildren().add(entry);
                rank++;
            }
        } else {
            Label noDataLabel = new Label("Leaderboard is empty.");
            noDataLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            leaderboardBox.getChildren().add(noDataLabel);
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 16px; -fx-background-color: #1DB954; -fx-text-fill: white;");
        closeButton.setOnAction(event -> leaderboardStage.close());

        // Add components to layout
        layout.getChildren().addAll(titleLabel, leaderboardBox, closeButton);

        // Create scene
        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add("style.css"); // Add external CSS

        // Set the scene
        leaderboardStage.setScene(scene);
        leaderboardStage.showAndWait();
    }

    @Override
    public void displayLeaderboard(Stage primaryStage, String genre) throws SQLException {
        // Fetch leaderboard data from the database
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        leaderboardData = databaseHandler.getLeaderboardData(genre);

        // Create a popup window
        Stage leaderboardStage = new Stage();
        leaderboardStage.initModality(Modality.APPLICATION_MODAL);
        leaderboardStage.initOwner(primaryStage);
        leaderboardStage.setTitle("Leaderboard");

        // Create UI components for leaderboard
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        VBox leaderboardBox = new VBox(10);
        leaderboardBox.setAlignment(Pos.TOP_CENTER);
        leaderboardBox.setPadding(new Insets(20));
        leaderboardBox.setStyle("-fx-background-color: #212121; -fx-border-color: #1DB954; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Add leaderboard data to layout
        if (leaderboardData != null && !leaderboardData.isEmpty()) {
            int rank = 1;
            for (String data : leaderboardData) {
                String[] parts = data.split(" - Score: ");
                Label rankLabel = new Label(String.format("%-5s", rank + "."));
                rankLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                Label nameLabel = new Label(String.format("%-20s", parts[0]));
                nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                Label scoreLabel = new Label(parts[1]);
                scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                HBox entry = new HBox(20);
                entry.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
                leaderboardBox.getChildren().add(entry);
                rank++;
            }
        } else {
            Label noDataLabel = new Label("Leaderboard is empty.");
            noDataLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            leaderboardBox.getChildren().add(noDataLabel);
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 16px; -fx-background-color: #1DB954; -fx-text-fill: white;");
        closeButton.setOnAction(event -> leaderboardStage.close());

        // Add components to layout
        layout.getChildren().addAll(leaderboardBox, closeButton);

        // Create scene for leaderboard
        Scene leaderboardScene = new Scene(layout, 600, 400);
        leaderboardScene.getStylesheets().add("style.css");
        leaderboardStage.setScene(leaderboardScene);

        // Show the leaderboard popup window
        leaderboardStage.show();
    }
}
