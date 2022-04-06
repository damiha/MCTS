package TTT;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Collections;

public class TTTNode extends Node {

    public TTTNode(Game game) {
        super(game);
    }

    public TTTNode(Game game, Node parent, Move move) {
        super(game, parent, move);
    }

    public void expandChildren(){
        ArrayList<Move> allLegalMoves = game.getAllLegalMoves();

        for(Move legalMove : allLegalMoves){

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(legalMove);
            children.add(new TTTNode(gameAfterMove, this, legalMove));
        }
    }

    // parent != null, since only called in child nodes
    public double getUCB1(){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        double exploitationTerm = (1 * nwins  + 0.5 * ndraws - 0 * nlosses) / ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = 2.0;
        return exploitationTerm + C * explorationTerm;
    }

    public Player rollout(){

        // maybe this is already a terminal state ?
        if(game.whoWon() != Player.NOBODY_IN_PROGRESS){
            return game.whoWon();
        }

        Game tempGame = game.getDeepCopy();
        ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
        Collections.shuffle(movesFromHere);

        for(Move move : movesFromHere){

            Player finishingPlayer = tempGame.getPlayer();
            tempGame.move(move);

            // did we win after making the move
            if(tempGame.whoWon() != Player.NOBODY_IN_PROGRESS){
                return game.whoWon();
            }
        }
        throw new RuntimeException("Simulation finished without a result. This should never happen!");
    }
}
