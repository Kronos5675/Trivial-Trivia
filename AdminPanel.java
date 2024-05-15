package ui;

import data.DatabaseHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import java.sql.SQLException;

public class AdminPanel {
    private Stage primaryStage;
    private Scene loginScene;
    private BorderPane adminOptionsLayout;

    public AdminPanel(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create UI components for login
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(Color.rgb(25, 25, 25), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label
        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Username input field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-font-size: 16px;");

        // Password input field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-font-size: 16px;");
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                login(usernameField.getText().trim(), passwordField.getText().trim());
            }
        });

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        loginButton.setOnAction(event -> {
            login(usernameField.getText().trim(), passwordField.getText().trim());
        });

        // Add components to layout
        layout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);

        // Create login scene
        loginScene = new Scene(layout, 500, 400);
        loginScene.setFill(Color.rgb(25, 25, 25));
    }

    public void show() {
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void login(String username, String password) {
        // Check username and password
        if (username.equals("admin") && password.equals("admin123")) {
            // Show admin options
            showAdminOptions();
        } else {
            // Show error message
            showError("Invalid username or password!");
        }
    }

    private void showAdminOptions() {
        adminOptionsLayout = new BorderPane();
        adminOptionsLayout.setPadding(new Insets(20));
        adminOptionsLayout.setBackground(new Background(new BackgroundFill(Color.rgb(25, 25, 25), CornerRadii.EMPTY, Insets.EMPTY)));

        // Welcome admin message
        Label welcomeLabel = new Label("Welcome Admin!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #FF0000; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> {
            // Go back to the main Welcome Page
            primaryStage.setScene(new WelcomePage(primaryStage).getScene());
        });

        HBox topPane = new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(10));
        topPane.setSpacing(20);

        // Adding the welcome message to the left
        HBox.setHgrow(welcomeLabel, Priority.ALWAYS);
        topPane.getChildren().add(welcomeLabel);

        // Adding the logout button to the right
        HBox.setHgrow(logoutButton, Priority.ALWAYS);
        topPane.getChildren().add(logoutButton);

        adminOptionsLayout.setTop(topPane);

        // Delete categories button
        Button deleteCategoriesButton = new Button("Delete Categories");
        deleteCategoriesButton.setStyle("-fx-font-size: 20px; -fx-padding: 20 50; -fx-background-color: #1DB954; -fx-text-fill: white;");
        deleteCategoriesButton.setOnAction(event -> {
            showDeleteCategoryScene();
        });

        // Add categories button
        Button addCategoriesButton = new Button("Add Categories");
        addCategoriesButton.setStyle("-fx-font-size: 20px; -fx-padding: 20 50; -fx-background-color: #1DB954; -fx-text-fill: white;");
        addCategoriesButton.setOnAction(event -> {
            showAddCategoryScene();
        });

        VBox centerPane = new VBox(50);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.getChildren().addAll(deleteCategoriesButton, addCategoriesButton);
        adminOptionsLayout.setCenter(centerPane);

        // Create scene
        Scene adminOptionsScene = new Scene(adminOptionsLayout, 600, 500);
        adminOptionsScene.setFill(Color.rgb(25, 25, 25));

        // Set the scene
        primaryStage.setScene(adminOptionsScene);
    }




    private void showAddCategoryScene() {
        // Create the layout for adding category
        VBox addCategoryLayout = new VBox(20);
        addCategoryLayout.setAlignment(Pos.CENTER);
        addCategoryLayout.setPadding(new Insets(20));
        addCategoryLayout.setBackground(new Background(new BackgroundFill(Color.rgb(25, 25, 25), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label
        Label titleLabel = new Label("Add Category");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Category input field
        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter category");
        categoryField.setMaxWidth(300);
        categoryField.setStyle("-fx-font-size: 16px;");

        // Check if category exists
        Button checkCategoryButton = new Button("Check Category");
        checkCategoryButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        checkCategoryButton.setOnAction(event -> {
            if (categoryField.getText().isEmpty()) {
                showError("Category field is empty!");
            } else {
                try {
                    if (DatabaseHandler.getInstance().categoryExists(categoryField.getText().trim())) {
                        // Category exists, show error message
                        showError("Category already exists!");
                    } else {
                        // Category doesn't exist, show add question scene
                        showAddQuestionScene(categoryField.getText().trim());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Show error message
                    showError("Database error occurred!");
                }
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        backButton.setOnAction(event -> {
            showAdminOptions();
        });

        // Add components to layout
        addCategoryLayout.getChildren().addAll(titleLabel, categoryField, checkCategoryButton, backButton);

        // Create scene
        Scene addCategoryScene = new Scene(addCategoryLayout, 500, 400);
        addCategoryScene.setFill(Color.rgb(25, 25, 25));

        // Set the scene
        primaryStage.setScene(addCategoryScene);
    }

    private void showDeleteCategoryScene() {
        // Create the layout for deleting category
        VBox deleteCategoryLayout = new VBox(20);
        deleteCategoryLayout.setAlignment(Pos.CENTER);
        deleteCategoryLayout.setPadding(new Insets(20));
        deleteCategoryLayout.setBackground(new Background(new BackgroundFill(Color.rgb(25, 25, 25), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label
        Label titleLabel = new Label("Delete Category");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Category input field
        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter category");
        categoryField.setMaxWidth(300);
        categoryField.setStyle("-fx-font-size: 16px;");

        // Check if category exists and delete it
        Button deleteCategoryButton = new Button("Delete Category");
        deleteCategoryButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        deleteCategoryButton.setOnAction(event -> {
            if (categoryField.getText().isEmpty()) {
                showError("Category field is empty!");
            } else {
                try {
                    if (DatabaseHandler.getInstance().categoryExists(categoryField.getText().trim())) {
                        // Category exists, delete it
                        DatabaseHandler.getInstance().deleteCategory(categoryField.getText().trim());
                        // Show success message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Category deleted successfully!");
                        alert.showAndWait();
                    } else {
                        // Category doesn't exist, show error message
                        showError("Category does not exist!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Show error message
                    showError("Database error occurred!");
                }
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        backButton.setOnAction(event -> {
            showAdminOptions();
        });

        // Add components to layout
        deleteCategoryLayout.getChildren().addAll(titleLabel, categoryField, deleteCategoryButton, backButton);

        // Create scene
        Scene deleteCategoryScene = new Scene(deleteCategoryLayout, 500, 400);
        deleteCategoryScene.setFill(Color.rgb(25, 25, 25));

        // Set the scene
        primaryStage.setScene(deleteCategoryScene);
    }

    private void showAddQuestionScene(String category) {
        // Create the layout for adding question
        VBox addQuestionLayout = new VBox(20);
        addQuestionLayout.setAlignment(Pos.CENTER);
        addQuestionLayout.setPadding(new Insets(20));
        addQuestionLayout.setBackground(new Background(new BackgroundFill(Color.rgb(25, 25, 25), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title label
        Label titleLabel = new Label("Add Question");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Question input field
        TextField questionField = new TextField();
        questionField.setPromptText("Enter question");
        questionField.setMaxWidth(400);
        questionField.setStyle("-fx-font-size: 16px;");

        // Option input fields
        TextField option1Field = new TextField();
        option1Field.setPromptText("Option 1");
        option1Field.setMaxWidth(300);
        option1Field.setStyle("-fx-font-size: 16px;");

        TextField option2Field = new TextField();
        option2Field.setPromptText("Option 2");
        option2Field.setMaxWidth(300);
        option2Field.setStyle("-fx-font-size: 16px;");

        TextField option3Field = new TextField();
        option3Field.setPromptText("Option 3");
        option3Field.setMaxWidth(300);
        option3Field.setStyle("-fx-font-size: 16px;");

        TextField option4Field = new TextField();
        option4Field.setPromptText("Option 4");
        option4Field.setMaxWidth(300);
        option4Field.setStyle("-fx-font-size: 16px;");

        // Hint input field
        TextField hintField = new TextField();
        hintField.setPromptText("Hint");
        hintField.setMaxWidth(400);
        hintField.setStyle("-fx-font-size: 16px;");

        // Correct answer input field
        TextField correctAnswerField = new TextField();
        correctAnswerField.setPromptText("Correct Answer");
        correctAnswerField.setMaxWidth(400);
        correctAnswerField.setStyle("-fx-font-size: 16px;");

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        backButton.setOnAction(event -> {
            showAdminOptions();
        });

        // Add button
        Button addButton = new Button("Add");
        addButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #1DB954; -fx-text-fill: white;");
        addButton.setOnAction(event -> {
            try {
                DatabaseHandler.getInstance().addQuestion(questionField.getText().trim(),
                        option1Field.getText().trim(),
                        option2Field.getText().trim(),
                        option3Field.getText().trim(),
                        option4Field.getText().trim(),
                        hintField.getText().trim(),
                        correctAnswerField.getText().trim(),
                        category);
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Question added successfully!");
                alert.showAndWait();
                // Go back to the admin options
                showAdminOptions();
            } catch (SQLException e) {
                e.printStackTrace();
                // Show error message
                showError("Database error occurred!");
            }
        });

        // Add components to layout
        addQuestionLayout.getChildren().addAll(titleLabel, questionField, option1Field, option2Field,
                option3Field, option4Field, hintField, correctAnswerField, addButton, backButton);

        // Create scene
        Scene addQuestionScene = new Scene(addQuestionLayout, 600, 600);
        addQuestionScene.setFill(Color.rgb(25, 25, 25));

        // Set the scene
        primaryStage.setScene(addQuestionScene);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
