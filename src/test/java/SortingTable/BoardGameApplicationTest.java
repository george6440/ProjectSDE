package SortingTable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameApplicationTest extends ApplicationTest {

    private BoardGameApplication app;

    @Override
    public void start(Stage primaryStage) {
        app = new BoardGameApplication();
        app.start(primaryStage);
    }

    @Test
    public void testApplicationStartsWithoutErrors() {
        // Check that the application instance is not null
        assertNotNull(app, "The application should have been initialized");

        // Check that the primary stage is shown with a scene
        Stage primaryStage = (Stage) app.getPrimaryStage();
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
}
