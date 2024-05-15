package ui;

import data.DatabaseHandler;
import data.Question;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class TriviaGamePage {
    private Scene scene;
    private String genre;
    private String username;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Timeline timer;
    private Label timerLabel;
    private Label scoreLabel;
    private Text questionText;
    private VBox answersBox;
    private DatabaseHandler databaseHandler;

    public TriviaGamePage(Stage primaryStage, String genre, String username) {
        this.genre = genre;
        this.username = username;

        try {
            // Load questions for the selected genre
            databaseHandler = DatabaseHandler.getInstance();
            loadQuestionsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load questions. Please try again later.");
            alert.showAndWait();
            // Return to welcome page
            WelcomePage welcomePage = new WelcomePage(primaryStage);
            primaryStage.setScene(welcomePage.getScene());
            return;
        }

        // Create UI components
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #121212;");

        // Top panel
        VBox topPanel = new VBox(10);
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        timerLabel = new Label("Timer: 15");
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        topPanel.getChildren().addAll(timerLabel, scoreLabel);
        layout.setTop(topPanel);

        // Center panel
        VBox centerPanel = new VBox(20);
        centerPanel.setAlignment(Pos.CENTER);
        questionText = new Text();
        questionText.setFill(Color.WHITE);
        questionText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        answersBox = new VBox(10);
        answersBox.setAlignment(Pos.CENTER);
        centerPanel.getChildren().addAll(new Label("Question:"), questionText, answersBox);
        layout.setCenter(centerPanel);

        // Bottom panel
        Button hintButton = new Button("Hint");
        hintButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white;");
        hintButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        hintButton.setOnAction(event -> showHint());
        layout.setBottom(hintButton);
        BorderPane.setMargin(hintButton, new Insets(20, 0, 20, 0));

        // Create scene
        scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add("style.css");

        // Start the game
        startGame(primaryStage);
    }

    public Scene getScene() {
        return scene;
    }

    private void loadQuestionsFromDatabase() throws SQLException {
        if (genre.equalsIgnoreCase("Wild Card")) {
            questions = databaseHandler.getWildCardQuestions();
        } else {
            questions = databaseHandler.getQuestionsForGenre(genre);
        }
        Collections.shuffle(questions); // Shuffle the questions
    }

    private void startGame(Stage primaryStage) {
        // Load the first question and display it
        loadQuestion(primaryStage);

        // Start the timer
        startTimer(primaryStage);
    }

    private void loadQuestion(Stage primaryStage) {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionText.setText(question.getQuestion());
            List<String> options = question.getOptions();
            answersBox.getChildren().clear();
            for (int i = 0; i < options.size(); i++) {
                Button optionButton = new Button(options.get(i));
                optionButton.setMinWidth(200);
                optionButton.setStyle("-fx-font-size: 16px; -fx-background-color: #1DB954; -fx-text-fill: white;");
                int finalI = i;
                optionButton.setOnAction(event -> handleAnswer(finalI, primaryStage));
                answersBox.getChildren().add(optionButton);
            }
        } else {
            addToLeaderboard(username, score, genre);
            endGame(primaryStage);
        }
    }

    private void startTimer(Stage primaryStage) {
        // Initialize and start the timer for 15 seconds countdown
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int remainingTime = Integer.parseInt(timerLabel.getText().split(": ")[1]);
            if (remainingTime == 0) {
                // Handle timeout (no answer selected)
                handleAnswer(-1, primaryStage);
            } else {
                // Update the timer label with remaining time
                remainingTime--;
                timerLabel.setText("Timer: " + remainingTime);
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void showHint() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        String hint = currentQuestion.getHint();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText(null);
        alert.setContentText(hint);
        alert.showAndWait();
    }

    private void handleAnswer(int selectedOptionIndex, Stage primaryStage) {
        // Stop the timer
        timer.stop();

        // Get the current question
        Question currentQuestion = questions.get(currentQuestionIndex);

        // Check if the answer is correct
        String selectedAnswer = (selectedOptionIndex != -1) ? currentQuestion.getOptions().get(selectedOptionIndex) : null;
        String correctAnswer = currentQuestion.getCorrectAnswer();
        String message;
        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            // Update score based on remaining time
            int remainingTime = Integer.parseInt(timerLabel.getText().split(": ")[1]);
            int points = calculatePoints(remainingTime);
            score += points;
            scoreLabel.setText("Score: " + score);
            message = "Correct!";
        } else if (selectedOptionIndex == -1) {
            message = "You Failed to Answer the Question!\nThe correct answer is " + correctAnswer;
        } else {
            message = "Incorrect\nThe correct answer is " + correctAnswer;
        }

        // Show the message (Correct or Incorrect)
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Result");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });

        if (currentQuestionIndex < questions.size() - 1) {
            // Move to the next question
            currentQuestionIndex++;
            loadQuestion(primaryStage);

            // Restart the timer
            timerLabel.setText("Timer: 15");
            startTimer(primaryStage);
        } else {
            addToLeaderboard(username, score, genre);
            endGame(primaryStage);
        }
    }

    private int calculatePoints(int remainingTime) {
        // Calculate points based on remaining time
        return remainingTime * 10;
    }

    private void addToLeaderboard(String username, int score, String genre) {
        try {
            databaseHandler.addToLeaderboard(username, score, genre);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add data to the leaderboard. Please try again later.");
            alert.showAndWait();
        }
    }

    private void endGame(Stage primaryStage) {
        // Display final score and leaderboard
        GameEndPage gameEndPage = new GameEndPage(primaryStage, username, score, genre);
        primaryStage.setScene(gameEndPage.getScene());
    }
}
