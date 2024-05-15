package ui;

import data.DatabaseHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreSelectionPage {
    private Scene scene;
    private String username;
    private Button startPlayingButton;
    private Button selectedGenreButton;

    public GenreSelectionPage(Stage primaryStage, String username) {
        this.username = username;
        String[] selectedGenre = {null};

        // Create UI components
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #121212;");

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 16px;");
        logoutButton.setOnAction(event -> {
            WelcomePage welcomePage = new WelcomePage(primaryStage);
            primaryStage.setScene(welcomePage.getScene());
        });
        VBox.setMargin(logoutButton, new Insets(0, 0, 10, 10));
        logoutButton.setAlignment(Pos.BOTTOM_LEFT);

        // Welcome message
        Text welcomeMessage = new Text("Welcome " + username + "!");
        welcomeMessage.setFill(Color.WHITE);
        welcomeMessage.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        VBox.setMargin(welcomeMessage, new Insets(0, 0, 20, 0));

        // Select Category title
        Text selectCategoryTitle = new Text("Select Category");
        selectCategoryTitle.setFill(Color.WHITE);
        selectCategoryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        VBox.setMargin(selectCategoryTitle, new Insets(0, 0, 20, 0));

        // Grid of buttons for genres
        GridPane genresGrid = new GridPane();
        genresGrid.setAlignment(Pos.CENTER);
        genresGrid.setHgap(10);
        genresGrid.setVgap(10);

        // Fetch categories from the database
        List<String> genres = new ArrayList<>();
        try {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance(); // Get an instance of DatabaseHandler
            genres = databaseHandler.getCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int row = 0;
        int col = 0;
        for (String genre : genres) {
            Button genreButton = new Button(genre);
            genreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
            genreButton.setMaxWidth(Double.MAX_VALUE);
            genreButton.setOnMouseEntered(e -> genreButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 20px;"));
            genreButton.setOnMouseExited(e -> {
                if (!genreButton.isDisabled()) {
                    genreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
                }
            });
            genreButton.setOnAction(event -> {
                if (selectedGenre[0] != null && !selectedGenre[0].equals(genre)) {
                    selectedGenreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
                    selectedGenreButton.setDisable(false);
                }
                selectedGenre[0] = genre;
                genreButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 20px;");
                genreButton.setDisable(true);
                selectedGenreButton = genreButton;
                startPlayingButton.setDisable(false);
            });
            genresGrid.add(genreButton, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }

        // Wild Card button
        Button wildCardButton = new Button("Wild Card");
        wildCardButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
        wildCardButton.setMaxWidth(Double.MAX_VALUE);
        wildCardButton.setOnMouseEntered(e -> wildCardButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 20px;"));
        wildCardButton.setOnMouseExited(e -> {
            if (!wildCardButton.isDisabled()) {
                wildCardButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
            }
        });
        wildCardButton.setOnAction(event -> {
            if (selectedGenre[0] != null) {
                selectedGenreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-border-radius: 5px; -fx-padding: 20px;");
                selectedGenreButton.setDisable(false);
            }
            selectedGenre[0] = "Wild Card";
            wildCardButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 20px;");
            wildCardButton.setDisable(true);
            selectedGenreButton = wildCardButton;
            startPlayingButton.setDisable(false);
        });
        genresGrid.add(wildCardButton, col, row);

        // Start Playing button
        startPlayingButton = new Button("Start");
        startPlayingButton.setDisable(true);
        startPlayingButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 10px 40px;");
        startPlayingButton.setMaxWidth(Double.MAX_VALUE);
        startPlayingButton.setOnMouseEntered(e -> startPlayingButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 10px 40px;"));
        startPlayingButton.setOnMouseExited(e -> startPlayingButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: transparent; -fx-border-radius: 5px; -fx-padding: 10px 40px;"));
        startPlayingButton.setOnAction(event -> {
            if (selectedGenre[0] != null) {
                TriviaGamePage triviaGamePage = new TriviaGamePage(primaryStage, selectedGenre[0], username);
                primaryStage.setScene(triviaGamePage.getScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a category to start playing!");
                alert.showAndWait();
            }
        });

        // Add components to layout
        layout.getChildren().addAll(logoutButton, welcomeMessage, selectCategoryTitle, genresGrid, startPlayingButton);

        // Create scene
        scene = new Scene(layout, 600, 500);
    }

    public Scene getScene() {
        return scene;
    }
}
