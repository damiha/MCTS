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
        double secondsToThink = 10.0;

        REPL repl = new REPL(game, nodeFactory, moveFactory, secondsToThink);

        repl.runREPL();
    }
}
