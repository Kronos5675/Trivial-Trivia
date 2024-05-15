import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ui.WelcomePage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize and display the welcome page
        WelcomePage welcomePage = new WelcomePage(primaryStage);
        primaryStage.setScene(welcomePage.getScene());
        primaryStage.setTitle("Trivial Trivia");

        // Get the screen size
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        // Set width and height to screen width and height
        primaryStage.setWidth(screenSize.getWidth());
        primaryStage.setHeight(screenSize.getHeight());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
