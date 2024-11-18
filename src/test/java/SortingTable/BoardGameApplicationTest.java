package SortingTable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import solver.Game;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BoardGameApplicationTest extends ApplicationTest {

    private BoardGameApplication app;


    @InjectMocks
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
    }

    @Override
    public void start(Stage PS) {
        app = new BoardGameApplication();
        app.start(PS);
    }

    @Test
    public void testApplicationStartsWithoutErrors() {
        // Check that the application instance is not null
        assertNotNull(app, "The application should have been initialized");

        // Get the primary stage using TestFX's getStage() method
        Stage primaryStage = BoardGameApplication.getPrimaryStage();
        assertNotNull(primaryStage, "Primary stage should not be null");

        // Check that the primary stage is showing
        assertTrue(primaryStage.isShowing(), "Primary stage should be showing");

        // Check that the scene was correctly set
        Scene scene = primaryStage.getScene();
        assertNotNull(scene, "The primary stage should have a scene set");

        // Check that the scene's root is set correctly
        assertTrue(scene.getRoot() instanceof Pane, "The root of the scene should be of type Pane or a subclass");
    }


    @Test
    public void testFXMLResourceLoading() {
        // This test ensures that the FXML file loads without IOException
        try {
            Pane root = (Pane) new FXMLLoader(getClass().getResource("/board.fxml")).load();
            assertNotNull(root, "Root pane from FXML should not be null");
        } catch (IOException e) {
            fail("FXML file should load without an IOException");
        }
    }

    @Test
    public void testShowHighScoresButton() {
        // Click the "Show High Scores" button
        clickOn("#showHighScoresButton");

        // Wait for the high scores label to appear
        waitForFxEvents();

        // Check that the high scores label is visible (assuming you have it in the FXML)
        assertTrue(lookup("#highScoresLabel").query().isVisible(), "High Scores label should be visible after clicking the button");
    }

    @Test
    public void testResetGameButton() {
        // Click the "Reset Game" button
        clickOn("#resetButton");

        // Wait for the board to reset
        waitForFxEvents();

        // Assert that the board is present after reset
        assertNotNull(lookup("#board").query(), "The board should be present after reset");
    }

    @Test
    public void testShowRulesButton() {
        // Click the "Show Rules" button
        clickOn("#showRulesButton");

        // Wait for the rules label to appear
        waitForFxEvents();

        // Assert that the rules label is visible
        assertTrue(lookup("#rulesLabel").query().isVisible(), "Rules label should be visible after clicking the button");
    }
}
