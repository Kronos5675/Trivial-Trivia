package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/quiz_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kronos";

    private static DatabaseHandler instance;

    private DatabaseHandler() {
        // Private constructor to prevent instantiation from outside
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public List<Question> getQuestionsForGenre(String genre) throws SQLException {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT Question, Option1, Option2, Option3, Option4, Hint, CorrectAnswer " +
                    "FROM Questions WHERE Category = ? ORDER BY RAND() LIMIT 20";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, genre);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String question = resultSet.getString("Question");
                    List<String> options = new ArrayList<>();
                    options.add(resultSet.getString("Option1"));
                    options.add(resultSet.getString("Option2"));
                    options.add(resultSet.getString("Option3"));
                    options.add(resultSet.getString("Option4"));
                    String hint = resultSet.getString("Hint");
                    String correctAnswer = resultSet.getString("CorrectAnswer");
                    questions.add(new Question(question, options, hint, correctAnswer));
                }
            }
        }
        return questions;
    }

    public List<Question> getWildCardQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT Question, Option1, Option2, Option3, Option4, Hint, CorrectAnswer " +
                    "FROM Questions ORDER BY RAND() LIMIT 20";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String question = resultSet.getString("Question");
                    List<String> options = new ArrayList<>();
                    options.add(resultSet.getString("Option1"));
                    options.add(resultSet.getString("Option2"));
                    options.add(resultSet.getString("Option3"));
                    options.add(resultSet.getString("Option4"));
                    String hint = resultSet.getString("Hint");
                    String correctAnswer = resultSet.getString("CorrectAnswer");
                    questions.add(new Question(question, options, hint, correctAnswer));
                }
            }
        }
        return questions;
    }

    public void addToLeaderboard(String username, int score, String genre) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            // Check if the username already exists in the leaderboard
            String selectQuery = "SELECT * FROM leaderboard WHERE Username = ? AND Genre = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, username);
                selectStatement.setString(2, genre);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    // Username already exists in the leaderboard
                    int currentScore = resultSet.getInt("Score");
                    if (score > currentScore) {
                        // Update the score if the new score is higher
                        String updateQuery = "UPDATE leaderboard SET Score = ? WHERE Username = ? AND Genre = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, score);
                            updateStatement.setString(2, username);
                            updateStatement.setString(3, genre);
                            updateStatement.executeUpdate();
                        }
                    }
                } else {
                    // Username doesn't exist in the leaderboard, add it
                    String insertQuery = "INSERT INTO leaderboard VALUES (?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, username);
                        insertStatement.setInt(2, score);
                        insertStatement.setString(3, genre);
                        insertStatement.executeUpdate();
                    }
                }
            }
        }
    }

    public List<String> getLeaderboardData(String genre) throws SQLException {
        List<String> leaderboardData = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT Username, Score FROM leaderboard WHERE Genre = ? ORDER BY Score DESC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, genre);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String username = resultSet.getString("Username");
                    int score = resultSet.getInt("Score");
                    leaderboardData.add(username + " - Score: " + score);
                }
            }
        }

        return leaderboardData;
    }

    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT DISTINCT Category FROM Questions";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    categories.add(resultSet.getString("Category"));
                }
            }
        }
        return categories;
    }

    public boolean categoryExists(String category) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM Questions WHERE Category = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, category);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            }
        }
    }

    public void addQuestion(String question, String option1, String option2, String option3, String option4,
                            String hint, String correctAnswer, String category) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO Questions (Question, Option1, Option2, Option3, Option4, Hint, CorrectAnswer, Category) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, question);
                statement.setString(2, option1);
                statement.setString(3, option2);
                statement.setString(4, option3);
                statement.setString(5, option4);
                statement.setString(6, hint);
                statement.setString(7, correctAnswer);
                statement.setString(8, category);
                statement.executeUpdate();
            }
        }
    }

    public void deleteCategory(String category) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM Questions WHERE Category = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, category);
                statement.executeUpdate();
            }
        }
    }
}
