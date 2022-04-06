package C4;

import MCTS.*;
import TTT.TTTGame;
import TTT.TTTMove;
import TTT.TTTNodeFactory;

import java.util.Scanner;

public class C4REPL {

    public static void main(String[] args) {

        Game game = new C4Game();
        NodeFactory nodeFactory = new C4NodeFactory();

        int iterations = 100000;

        Scanner sc = new Scanner(System.in);
        System.out.println(game.getRepresentation());

        /*
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game, nodeFactory);
        Move bestMove = mcts.getBestMove(iterations);
        mcts.showDistribution();
        System.out.println("best move according to mcts: " + bestMove.getString());
        */

        while(true){
            System.out.print("command: ");
            String command = sc.nextLine();

            // interpret as a move
            Move nextMove = C4Move.fromString(command);
            game.move(nextMove);
            System.out.println(game.getRepresentation());

            Player whoWon;
            if((whoWon = game.whoWon()) != Player.NOBODY_IN_PROGRESS){
                if(whoWon != Player.NOBODY_DRAW){
                    System.out.println("player " + whoWon + " has won the game!");
                }
                else{
                    System.out.println("it's a draw!");
                }
                // exit the program
                return;
            }
            // TODO: don't build up new Monte Carlo Tree
            MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game, nodeFactory);
            Move bestMove = mcts.getBestMove(iterations);
            // if it's humans turn (blue), dont show evaluation
            if(game.getPlayer() == Player.RED){
                mcts.showDistribution();
                System.out.println("best move according to mcts: " + bestMove.getString());
            }
        }
    }
}
