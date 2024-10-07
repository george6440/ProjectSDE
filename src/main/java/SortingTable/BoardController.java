package SortingTable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import solver.Game;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The {@code BoardController} class is the controller for the UI. It handles the game logic and user interactions.
 */
public class BoardController {
    @FXML
    private GridPane board;

    @FXML
    private Label movesLabel;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label startTimeLabel;

    private Game game;
    private Button selectedButton;
    private int[] selectedPosition;

    private String playerName;
    private LocalDateTime startTime;
    private List<Scores> highScores;

    private static final String SCORES_FILE = "highscores.json";
    private static final Logger logger = LogManager.getLogger(BoardController.class);

    private ObjectMapper objectMapper;

    public void initialize() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        loadHighScores();
        promptPlayerName();
        startNewGame();
    }

    private void promptPlayerName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Player Name");
        dialog.setHeaderText("Welcome to the Sorting Game");
        dialog.setContentText("Please enter your name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> playerName = name);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        playerNameLabel.setText("Player: " + playerName);
        logger.info("Player name set to {}", playerName);
    }

    private void startNewGame() {
        game = new Game();
        startTime = LocalDateTime.now();
        startTimeLabel.setText("Start Time: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        updateBoard();
    }

    private void selectTile(int row, int col, Button button) {
        if (selectedButton != null) {
            selectedButton.setStyle("");
        }

        selectedButton = button;
        selectedPosition = new int[]{row, col};
        button.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void moveTile(int targetRow, int targetCol) {
        if (selectedPosition != null) {
            int[] from = selectedPosition;
            int[] to = new int[]{targetRow, targetCol};

            if (game.isLegalMove(from, to)) {
                game.makeMove(from, to);
                selectedButton.setStyle("");
                updateBoard();
                selectedButton = null;
                selectedPosition = null;

                if (game.isSolved()) {
                    showGameSolvedMessage();
                }
                logger.info("Player clicked on {} {} at {} {}", from[0], from[1], targetRow, targetCol);
            }
        }
    }

    private void updateBoard() {
        char[][] boardArray = game.getBoard();
        board.getChildren().clear();
        for (int row = 0; row < boardArray.length; row++) {
            for (int col = 0; col < boardArray[row].length; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);

                if (game.isBlackBox(row, col)) {
                    button.setStyle("-fx-background-color: black;");
                } else {
                    char value = boardArray[row][col];
                    button.setText(value == ' ' ? "" : String.valueOf(value));
                    final int r = row;
                    final int c = col;

                    if (value != ' ') {
                        button.setOnAction(e -> selectTile(r, c, button));
                    } else {
                        button.setOnAction(e -> moveTile(r, c));
                    }
                }

                board.add(button, col, row);
            }
        }
        movesLabel.setText("Moves: " + game.getMoves());
    }

    @FXML
    public void resetGame() {
        startNewGame();
        logger.info("Game reset");
    }

    @FXML
    public void showHighScores() {
        List<Scores> topScores = highScores.stream()
                .sorted(Comparator.comparingInt(Scores::getSteps))
                .limit(10)
                .toList();

        StringBuilder scoreText = new StringBuilder();
        int rank = 1;
        for (Scores score : topScores) {
            scoreText.append(rank).append(". ").append(score.getName()).append(" - ").append(score.getSteps()).append(" moves\n");
            rank++;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Top 10 High Scores");
        alert.setContentText(scoreText.toString());
        alert.showAndWait();
        logger.info("High Scores updated.");
    }

    private void showGameSolvedMessage() {
        LocalDateTime endTime = LocalDateTime.now();
        Scores score = new Scores(playerName, startTime, endTime, game.getMoves(), true);
        highScores.add(score);
        saveHighScores();
        logger.info("The player that won is {} in {} moves and {} duration", playerName, game.getMoves(), endTime);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Puzzle Solved");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You solved the puzzle.");
        alert.showAndWait();

        startNewGame();
    }

    private void loadHighScores() {
        try {
            File file = new File(SCORES_FILE);
            if (file.exists()) {
                highScores = objectMapper.readValue(file, new TypeReference<List<Scores>>() {});
            } else {
                highScores = new ArrayList<>();
            }
        } catch (IOException e) {
            logger.error("Failed to load high scores", e);
            e.printStackTrace();
            highScores = new ArrayList<>();
        }
    }

    private void saveHighScores() {
        try {
            objectMapper.writeValue(new File(SCORES_FILE), highScores);
            logger.info("Saved high scores to " + SCORES_FILE);
        } catch (IOException e) {
            logger.error("Failed to save high scores", e);
            e.printStackTrace();
        }
    }

    @FXML
    public void showRules() {
        Stage rulesStage = new Stage();
        rulesStage.setTitle("Game Rules");

        Label rulesLabel1 = new Label("Game Rules:\n\n" +
                "1. The puzzle consists of a grid with numbered tiles and one empty space.\n" +
                "2. You can move a tile into the empty space.\n" +
                "3. The goal is to arrange the tiles in ascending order from bottom-left to bottom-right,\n" +
                "   with the empty space in the bottom-right corner.\n" +
                "4. Start state:");
        Label rulesLabel2 = new Label("5. Final state:");
        Label rulesLabel3 = new Label("6. Use as few moves as possible to achieve the final state.\n");

        Image image1 = new Image(getClass().getResourceAsStream("/images/screenshot2.png"));
        Image image2 = new Image(getClass().getResourceAsStream("/images/screenshot1.png"));

        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);

        imageView1.fitWidthProperty().bind(rulesStage.widthProperty().subtract(40));
        imageView1.setPreserveRatio(true);
        imageView2.fitWidthProperty().bind(rulesStage.widthProperty().subtract(40));
        imageView2.setPreserveRatio(true);

        VBox rulesLayout = new VBox(10);
        rulesLayout.getChildren().addAll(rulesLabel1, imageView1, rulesLabel2, imageView2, rulesLabel3);
        rulesLayout.setAlignment(Pos.CENTER);
        rulesLayout.setPadding(new Insets(10));

        Scene rulesScene = new Scene(rulesLayout, 800, 600);
        rulesStage.setScene(rulesScene);
        rulesStage.show();
        logger.info("Rules loaded successfully.");
    }
}
