import java.util.ArrayList;

public class Game {

    char[][] board;
    char player;
    int emptySquares = 9;

    public Game(){

        board = new char[3][3];
        // This player starts
        player = 'X';

        for(int y = 0;y < 3; y++){
            for(int x = 0; x < 3; x++){
                board[y][x] = '.';
            }
        }
    }

    public Game getDeepCopy(){
        Game deepCopy = new Game();

        deepCopy.player = player;
        deepCopy.emptySquares = emptySquares;

        for(int y = 0;y < 3; y++){
            for(int x = 0; x < 3; x++){
                deepCopy.board[y][x] = board[y][x];
            }
        }
        return deepCopy;
    }

    public ArrayList<Integer> getAllLegalMoves(){

        ArrayList<Integer> allLegalMoves = new ArrayList<Integer>();

        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 3; x++){
                if(board[y][x] == '.')
                    allLegalMoves.add(3 * y + x + 1);
            }
        }
        return allLegalMoves;
    }

    public void move(int num){
        // translate number from 1 - 9 to indices 0..2
        num -= 1;
        int y = num / 3;
        int x = num % 3;
        board[y][x] = player;

        player =  player == 'X' ? 'O' : 'X';
        emptySquares -= 1;
    }

    public char getOpponent(){
        return player == 'X' ? 'O' : 'X';
    }

    public String toString(){
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

    public char whoWon(){

        // check horizontal lines
        for(int y = 0;y < 3; y++){
            int xPoints = 0;
            int yPoints = 0;
            for(int x = 0; x < 3; x++){
                xPoints += (board[y][x] == 'X' ? 1 : 0);
                yPoints += (board[y][x] == 'O' ? 1 : 0);
            }

            if(xPoints == 3 || yPoints == 3){
                return xPoints == 3 ? 'X' : 'O';
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
                return xPoints == 3 ? 'X' : 'O';
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
            return xPoints == 3 ? 'X' : 'O';
        }

        xPoints = 0;
        yPoints = 0;

        for(int i = 2; i >= 0; i--){
            xPoints += (board[2 - i][i] == 'X' ? 1 : 0);
            yPoints += (board[2 - i][i] == 'O' ? 1 : 0);
        }

        if(xPoints == 3 || yPoints == 3){
            return xPoints == 3 ? 'X' : 'O';
        }

        // D = draw, - as not decided yet
        return emptySquares == 0 ? 'D' : '-';
    }
}
