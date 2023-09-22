package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board _board;
    /** Current score. */
    private int _score;
    /** Maximum score so far.  Updated when game ends. */
    private int _maxScore;
    /** True iff game is ended. */
    private boolean _gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        _board = new Board(size);
        _score = _maxScore = 0;
        _gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        _board = new Board(rawValues);
        this._score = score;
        this._maxScore = maxScore;
        this._gameOver = gameOver;

    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return _board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return _board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (_gameOver) {
            _maxScore = Math.max(_score, _maxScore);
        }
        return _gameOver;
    }

    /** Return the current score. */
    public int score() {
        return _score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return _maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        _score = 0;
        _gameOver = false;
        _board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        _board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        _board.setViewingPerspective(side);
        for(int column_side = 0; column_side < _board.size(); column_side++){
            int beforevalue = 0;
            for (int row_side = _board.size() - 1; row_side >-1; row_side--){
                Tile store = _board.tile(column_side, row_side);
                if (store != null){
                    int realvalue = singlecolumn_side(_board, column_side, row_side, store.value(), beforevalue);
                    boolean is_it_merged = _board.move(column_side, realvalue, store);
                    if (is_it_merged){
                        _score = _score + store.value() *2;
                        beforevalue = store.value() * 2;
                    }
                }
            }
        }




        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    private boolean singlecolumn_side(Board board, int columnSide, int rowSide, int value, int beforevalue) {
        for (int row_side = rowSide + 1; row_side < board.size(); row_side++){
            Tile storevalue = board.tile(columnSide, row_side);
            if (storevalue == null){
                continue;
            }
            else if (storevalue != null){
                if (storevalue.value() == value && storevalue.value() != beforevalue){
                    return true;
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        _gameOver = checkGameOver(_board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        int column = 0;
        int row = 0;

        while (b.tile(column, row) != null) {
            if (column == b.size() - 1 && row == b.size() - 1) {
                return false;
            }
            else if (row == b.size() - 1) {
                column = column + 1;
                row = 0;
            }
            row = row + 1;
        }

        return true;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for(int column = 0; column < b.size(); column++) {
            for(int row = 0; row < b.size(); row++){
                if (b.tile(column, row) != null){
                    if (b.tile(column, row).value() == MAX_PIECE){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        for(int column = 0; column < b.size(); column++) {
            for(int row = 0; row < b.size(); row++){
                if (b.tile(column, row) == null){
                    return true;}
                else if (b.tile(column, row) != null){
                    if (check_for_adjacent(b, b.tile(column, row))){
                        return true;
                    }
                    }
                }
            }

        return false;
    }

    public static boolean check_for_adjacent(Board b, Tile t) {
        int row_adj = t.row();
        int column_adj = t.col();
        if (row_adj + 1 < b.size()){
            if(b.tile(column_adj, row_adj +1) != null && b.tile(column_adj, row_adj +1).value() == t.value()){
                return true;
            }
        }
        if (row_adj - 1 < -1){
            if(b.tile(column_adj, row_adj -1) != null && b.tile(column_adj, row_adj -1).value() == t.value()){
                return true;
            }
        }
        if (column_adj + 1 < b.size()){
            if(b.tile(column_adj + 1, row_adj ) != null && b.tile(column_adj +1, row_adj).value() == t.value()){
                return true;
            }
        }
        if (column_adj - 1 < -1){
            if(b.tile(column_adj - 1, row_adj ) != null && b.tile(column_adj -1, row_adj).value() == t.value()){
                return true;
            }
        }
        return false;
    }

    /** Returns the model as a string, used for debugging. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    /** Returns whether two models are equal. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    /** Returns hash code of Model’s string. */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
