package Connect4;

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

    // rollout works different from TTT
    public Player rollout(){

        Game tempGame = getGame().getDeepCopy();

        while(tempGame.isInProgress()){

            // TODO: make this more efficient; creating a random object for each node is unnecessary
            ArrayList<Move> allLegalMoves = tempGame.getAllLegalMoves();
            int randomIndex = (new Random()).nextInt(allLegalMoves.size());
            Move randomMove = allLegalMoves.get(randomIndex);

            tempGame.move(randomMove);
        }
        return tempGame.getWinner();
    }
}
