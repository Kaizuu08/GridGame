package Fallin.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Manages the state of the game and logic
 */
public class GameEngine implements Serializable {

    private final Cell[][] map; // The map object, represented as a 2D array
    private final Player player; // The player object
    private int playerX, playerY; // Player position on map
    private final int size; // Size of the game board
    private final int difficulty; // Difficulty level
    private boolean gameEnded; // Flag to check if the game has ended
    private int score; // Score of the game
    private final int timeLimit; // Time limit for the game
    private final List<Mutant> mutants; // List to store mutants
    private List<ScoreRecord> topScores; // List to store top scores

    // Add GameState inner class to represent the game state
    public static class GameState implements Serializable {
        private final Cell[][] map;
        private final Player player;
        public final int playerX;
        public final int playerY;
        private final int size;
        private final int difficulty;
        public final boolean gameEnded;
        public final int score;
        private final int timeLimit;
        private final List<Mutant> mutants;
        private final List<ScoreRecord> topScores;

        // Constructor for GameState
        public GameState(Cell[][] map, Player player, int playerX, int playerY, int size, int difficulty, boolean gameEnded, int score, int timeLimit, List<Mutant> mutants, List<ScoreRecord> topScores) {
            this.map = map;
            this.player = player;
            this.playerX = playerX;
            this.playerY = playerY;
            this.size = size;
            this.difficulty = difficulty;
            this.gameEnded = gameEnded;
            this.score = score;
            this.timeLimit = timeLimit;
            this.mutants = mutants;
            this.topScores = topScores;
        }
    }

