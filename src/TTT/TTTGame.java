package TTT;

import MCTS.Game;
import MCTS.Move;
import MCTS.Player;

import java.util.ArrayList;

public class TTTGame implements Game {

    char[][] board;
    Player player;
    int emptySquares = 9;

    public TTTGame(){

        board = new char[3][3];

        // BLUE = X, RED = O
        player = Player.BLUE;

        for(int y = 0;y < 3; y++){
            for(int x = 0; x < 3; x++){
                board[y][x] = '.';
            }
        }
    }

    public TTTGame getDeepCopy(){
        TTTGame deepCopy = new TTTGame();

        deepCopy.player = player;
        deepCopy.emptySquares = emptySquares;

        for(int y = 0;y < 3; y++){
            for(int x = 0; x < 3; x++){
                deepCopy.board[y][x] = board[y][x];
            }
        }
        return deepCopy;
    }

    public ArrayList<Move> getAllLegalMoves(){

        ArrayList<Move> allLegalMoves = new ArrayList<>();

        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 3; x++){
                if(board[y][x] == '.')
                    allLegalMoves.add(new TTTMove(3 * y + x + 1));
            }
        }
        return allLegalMoves;
    }

    public void move(Move move){

        // ignore other types of moves
        if(!(move instanceof TTTMove)){
            return;
        }
        // translate number from 1 - 9 to indices 0..2
        int num = ((TTTMove)move).getNumber();
        num -= 1;
        int y = num / 3;
        int x = num % 3;

        board[y][x] = player == Player.BLUE ? 'X' : 'O';

        player =  Player.getOpponent(player);
        emptySquares -= 1;
    }

    public Player getWinner(){

        // check horizontal lines
        for(int y = 0;y < 3; y++){
            int xPoints = 0;
            int yPoints = 0;
            for(int x = 0; x < 3; x++){
                xPoints += (board[y][x] == 'X' ? 1 : 0);
                yPoints += (board[y][x] == 'O' ? 1 : 0);
            }

            if(xPoints == 3 || yPoints == 3){
                return xPoints == 3 ? Player.BLUE : Player.RED;
            }
        }

        // check vertical lines
        for(int x = 0;x < 3; x++){
            int xPoints = 0;
            int yPoints = 0;
            for(int y = 0; y < 3; y++){
                xPoints += (board[y][x] == 'X' ? 1 : 0);
                yPoints += (board[y][x] == 'O' ? 1 : 0);
            }

            if(xPoints == 3 || yPoints == 3){
                return xPoints == 3 ?  Player.BLUE : Player.RED;
            }
        }

        int xPoints = 0;
        int yPoints = 0;

        // check diagonals
        for(int i = 0;i < 3; i++){
            xPoints += (board[i][i] == 'X' ? 1 : 0);
            yPoints += (board[i][i] == 'O' ? 1 : 0);
        }

        if(xPoints == 3 || yPoints == 3){
            return xPoints == 3 ?  Player.BLUE : Player.RED;
        }

        xPoints = 0;
        yPoints = 0;

        for(int i = 2; i >= 0; i--){
            xPoints += (board[2 - i][i] == 'X' ? 1 : 0);
            yPoints += (board[2 - i][i] == 'O' ? 1 : 0);
        }

        if(xPoints == 3 || yPoints == 3){
            return xPoints == 3 ?  Player.BLUE : Player.RED;
        }

        // D = draw, - as not decided yet
        return emptySquares == 0 ? Player.NOBODY_DRAW : Player.NOBODY_IN_PROGRESS;
    }

    @Override
    public String getString() {
        String rep = "";
        rep += ("current player: " + player + "\n");
        for(int y = 0;y < 3; y++){
            for(int x = 0; x < 3; x++){
                rep += (board[y][x] + " ");
            }
            rep += "\n";
        }
        return rep;
    }

    public String toString(){
        return getString();
    }

    public Player getPlayer(){
        return player;
    }
}
