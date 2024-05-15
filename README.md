# Trivial-Trivia
Trivial Trivia is a Java application that allows users to play a trivia game and compete for high scores. Users can select different genres for the trivia questions and compete with other players for the highest score. The application also includes a leaderboard feature to display the top scores for each genre.
---

## Features:
- User authentication
- Select different genres for trivia questions
- Timer for each question
- Hint option for each question
- Leaderboard to display top scores
- Admin panel to manage trivia questions and leaderboard

## Project Structure:

### 1. Main.java
- **Purpose**: Entry point of the application.
- **Methods**:
   - `public static void main(String[] args)`: Starts the application.

### 2. WelcomePage.java
- **Purpose**: Displays the welcome page with login options.
- **Methods**:
   - `public Scene getScene()`: Returns the JavaFX Scene for the welcome page.
   - `private void startGame(Stage primaryStage, String username)`: Transition to the Genre Selection Page.
   - `private void openAdminPanel(Stage primaryStage)`: Opens the Admin Panel.

### 3. AdminPanel.java
- **Purpose**: Allows the admin to manage trivia questions and view the leaderboard.
- **Methods**:
   - `public Scene getScene()`: Returns the JavaFX Scene for the admin panel.
   - `public void show()`: Shows the admin panel.
   - `private void login(String username, String password)`: Handles admin login.
   - `private void showAdminOptions()`: Displays admin options.
   - `private void showAddCategoryScene()`: Displays the scene for adding a new category.
   - `private void showDeleteCategoryScene()`: Displays the scene for deleting a category.
   - `private void showAddQuestionScene(String category)`: Displays the scene for adding a new question.
   - `private void showError(String message)`: Displays an error message.

### 4. GenreSelectionPage.java
- **Purpose**: Allows users to select a genre for the trivia game.
- **Methods**:
   - `public Scene getScene()`: Returns the JavaFX Scene for the genre selection page.

### 5. TriviaGamePage.java
- **Purpose**: Displays trivia questions and handles user answers.
- **Methods**:
   - `public Scene getScene()`: Returns the JavaFX Scene for the trivia game page.
   - `private void loadQuestionsFromDatabase() throws SQLException`: Loads trivia questions from the database.
   - `private void startGame(Stage primaryStage)`: Starts the trivia game.
   - `private void loadQuestion(Stage primaryStage)`: Loads the next trivia question.
   - `private void startTimer(Stage primaryStage)`: Starts the timer for each question.
   - `private void showHint()`: Displays a hint for the current question.
   - `private void handleAnswer(int selectedOptionIndex, Stage primaryStage)`: Handles user's answer to the current question.
   - `private int calculatePoints(int remainingTime)`: Calculates points based on the remaining time for each question.
   - `private void addToLeaderboard(String username, int score, String genre)`: Adds user's score to the leaderboard.
   - `private void endGame(Stage primaryStage)`: Ends the trivia game and displays the final score.

### 6. GameEndPage.java
- **Purpose**: Displays the final score and provides options to view the leaderboard or go back to the main menu.
- **Methods**:
   - `public Scene getScene()`: Returns the JavaFX Scene for the game end page.
   - `private void displayLeaderboard(Stage primaryStage)`: Displays the leaderboard.

### 7. LeaderboardPage.java
- **Purpose**: Displays the leaderboard with top scores.
- **Methods**:
   - `public void display()`: Displays the leaderboard as a popup window.
   - `public void displayLeaderboard(Stage primaryStage, String genre) throws SQLException`: Displays the leaderboard for a specific genre.

### 8. LeaderboardDisplay.java
- **Purpose**: Interface for displaying the leaderboard.
- **Methods**:
   - `void displayLeaderboard(Stage primaryStage, String genre) throws SQLException`: Displays the leaderboard.

### 9. Question.java
- **Purpose**: Represents a trivia question.
- **Methods**:
   - `public String getQuestion()`: Returns the question.
   - `public List<String> getOptions()`: Returns the options for the question.
   - `public String getHint()`: Returns a hint for the question.
   - `public String getCorrectAnswer()`: Returns the correct answer for the question.

### 10. DatabaseHandler.java
- **Purpose**: Handles database operations such as user authentication, fetching trivia questions, and leaderboard data.
- **Methods**:
   - `public static DatabaseHandler getInstance()`: Returns the singleton instance of the `DatabaseHandler` class.
   - `public List<String> getCategories() throws SQLException`: Retrieves available categories/genres for trivia questions.
   - `public List<Question> getQuestionsForGenre(String genre) throws SQLException`: Retrieves trivia questions for a specific genre.
   - `public List<Question> getWildCardQuestions() throws SQLException`: Retrieves wildcard trivia questions.
   - `public void addToLeaderboard(String username, int score, String genre) throws SQLException`: Adds user's score to the leaderboard.
   - `public List<String> getLeaderboardData(String genre) throws SQLException`: Retrieves leaderboard data for a specific genre.
   - `public boolean addQuestion(String question, String option1, String option2, String option3, String option4, String hint, String correctAnswer, String category) throws SQLException`: Adds a new question to the database.
   - `public boolean categoryExists(String category) throws SQLException`: Checks if a category exists in the database.
   - `public void deleteCategory(String category) throws SQLException`: Deletes a category and its associated questions from the database.

## Game Logic:

1. **User Authentication**:
   - Users enter the username they choose to play with.

2. **Genre Selection**:
   - Users can select a genre for the trivia questions.

3. **Trivia Game**:
   - The trivia game starts with a timer for each question.
   - Users can select an answer for each question.
   - Points are awarded based on the remaining time and the correctness of the answer.

4. **Leaderboard**:
   - After completing the game, users can view their final score and the leaderboard.

5. **Admin Panel**:
   - The admin can manage trivia questions, including adding and removing questions.
   - The admin can also view the leaderboard.

---
