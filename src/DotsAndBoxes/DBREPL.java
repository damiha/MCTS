package DotsAndBoxes;

import MCTS.*;

import java.util.Scanner;

public class DBREPL {

    public static void main(String[] args) {

        Player humanPlayer = Player.BLUE;

        Game game = new DBGame();
        NodeFactory nodeFactory = new DBNodeFactory();

        int iterations = 1000000;

        Scanner sc = new Scanner(System.in);
        System.out.println(game.getString());

        if(game.getPlayer() != humanPlayer) {
            MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game, nodeFactory);
            Move bestMove = mcts.getBestMove(iterations);
            mcts.showDistribution();
            System.out.println("best move according to mcts: " + bestMove.getString());
        }
        while(true){
            System.out.print("command: ");
            String command = sc.nextLine();

            // interpret as a move
            Move nextMove = DBMove.fromString(command);
            game.move(nextMove);
            System.out.println(game.getString());

            Player whoWon;
            if((whoWon = game.getWinner()) != Player.NOBODY_IN_PROGRESS){
                if(whoWon != Player.NOBODY_DRAW){
                    System.out.println("player " + whoWon + " has won the game!");
                }
                else{
                    System.out.println("it's a draw!");
                }
                // exit the program
                return;
            }
            if(game.getPlayer() != humanPlayer){
                MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game, nodeFactory);
                Move bestMove = mcts.getBestMove(iterations);
                mcts.showDistribution();
                System.out.println("best move according to mcts: " + bestMove.getString());
            }
        }
    }
}
