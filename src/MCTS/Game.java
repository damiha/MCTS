package MCTS;

import java.util.ArrayList;

public interface Game {

    void move(Move move);

    Game getDeepCopy();

    ArrayList<Move> getAllLegalMoves();

    Player getPlayer();

    // in Games were players can make multiple moves in a row, this MUST be overwritten!
    default Player getPrevPlayer(){
        return Player.getOpponent(getPlayer());
    }

    Player getWinner();

    default boolean isInProgress(){
        return getWinner() == Player.NOBODY_IN_PROGRESS;
    }

    default boolean isGameOver(){
        return getWinner() != Player.NOBODY_IN_PROGRESS;
    }

    String getString();
}
