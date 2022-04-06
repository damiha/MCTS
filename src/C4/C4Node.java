package C4;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class C4Node extends Node {

    public C4Node(Game game) {
        super(game);
    }

    public C4Node(Game game, Node parent, Move move) {
        super(game, parent, move);
    }

    public void expandChildren(){
        ArrayList<Move> allLegalMoves = game.getAllLegalMoves();

        for(Move legalMove : allLegalMoves){

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(legalMove);
            children.add(new C4Node(gameAfterMove, this, legalMove));
        }
    }

    // parent != null, since only called in child nodes
    public double getUCB1(){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        // parent wants to see the opponent (child) lose
        double exploitationTerm = (2 * nlosses  + 1 * ndraws - 4 * nwins) / ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = 2.0;
        return exploitationTerm + C * explorationTerm;
    }

    // rollout works different from TTT
    public Player rollout(){

        // maybe this is already a terminal state ?
        if(game.whoWon() != Player.NOBODY_IN_PROGRESS){
            return game.whoWon();
        }

        Game tempGame = game.getDeepCopy();

        while(tempGame.whoWon() == Player.NOBODY_IN_PROGRESS){

            ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
            // TODO: make this more effiencient; creating a random object for each node is unnecessary
            int randomIndex = (new Random()).nextInt(movesFromHere.size());
            Move randomNextMove = movesFromHere.get(randomIndex);

            tempGame.move(randomNextMove);
        }
        return tempGame.whoWon();
    }
}