    /**
     * Creates a square game board with a given difficulty level.
     *
     * @param size the width and height.
     * @param difficulty the difficulty level.
     */
    public GameEngine(int size, int difficulty) {
        this.size = size;
        this.difficulty = difficulty;
        this.timeLimit = 100; // Initial time limit
        this.gameEnded = false;
        this.score = 0;
        this.mutants = new ArrayList<>();
        map = new Cell[size][size]; // Initialise the map
        player = new Player(); // Initialise the player
        playerX = size - 1; // Initial position of player (bottom left corner)
        playerY = 0; // Initial position of player (bottom left corner)

        // Initialise each cell without using JavaFX
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Cell();
            }
        }

        // Place game items on the map
        placeGameItems();
        map[playerX][playerY].setPlayerImage(); // Place the player image at the start
    }

    // Constructor to create a GameEngine from a GameState
    public GameEngine(GameState gameState) {
        this.map = gameState.map;
        this.player = gameState.player;
        this.playerX = gameState.playerX;
        this.playerY = gameState.playerY;
        this.size = gameState.size;
        this.difficulty = gameState.difficulty;
        this.gameEnded = gameState.gameEnded;
        this.score = gameState.score;
        this.timeLimit = gameState.timeLimit;
        this.mutants = gameState.mutants;
        TopScores.setTopScores(gameState.topScores);
    }

    // Method to get the current game state
    public GameState getGameState() {
        return new GameState(map, player, playerX, playerY, size, difficulty, gameEnded, score, timeLimit, mutants, TopScores.getTopScores());
    }

    /**
     * The size of the current game.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * The map of the current game.
     *
     * @return the map, which is a 2d array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Places game items randomly on the map.
     */
    public void placeGameItems() {
        Random rand = new Random();

        // Place treasures
        for (int i = 0; i < 8; i++) {
            placeRandomItem(new Treasures(), rand);
        }

        // Place medical units
        for (int i = 0; i < 2; i++) {
            placeRandomItem(new MedicalUnit(), rand);
        }

        // Place traps
        for (int i = 0; i < 5; i++) {
            placeRandomItem(new Trap(), rand);
        }

        // Place mutants based on difficulty
        for (int i = 0; i < difficulty; i++) {
            Mutant mutant = new Mutant();
            placeRandomItem(mutant, rand);
            mutants.add(mutant); // Add mutant to the list
        }
    }

    /**
     * Places a game item at a random location on the map, avoiding the entrance and exit.
     *
     * @param item the game item to place
     * @param rand the Random instance to use for generating random positions
     */
    private void placeRandomItem(GameItem item, Random rand) {
        int x, y;
        do {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        } while ((x == size - 1 && y == 0) || (x == 0 && y == size - 1) || map[x][y].getItem() != null);

        map[x][y].setItem(item);
    }

    /**
     * Moves the player in the specified direction.
     *
     * @param direction the direction to move player ('up', 'down', 'left' or 'right')
     */
    public void movePlayer(String direction) {
        if (gameEnded) {
            System.out.println("Game has already ended.");
            return;
        }

        int newX = playerX;
        int newY = playerY;

        // changes position based on the direction
        switch (direction.toLowerCase()) {
            case "up":
                newX = playerX - 1;
                break;
            case "down":
                newX = playerX + 1;
                break;
            case "left":
                newY = playerY - 1;
                break;
            case "right":
                newY = playerY + 1;
                break;
            default:
                System.out.println("Invalid direction. Use 'up', 'down', 'left' or 'right'.");
                return;
        }

        // Checks if the new position is within map limit
        if (newX >= 0 && newX < getSize() && newY >= 0 && newY < getSize()) {
            map[playerX][playerY].clearPlayerImage(); // Clear the player image from the old position
            player.move();
            map[newX][newY].interact(player);
            playerX = newX;
            playerY = newY;
            map[playerX][playerY].setPlayerImage(); // Set the player image at the new position
            updateScore();
            System.out.println("Player moved to: " + playerX + "," + playerY);
            System.out.println("Score after move: " + score); // Debug statement

            // Check if the player has reached the exit
            if (playerX == 0 && playerY == size - 1) {
                endGame(true);
            }
        } else {
            System.out.println("Movement out of bounds.");
        }

        // Move mutants after player moves
        moveMutants();

        // Check if the game has ended
        if (player.getLife() <= 0 || player.getSteps() >= timeLimit) {
            endGame(false);
        }
    }

    /**
     * Moves each mutant randomly.
     */
    private void moveMutants() {
        Random rand = new Random();
        for (Mutant mutant : mutants) {
            int[] pos = findItemPosition(mutant);
            if (pos == null) continue;

            int mutantX = pos[0];
            int mutantY = pos[1];
            int newX = mutantX;
            int newY = mutantY;

            // Randomly choose a direction
            switch (rand.nextInt(5)) {
                case 0: // Move up
                    newX = mutantX - 1;
                    break;
                case 1: // Move down
                    newX = mutantX + 1;
                    break;
                case 2: // Move left
                    newY = mutantY - 1;
                    break;
                case 3: // Move right
                    newY = mutantY + 1;
                    break;
                case 4: // Stay in place
                    break;
            }

            // Check if the new position is within map limits and not a special cell
            if (newX >= 0 && newX < getSize() && newY >= 0 && newY < getSize() &&
                    !(map[newX][newY].getItem() instanceof Treasures ||
                            map[newX][newY].getItem() instanceof MedicalUnit ||
                            map[newX][newY].getItem() instanceof Trap) &&
                    !(newX == size - 1 && newY == 0) && // Prevent mutant from moving to entrance
                    !(newX == 0 && newY == size - 1)) { // Prevent mutant from moving to exit

                map[mutantX][mutantY].clearItem(); // Clear the mutant from the old position
                map[newX][newY].setItem(mutant); // Move mutant to the new position
            }
        }
    }

    /**
     * Finds the position of a game item on the map.
     *
     * @param item the game item to find
     * @return an array with x and y coordinates of the item, or null if not found
     */
    private int[] findItemPosition(GameItem item) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (map[x][y].getItem() == item) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Updates the score based on player's progress.
     */
    public void updateScore() {
        score = 20 * player.getTreasuresCollected() + (timeLimit - player.getSteps());
        System.out.println("Updated score: " + score); // Debug statement
    }

    /**
     * Ends the game and prints a message.
     *
     * @param reachedExit whether the player reached the exit
     */
    public void endGame(boolean reachedExit) {
        gameEnded = true;
        if (reachedExit) {
            System.out.println("Congratulations! You've reached the exit!");
        } else {
            System.out.println("Game Over. Player has no more life or time.");
            score = -1;
        }
        System.out.println("Your score: " + score);
        if (score > 0) {
            TopScores.addScore(score); // Add score to top scores
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Plays a text-based game.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for difficulty level
        System.out.print("Enter difficulty level (number of mutants, 0-10, default 5): ");
        int difficulty = scanner.hasNextInt() ? scanner.nextInt() : 5;
        if (difficulty < 0 || difficulty > 10) difficulty = 5;

        GameEngine engine = new GameEngine(10, difficulty);
        scanner.nextLine(); // Consume newline

        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());

        while (!engine.isGameEnded()) {
            System.out.print("Enter direction (up, down, left, right) to move player or 'quit' to exit: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                engine.endGame(false);
                break; // Exit loop if user enters 'quit'
            }

            engine.movePlayer(input); // Moves the player based on input

            // Display player progression status
            System.out.printf("Player life: %d, Treasures collected: %d, Steps: %d\n",
                    engine.getPlayer().getLife(), engine.getPlayer().getTreasuresCollected(), engine.getPlayer().getSteps());
        }

        scanner.close();
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
}
