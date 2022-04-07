package TTT;

import MCTS.Game;
import MCTS.MoveFactory;
import MCTS.NodeFactory;
import MCTS.REPL;

public class TTTMain {

    public static void main(String[] args) {

        Game game = new TTTGame();
        NodeFactory nodeFactory = new TTTNodeFactory();
        MoveFactory moveFactory = new TTTMoveFactory();
        int iterations = 10000;

        REPL repl = new REPL(game, nodeFactory, moveFactory, iterations);

        repl.runREPL();
    }
}
