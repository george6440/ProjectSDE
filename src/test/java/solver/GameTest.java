package solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import solver.Game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import solver.Game;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    @InjectMocks
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
    }

    @Test
    void testInitialBoardSetup() {
        // Verify the initial state of the board
        char[][] board = game.getBoard();
        assertEquals('X', board[0][0]);
        assertEquals(' ', board[1][0]);
    }

    @Test
    void testResetGame() {
        game.resetGame();
        char[][] board = game.getBoard();
        assertEquals('X', board[0][0]);
        assertEquals(' ', board[1][0]);
        assertEquals(0, game.getMoves());
    }

    @Test
    void testIsLegalToMoveFrom_AllPositions() {
        for (int row = -1; row <= 2; row++) {
            for (int col = -1; col <= 10; col++) {
                int[] position = {row, col};
                boolean expected = true;

                if (row >= 0 && row < 2 && col >= 0 && col < 10 && game.getBoard()[row][col] != ' ') {
                    assertEquals(expected, game.isLegalToMoveFrom(position),
                            "Unexpected result at position (" + row + ", " + col + ")");
                }

            }
        }
    }

    @Test
    void testIsLegalMove() {
        // Test moves from all positions to all valid adjacent positions
        for (int fromRow = 0; fromRow < 2; fromRow++) {
            for (int fromCol = 0; fromCol < 10; fromCol++) {
                int[] from = {fromRow, fromCol};
                boolean canMoveFrom = game.isLegalToMoveFrom(from);

                // Adjacent positions: up, down, left, right
                int[][] adjacents = {
                        {fromRow - 1, fromCol}, // up
                        {fromRow + 1, fromCol}, // down
                        {fromRow, fromCol - 1}, // left
                        {fromRow, fromCol + 1}  // right
                };

                for (int[] to : adjacents) {
                    boolean expected;

                    if (!canMoveFrom) {
                        expected = false; // Cannot move from an invalid 'from' position
                    } else if (to[0] < 0 || to[0] >= 2 || to[1] < 0 || to[1] >= 10) {
                        expected = false; // Out-of-bounds 'to' position
                    } else if (game.isBlackBox(to[0], to[1])) {
                        expected = false; // 'To' position is a black box
                    } else {
                        expected = game.getBoard()[to[0]][to[1]] == ' '; // Only legal if 'to' is empty
                    }

                    assertEquals(expected, game.isLegalMove(from, to),
                            "Failed at from: (" + fromRow + "," + fromCol +
                                    ") to (" + to[0] + "," + to[1] + ")");
                }
            }
        }
    }


    @Test
    void testMakeMoveValid() {
        int[] from = {1, 1}; // Movable tile '2'
        int[] to = {1, 0};   // Empty space

        game.makeMove(from, to);

        char[][] board = game.getBoard();
        assertEquals('2', board[1][0]);
        assertEquals(' ', board[1][1]);
        assertEquals(1, game.getMoves());
    }

    @Test
    void testMakeMoveInvalid() {
        int[] from = {1, 1}; // Movable tile '2'
        int[] to = {0, 0};   // Black box - illegal move

        game.makeMove(from, to);

        char[][] board = game.getBoard();
        assertEquals('2', board[1][1]);
        assertEquals(0, game.getMoves());
    }

    @Test
    void testIsSolved() {
        // Place board in a solved state
        char[][] solvedBoard = {
                {'X', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                {'1', '2', '3', '4', '5', '6', '7', '8', '9', ' '}
        };
        game.resetGame();
        game.getBoard()[1] = solvedBoard[1]; // Manually adjust

        assertTrue(game.isSolved());
    }

    @Test
    void testIsBlackBox() {
        assertTrue(game.isBlackBox(0, 0)); // Known black box position
        assertFalse(game.isBlackBox(1, 1)); // Not a black box
    }

    @Test
    void testClone() {
        Game clonedGame = game.clone();

        //testing the logic of the clone function
        //System.out.println("Original game board: " + Arrays.deepToString(game.board));
        //System.out.println("Cloned game board: " + Arrays.deepToString(clonedGame.board));

        assertNotSame(game, clonedGame); // Ensure they are different objects
        assertEquals(game, clonedGame);  // Ensure their contents are equal
    }


    // Test scenarios for getLegalMoves
    static Stream<Game> provideGameScenarios() {
        return Stream.of(
                // Scenario 1: Default game state
                new Game(
                        new char[][]{
                                {'X', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                                {' ', '2', '3', '4', '5', '6', '7', '8', '9', '1'}
                        },
                        1, 0, 0
                ),
                // Scenario 2: Solved board state
                new Game(
                        new char[][]{
                                {'X', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                                {'1', '2', '3', '4', '5', '6', '7', '8', '9', ' '}
                        },
                        1, 9, 0
                ),
                // Scenario 3: Empty board state (no legal moves)
                new Game(
                        new char[][]{
                                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                        },
                        0, 0, 0
                )
        );
    }

    @Test
    void testMoveMethod() {
        assertTrue(game.move(1, 1, 1, 0)); // Valid move
        assertFalse(game.move(1, 1, 0, 0)); // Invalid move (black box)
    }

    @ParameterizedTest
    @MethodSource("provideGameScenarios")
    void testGetLegalMoves(Game testGame) {
        Set<int[][]> legalMoves = testGame.getLegalMoves();
        // Ensure legal moves are present (default/specific states)
        if (!testGame.isSolved() && testGame.getMoves() > 0) {
            assertFalse(legalMoves.isEmpty(), "Legal moves should exist.");
        }


    }
}
