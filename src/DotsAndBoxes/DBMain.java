package DotsAndBoxes;

import MCTS.*;
import TTT.TTTGame;
import TTT.TTTMoveFactory;
import TTT.TTTNodeFactory;

import java.util.Scanner;

public class DBMain {

    public static void main(String[] args) {

        Game game = new DBGame(3,3);
        NodeFactory nodeFactory = new DBNodeFactory();
        MoveFactory moveFactory = new DBMoveFactory();
        int iterations = 10000;

        REPL repl = new REPL(game, nodeFactory, moveFactory, iterations);

        repl.runREPL();
    }
}
