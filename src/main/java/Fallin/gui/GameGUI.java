package Fallin.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

/**
 * GUI for the Maze Runner Game.
 * NOTE: Do NOT run this class directly in IntelliJ - run 'RunGame' instead.
 */
public class GameGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("/Fallin/gui/game_gui.fxml");
        URL cssUrl = getClass().getResource("/Fallin/gui/style.css");

        System.out.println("FXML URL: " + fxmlUrl);
        System.out.println("CSS URL: " + cssUrl);

        if (fxmlUrl == null || cssUrl == null) {
            throw new RuntimeException("Resource files not found!");
        }

        BorderPane root = FXMLLoader.load(fxmlUrl);

        // Load the style.css background
        Scene scene = new Scene(root, 1080,800);
        scene.getStylesheets().add(cssUrl.toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fallin Game");
        primaryStage.show();
    }

    /** In IntelliJ, do NOT run this method.  Run 'RunGame.main()' instead. */
    public static void main(String[] args) {
        launch(args);
    }
}
