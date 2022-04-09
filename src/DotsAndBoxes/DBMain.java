package DotsAndBoxes;

import MCTS.*;

public class DBMain {

    public static void main(String[] args) {

        Game game = new DBGame(3,3);
        NodeFactory nodeFactory = new DBNodeFactory();
        MoveFactory moveFactory = new DBMoveFactory();

        MCTSConfiguration mctsConfiguration = new MCTSConfiguration();
        mctsConfiguration.setFixedTime(5.0);

        REPL repl = new REPL(game, nodeFactory, moveFactory, mctsConfiguration);

        repl.runREPL();
    }
}
