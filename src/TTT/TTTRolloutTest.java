package TTT;

import MCTS.Game;
import MCTS.Player;

public class TTTRolloutTest {

    public static void main(String[] args) {
        // Something is completely wrong with the statistics ?!

        Game game = new TTTGame();
        game.move(new TTTMove(3));
        game.move(new TTTMove(5));
        game.move(new TTTMove(7));
        game.move(new TTTMove(1));
        game.move(new TTTMove(9));

        System.out.println(game.getRepresentation());

        TTTNode node = new TTTNode(game);
        int wins = 0;
        int draws = 0;
        int losses = 0;

        for(int i = 0; i < 10000; i++){
            Player winnerOfRollout = node.rollout();

            if(winnerOfRollout == Player.BLUE){
                wins++;
            }
            else if(winnerOfRollout == Player.RED){
                losses++;
            }else{
                draws++;
            }
        }
        System.out.println("wins: " + wins + ", draws: " + draws + ", losses: " + losses);
    }
}
