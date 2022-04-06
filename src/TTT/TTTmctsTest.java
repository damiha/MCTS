package TTT;

import MCTS.Game;
import MCTS.MonteCarloTreeSearch;
import MCTS.Move;
import MCTS.NodeFactory;

public class TTTmctsTest {

    public static void main(String[] args) {

        int iterations = 100;

        Game game = new TTTGame();
        NodeFactory nodeFactory = new TTTNodeFactory();

        game.move(new TTTMove(3));
        game.move(new TTTMove(5));
        game.move(new TTTMove(7));
        game.move(new TTTMove(1));
        game.move(new TTTMove(9));

        System.out.println(game.getRepresentation());

        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game, nodeFactory);
        Move bestMove = mcts.getBestMove(iterations);
        mcts.showDistribution();
        System.out.println("best move according to mcts: " + bestMove.getString());
    }
}
