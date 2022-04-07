package C4;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Random;

public class C4Node extends Node {

    public C4Node(Game game) {
        super(game);
    }

    public C4Node(Game game, Node parent, Move move) {
        super(game, parent, move);
    }

    public Node expand(){

        // never expand terminal nodes
        if(game.whoWon() != Player.NOBODY_IN_PROGRESS){
            untriedMoves.clear();
            return this;
        }

        if(untriedMoves.size() != 0){
            int randomIndex = (new Random()).nextInt(untriedMoves.size());
            Move randomNextMove = untriedMoves.get(randomIndex);

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(randomNextMove);

            untriedMoves.remove(randomNextMove);
            Node newNode = new C4Node(gameAfterMove, this, randomNextMove);
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
        // parent wants to see the opponent (child) lose
        double exploitationTerm = value / ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = Math.sqrt(2.0);
        return exploitationTerm + C * explorationTerm;
    }

    // rollout works different from TTT
    public double rollout(){

        Game tempGame = game.getDeepCopy();

        while(tempGame.whoWon() == Player.NOBODY_IN_PROGRESS){

            ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
            // TODO: make this more effiencient; creating a random object for each node is unnecessary
            int randomIndex = (new Random()).nextInt(movesFromHere.size());
            Move randomNextMove = movesFromHere.get(randomIndex);

            tempGame.move(randomNextMove);
        }
        if(tempGame.whoWon() == Player.NOBODY_DRAW){
            return 0.0;
        }
        else{
            return tempGame.whoWon() ==  game.getPlayerBeforeMove() ? 1.0 : -1.0;
        }
    }
}
