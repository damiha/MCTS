package TTT;

import MCTS.*;

public class TTTMain {

    public static void main(String[] args) {

        Game game = new TTTGame();
        NodeFactory nodeFactory = new TTTNodeFactory();
        MoveFactory moveFactory = new TTTMoveFactory();

        MCTSConfiguration mctsConfiguration = new MCTSConfiguration();
        mctsConfiguration.setFixedTime(2.0);
        mctsConfiguration.setRolloutsPerLeaf(1);
        mctsConfiguration.setLeafParallelization(false);

        REPL repl = new REPL(game, nodeFactory, moveFactory, mctsConfiguration);

        repl.runREPL();
    }
}
