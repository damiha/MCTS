package C4;

import MCTS.*;
import TTT.TTTGame;
import TTT.TTTMoveFactory;
import TTT.TTTNodeFactory;

import java.util.Scanner;

public class C4Main {

    public static void main(String[] args) {

        Game game = new C4Game();
        NodeFactory nodeFactory = new C4NodeFactory();
        MoveFactory moveFactory = new C4MoveFactory();

        MCTSConfiguration mctsConfiguration = new MCTSConfiguration();
        mctsConfiguration.setFixedTime(5.0);

        REPL repl = new REPL(game, nodeFactory, moveFactory, mctsConfiguration);

        repl.runREPL();
    }
}
