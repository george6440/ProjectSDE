package solver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The {@code Game} class represents a puzzle game where a player needs to arrange numbers on a board.
 * The board contains some fixed black boxes that cannot be moved or replaced.
 */
public class Game implements Cloneable{
    private static final Set<String> blackBoxes = new HashSet<>(Set.of(
            "0,0", "0,1", "0,2", "0,4", "0,6", "0,8", "0,9"
    ));
    char[][] board;
    private int emptyRow;
    private int emptyCol;
    private int moves;

    public Game() {
        resetGame();
    }

    // Constructor for testing purposes
    public Game(char[][] board, int emptyRow, int emptyCol, int moves) {
        this.board = board;
        this.emptyRow = emptyRow;
        this.emptyCol = emptyCol;
        this.moves = moves;
    }


    public void resetGame() {
        board = new char[][]{
                {'X', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                {' ', '2', '3', '4', '5', '6', '7', '8', '9', '1'}
        };
        emptyRow = 1;
        emptyCol = 0;
        moves = 0;
    }

    public boolean isLegalToMoveFrom(int[] from) {
        int fromRow = from[0];
        int fromCol = from[1];
        return fromRow >= 0 && fromRow < 2 && fromCol >= 0 && fromCol < 10 && board[fromRow][fromCol] != ' ';
    }

    public boolean isSolved() {
        char[][] solvedBoard = {
                {'X', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X', 'X'},
                {'1', '2', '3', '4', '5', '6', '7', '8', '9', ' '}
        };
        return Arrays.deepEquals(board, solvedBoard);
    }

    public boolean isLegalMove(int[] from, int[] to) {
        int fromRow = from[0];
        int fromCol = from[1];
        int toRow = to[0];
        int toCol = to[1];

        if ((Math.abs(fromRow - toRow) == 1 && fromCol == toCol) ||
                (Math.abs(fromCol - toCol) == 1 && fromRow == toRow)) {
            return isLegalToMoveFrom(from) && toRow >= 0 && toRow < 2 && toCol >= 0 && toCol < 10 &&
                    !blackBoxes.contains(toRow + "," + toCol) && board[toRow][toCol] == ' ';
        }
        return false;
    }

    public void makeMove(int[] from, int[] to) {
        if (isLegalMove(from, to)) {
            board[to[0]][to[1]] = board[from[0]][from[1]];
            board[from[0]][from[1]] = ' ';
            emptyRow = to[0];
            emptyCol = to[1];
            moves++;
        }
    }

    public Set<int[][]> getLegalMoves() {
        Set<int[][]> legalMoves = new HashSet<>();
        for (int fromRow = 0; fromRow < 2; fromRow++) {
            for (int fromCol = 0; fromCol < 10; fromCol++) {
                for (int toRow = 0; toRow < 2; toRow++) {
                    for (int toCol = 0; toCol < 10; toCol++) {
                        int[] from = {fromRow, fromCol};
                        int[] to = {toRow, toCol};
                        if (isLegalMove(from, to)) {
                            legalMoves.add(new int[][]{from, to});
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public Game clone() {
        Game copy;
        try {
            copy = (Game) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.board = Arrays.stream(this.board).map(char[]::clone).toArray(char[][]::new);
        copy.emptyRow = this.emptyRow;
        copy.emptyCol = this.emptyCol;
        copy.moves = this.moves;
        return copy;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return emptyRow == game.emptyRow &&
                emptyCol == game.emptyCol &&
                moves == game.moves &&
                Arrays.deepEquals(board, game.board);
    }

    public int hashCode() {
        int result = Objects.hash(emptyRow, emptyCol, moves);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    public boolean isBlackBox(int row, int col) {
        return blackBoxes.contains(row + "," + col);
    }

    public boolean move(int row, int col, int targetRow, int targetCol) {
        int[] from = {row, col};
        int[] to = {targetRow, targetCol};
        if (isLegalMove(from, to)) {
            board[targetRow][targetCol] = board[row][col];
            board[row][col] = ' ';
            emptyRow = targetRow;
            emptyCol = targetCol;
            moves++;
            return true;
        }
        return false;
    }
}
