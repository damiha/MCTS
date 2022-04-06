package MCTS;

import java.util.ArrayList;

public interface Game {

    Game getDeepCopy();

    ArrayList<Move> getAllLegalMoves();

    void move(Move move);

    Player getPlayer();

    Player getOpponent();

    Player whoWon();

    String getRepresentation();
}
