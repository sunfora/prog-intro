package game;

/**
* Board class represents game board.
*/
public interface Board {
    Position getPosition();

    /**
    * Makes Move on board
    */
    GameResult makeMove(Move move);
}
