package C4;

import MCTS.Game;
import MCTS.Move;
import MCTS.Player;

import java.util.ArrayList;

public class C4Game implements Game {

    Player player;
    char[][] board;

    int width = 7;
    int height = 6;

    int emptySquares = width * height;

    public C4Game(){
        this.player = Player.BLUE;
        board = new char[height][width];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                board[y][x] = '.';
            }
        }
    }

    @Override
    public Game getDeepCopy() {
        C4Game deepCopy = new C4Game();

        deepCopy.player = player;
        deepCopy.emptySquares = emptySquares;

        for(int y = 0;y < height; y++){
            for(int x = 0; x < width; x++){
                deepCopy.board[y][x] = board[y][x];
            }
        }
        return deepCopy;
    }

    @Override
    public ArrayList<Move> getAllLegalMoves() {

        ArrayList<Move> allLegalMoves = new ArrayList<>();
        // is top row still vacant ?
        for(int i = 0; i < width; i++){
            if(board[0][i] == '.'){
                allLegalMoves.add(new C4Move(i + 1));
            }
        }
        return allLegalMoves;
    }

    @Override
    public void move(Move move) {
        // other moves are simply ignored
        if(! (move instanceof C4Move)){
            return;
        }

        int columnIndex = ((C4Move)move).num() - 1;

        // search from bottom to top to find first vacant spot
        for(int i = height - 1; i >= 0; i--){
            if(board[i][columnIndex] == '.'){
                board[i][columnIndex] = player == Player.BLUE ? 'X' : 'O';
                break;
            }
        }

        player = player.getOpponent();
        emptySquares --;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getOpponent() {
        return player.getOpponent();
    }

    // TODO: make more efficient
    private Player horizontal4Connected(){
        int xPoints = 0;
        int oPoints = 0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x <= width - 4; x++){
                for(int i = 0; i < 4; i++){
                    xPoints += (board[y][x + i] == 'X' ? 1 : 0);
                    oPoints += (board[y][x + i] == 'O' ? 1 : 0);
                }
                if(xPoints == 4 || oPoints == 4){
                    return xPoints == 4 ? Player.BLUE : Player.RED;
                }
                else{
                    xPoints = oPoints = 0;
                }
            }
        }
        return emptySquares > 1 ? Player.NOBODY_IN_PROGRESS : Player.NOBODY_DRAW;
    }

    // TODO: make more efficient
    private Player vertical4Connected(){
        int xPoints = 0;
        int oPoints = 0;

        for(int x = 0; x < width; x++){
            for(int y = 0; y <= height - 4; y++){
                for(int i = 0; i < 4; i++){
                    xPoints += (board[y + i][x] == 'X' ? 1 : 0);
                    oPoints += (board[y + i][x] == 'O' ? 1 : 0);
                }
                if(xPoints == 4 || oPoints == 4){
                    return xPoints == 4 ? Player.BLUE : Player.RED;
                }
                else{
                    xPoints = oPoints = 0;
                }
            }
        }
        return emptySquares > 1 ? Player.NOBODY_IN_PROGRESS : Player.NOBODY_DRAW;
    }

    // upper left to lower right
    private Player ULtoLRDiagonal4Connected(){
        int xPoints = 0;
        int oPoints = 0;

        for(int y = 0; y < height -  3; y++){
            for(int x = 0; x < width - 3; x++){
                for(int i = 0; i < 4; i++){
                    xPoints += (board[y + i][x + i] == 'X' ? 1 : 0);
                    oPoints += (board[y + i][x + i] == 'O' ? 1 : 0);
                }
                if(xPoints == 4 || oPoints == 4){
                    return xPoints == 4 ? Player.BLUE : Player.RED;
                }
                else{
                    xPoints = oPoints = 0;
                }
            }
        }
        return emptySquares > 1 ? Player.NOBODY_IN_PROGRESS : Player.NOBODY_DRAW;
    }

    // lower left to upper right diagonal
    private Player LLtoURDiagonal4Connected(){
        int xPoints = 0;
        int oPoints = 0;

        for(int y = 0; y < height - 3; y++){
            for(int x = 3; x < width; x++){
                for(int i = 0; i < 4; i++){
                    xPoints += (board[y + i][x - i] == 'X' ? 1 : 0);
                    oPoints += (board[y + i][x - i] == 'O' ? 1 : 0);
                }
                if(xPoints == 4 || oPoints == 4){
                    return xPoints == 4 ? Player.BLUE : Player.RED;
                }
                else{
                    xPoints = oPoints = 0;
                }
            }
        }
        return emptySquares > 1 ? Player.NOBODY_IN_PROGRESS : Player.NOBODY_DRAW;
    }

    @Override
    public Player whoWon() {

        if(horizontal4Connected() != Player.NOBODY_IN_PROGRESS){
            return horizontal4Connected();
        }
        else if(vertical4Connected() != Player.NOBODY_IN_PROGRESS){
            return vertical4Connected();
        }
        else if(ULtoLRDiagonal4Connected() != Player.NOBODY_IN_PROGRESS){
            return ULtoLRDiagonal4Connected();
        }
        else if(LLtoURDiagonal4Connected() != Player.NOBODY_IN_PROGRESS){
            return LLtoURDiagonal4Connected();
        }
        return emptySquares > 1 ? Player.NOBODY_IN_PROGRESS : Player.NOBODY_DRAW;
    }

    @Override
    public String getRepresentation() {
        String rep = "";
        rep += ("current player: " + player + "\n");
        for(int y = 0;y < height; y++){
            for(int x = 0; x < width; x++){
                rep += (board[y][x] + " ");
            }
            rep += "\n";
        }
        return rep;
    }
}
