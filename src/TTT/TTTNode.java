package TTT;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TTTNode extends Node {

    public TTTNode(Game game) {
        super(game);
    }

    public TTTNode(Game game, Node parent, Move move) {
        super(game, parent, move);
    }

    public Node expand(){

        if(untriedMoves.size() != 0){
            int randomIndex = (new Random()).nextInt(untriedMoves.size());
            Move randomNextMove = untriedMoves.get(randomIndex);

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(randomNextMove);

            untriedMoves.remove(randomNextMove);
            Node newNode = new TTTNode(gameAfterMove, this, randomNextMove);
            children.add(newNode);

            return newNode;
        }
        return this;
    }

    // parent != null, since only called in child nodes
    public double getUCB1(){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        double exploitationTerm = value / ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = Math.sqrt(2.0);
        return exploitationTerm + C * explorationTerm;
    }

    public double rollout(){

        // maybe this is already a terminal state ?
        if(game.whoWon() != Player.NOBODY_IN_PROGRESS){
           if(game.whoWon() == Player.NOBODY_DRAW){
               return 0;
           }
           return game.whoWon() == game.getPlayerBeforeMove() ? 1.0 : -1.0;
        }

        Game tempGame = game.getDeepCopy();
        ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
        Collections.shuffle(movesFromHere);

        for(Move move : movesFromHere){

            tempGame.move(move);

            // did we win after making the move
            if(tempGame.whoWon() != Player.NOBODY_IN_PROGRESS){
                if(tempGame.whoWon() == Player.NOBODY_DRAW){
                    return 0.0;
                }
                return tempGame.whoWon() == game.getPlayerBeforeMove() ? 1.0 : -1.0;
            }
        }
        throw new RuntimeException("Simulation finished without a result. This should never happen!");
    }
}
