package ui;

import javafx.stage.Stage;

import java.sql.SQLException;

public interface LeaderboardDisplay {
    void displayLeaderboard(Stage primaryStage, String genre) throws SQLException;
}
