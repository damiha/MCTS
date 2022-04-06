import MCTS.Game;
import MCTS.MonteCarloTreeSearch;
import MCTS.Move;
import MCTS.Player;

import java.util.Scanner;

public class GameREPL {

    private final Game game;
    private final int iterations = 10000000;

    public GameREPL(Game game){
        this.game = game;
    }

    public void run(){

        Scanner sc = new Scanner(System.in);
        System.out.println(game);

        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game);
        Move bestMove = mcts.getBestMove(iterations);
        mcts.showDistribution();
        System.out.println("best move according to mcts: " + bestMove.getString());

        while(true){
            System.out.print("command: ");
            String command = sc.nextLine();

            // interpret as a move
            Move nextMove = TTTMove.fromString(command);
            game.move(nextMove);
            System.out.println(game);

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

            mcts = new MonteCarloTreeSearch(game);
            bestMove = mcts.getBestMove(iterations);
            mcts.showDistribution();
            System.out.println("best move according to mcts: " + bestMove.getString());
        }
    }
}
