package DotsAndBoxes;

import MCTS.*;

public class DBMain {

    public static void main(String[] args) {

        Game game = new DBGame(3,3);
        NodeFactory nodeFactory = new DBNodeFactory();
        MoveFactory moveFactory = new DBMoveFactory();

        MCTSConfiguration mctsConfiguration = new MCTSConfiguration();
        mctsConfiguration.setFixedTime(10.0);
        mctsConfiguration.setRolloutsPerLeaf(512);
        mctsConfiguration.setLeafParallelization(true);

        REPL repl = new REPL(game, nodeFactory, moveFactory, mctsConfiguration);

        repl.runREPL();
    }
}
