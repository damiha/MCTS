package DotsAndBoxes;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Collections;

public class DBNode extends Node {

    public DBNode(Game game) {
        super(game);
    }

    public DBNode(Game game, Node parent, Move move) {
        super(game, parent, move);
    }

    public Player rollout(){

        // maybe this is already a terminal state ?
        if(getGame().isGameOver()){
            return getGame().getWinner();
        }

        Game tempGame = getGame().getDeepCopy();
        ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
        Collections.shuffle(movesFromHere);

        for(Move move : movesFromHere){

            tempGame.move(move);

            // did we win after making the move
            if(tempGame.isGameOver()){
                return tempGame.getWinner();
            }
        }
        throw new RuntimeException("Simulation finished without a result. This should never happen!");
    }
}
