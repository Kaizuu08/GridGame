package Fallin.gui;

import Fallin.engine.Cell;
import Fallin.engine.GameEngine;
import Fallin.engine.ScoreRecord;
import Fallin.engine.TopScores;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;

public class Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox topScoresBox;
    @FXML
    private TextField difficultyField;
    @FXML
    private Button runButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    private Label timeLimitLabel;
    @FXML
    private Label stepsLabel;
    @FXML
    private Label treasuresLabel;
    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private ProgressBar lifeStatusBar;

    private GameEngine engine;

    @FXML
    public void initialize() {
        runButton.setOnAction(event -> startGame());
        helpButton.setOnAction(event -> showHelp());
        upButton.setOnAction(event -> movePlayer("up"));
        downButton.setOnAction(event -> movePlayer("down"));
        leftButton.setOnAction(event -> movePlayer("left"));
        rightButton.setOnAction(event -> movePlayer("right"));
        saveButton.setOnAction(event -> saveGame());
        loadButton.setOnAction(event -> loadGame());

        setMovementButtonsDisabled(true); // Disable movement buttons initially
    }

    private void startGame() {
        int difficulty = 5; // Default difficulty
        try {
            difficulty = Integer.parseInt(difficultyField.getText());
            if (difficulty < 0 || difficulty > 10) {
                difficulty = 5;
            }
        } catch (NumberFormatException ignored) {
        }

        engine = new GameEngine(10, difficulty);
        updateGui();
        updateTopScores();
        updateLabels();
        updateLifeStatusBar(); // Update life status bar at the start

        runButton.setVisible(false); // Hide the RUN button
        setMovementButtonsDisabled(false); // Enable movement buttons
    }

    private void saveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Game Files", "*.game")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(engine.getGameState());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Game Files", "*.game")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                GameEngine.GameState gameState = (GameEngine.GameState) in.readObject();
                engine = new GameEngine(gameState);
                updateGui();
                updateTopScores();
                updateLabels();
                updateLifeStatusBar(); // Update life status bar after loading
                setMovementButtonsDisabled(false); // Enable movement buttons
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void movePlayer(String direction) {
        engine.movePlayer(direction);
        updateGui();
        updateLabels();
        updateLifeStatusBar();
        if (engine.isGameEnded()) {
            displayScore();
            updateTopScores();
            runButton.setVisible(true);
            setMovementButtonsDisabled(true);
        }
    }

    private void setMovementButtonsDisabled(boolean disabled) {
        upButton.setDisable(disabled);
        downButton.setDisable(disabled);
        leftButton.setDisable(disabled);
        rightButton.setDisable(disabled);
    }

    private void updateGui() {
        // Clear old GUI grid pane
        gridPane.getChildren().clear();

        // Loop through map board and add each cell into grid pane
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                Cell cell = engine.getMap()[i][j];
                StackPane cellPane = new StackPane();
                cellPane.setPrefSize(50, 50);

                // Set background image for each cell
                Image backgroundImage = new Image(getClass().getResourceAsStream("/images/Platform.png"));
                BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT);
                cellPane.setBackground(new Background(bgImage));

                // Add coordinates text to each cell
                Text text = new Text(i + "," + j);
                text.setFill(Color.WHITE);
                cellPane.getChildren().add(text);

                if (cell.getImageName() != null) {
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/" + cell.getImageName())));
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    cellPane.getChildren().add(imageView);
                }

                // Set the entrance and exit cell colors
                if (i == engine.getSize() - 1 && j == 0) {
                    cellPane.setStyle("-fx-background-color: #7baaa4"); // Entrance
                } else if (i == 0 && j == engine.getSize() - 1) {
                    cellPane.setStyle("-fx-background-color: #FF0000"); // Exit
                }

                gridPane.add(cellPane, j, i);
            }
        }
        gridPane.setGridLinesVisible(true);
    }

    private void updateTopScores() {
        topScoresBox.getChildren().clear();
        List<ScoreRecord> topScores = TopScores.getTopScores();
        for (int i = 0; i < topScores.size(); i++) {
            Label label = new Label((i + 1) + ". " + topScores.get(i).toString());
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
            topScoresBox.getChildren().add(label);
        }
    }

    private void updateLabels() {
        timeLimitLabel.setText("Time Limit: " + (100 - engine.getPlayer().getSteps()));
        stepsLabel.setText("Steps: " + engine.getPlayer().getSteps());
        treasuresLabel.setText("Treasures: " + engine.getPlayer().getTreasuresCollected());
    }

    private void updateLifeStatusBar() {
        double lives = engine.getPlayer().getLives();
        double maxLives = engine.getPlayer().getMaxLives();
        lifeStatusBar.setProgress(lives / maxLives);
    }

    private void displayScore() {
        int score = engine.getScore();
        System.out.println("Displaying score: " + score); // Debug statement
        List<ScoreRecord> topScores = TopScores.getTopScores();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");

        boolean isHighScore = score > 0 && (topScores.isEmpty() || topScores.size() < 5 || score > topScores.get(topScores.size() - 1).getScore());

        if (isHighScore) {
            alert.setHeaderText("Congratulations! New high score!");
        } else {
            alert.setHeaderText("Game Over. Better luck next time!");
        }

        alert.setContentText("Your score: " + score);
        alert.showAndWait();
    }



    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Game Instructions");
        alert.setContentText("Use the arrow buttons to move the player.\n" +
                "Collect diamonds and avoid bombs.\n" +
                "Increase points by collecting diamonds.\n" +
                "Avoid mutants and Bombs as they decrease your health.\n" +
                "Health can be recovered by picking up mushrooms.\n" +
                "Reach the exit to win the game.\n" +
                "Good luck!");
        alert.showAndWait();
    }
}
