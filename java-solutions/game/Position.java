package game;

/**
* Class position provides info about game to player.
* This class is suitable for playing games with the same figures for each side.
*/
public interface Position {

    // Checks if move is valid.
    boolean isValid(Move move);

    // Returns visible field for player
    Field<Cell> getField();

    Cell getTurn();
}
