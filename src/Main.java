import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Game game = new Game();
        System.out.println(game);

        while(true){
            System.out.print("command: ");
            String command = sc.nextLine();

            if(command.equals("A")){
                System.out.println("run the analysis!");
            }
            else{
                // interpret as a move
                int num = Integer.parseInt(command);
                game.move(num);
                System.out.println(game);

                char whoWon;
                if((whoWon = game.whoWon()) != '-'){
                    if(whoWon != 'D'){
                        System.out.println("player " + whoWon + " has won the game!");
                    }
                    else{
                        System.out.println("it's a draw!");
                    }
                    // exit the program
                    return;
                }

                MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(game);
                int bestMove = mcts.getBestMove(1000000);

                mcts.showDistribution();
                System.out.println("best move according to mcts: " + bestMove);
            }
        }
    }
}
