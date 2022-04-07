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
        int iterations = 10000;

        REPL repl = new REPL(game, nodeFactory, moveFactory, iterations);

        repl.runREPL();
    }
}
