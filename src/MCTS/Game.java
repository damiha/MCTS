package MCTS;

import java.util.ArrayList;

public interface Game {

    void move(Move move);

    Game getDeepCopy();

    ArrayList<Move> getAllLegalMoves();

    Player getPlayer();

    Player getWinner();

    default boolean isInProgress(){
        return getWinner() == Player.NOBODY_IN_PROGRESS;
    }

    default boolean isGameOver(){
        return getWinner() != Player.NOBODY_IN_PROGRESS;
    }

    String getString();
}
