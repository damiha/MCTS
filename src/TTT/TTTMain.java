package TTT;

import MCTS.*;

public class TTTMain {

    public static void main(String[] args) {

        Game game = new TTTGame();
        NodeFactory nodeFactory = new TTTNodeFactory();
        MoveFactory moveFactory = new TTTMoveFactory();

        MCTSConfiguration mctsConfiguration = new MCTSConfiguration();
        mctsConfiguration.setFixedTime(10.0);
        mctsConfiguration.setRolloutsPerLeaf(100);
        mctsConfiguration.setLeafParallelization(true);

        REPL repl = new REPL(game, nodeFactory, moveFactory, mctsConfiguration);

        repl.runREPL();
    }
}
