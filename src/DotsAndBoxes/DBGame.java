package DotsAndBoxes;

import MCTS.Game;
import MCTS.Move;
import MCTS.Player;

import java.util.ArrayList;

public class DBGame implements Game {

    private final static int WIDTH = 3;
    private final static int HEIGHT = 3;
    private final static int MIN_CAPTURES_TO_WIN = (WIDTH * HEIGHT) / 2 + 1;

    private Player player;
    private Player prevPlayer;

    private int blueCaptured;
    private int redCaptured;

    private final Tile[][] board;

    public DBGame(){
        // Blue player begins by convention
        player = Player.BLUE;
        prevPlayer = Player.BLUE;

        blueCaptured = 0;
        redCaptured = 0;

        board = new Tile[HEIGHT][WIDTH];

        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                board[y][x] = new Tile(x, y, getPositionFrom(x, y));
            }
        }
    }

    private Position getPositionFrom(int x, int y){
        boolean isLeft = x == 0;
        boolean isRight = x == WIDTH - 1;
        boolean isTop = y == 0;
        boolean isBottom = y == HEIGHT - 1;

        if(isTop && isLeft)
            return Position.TOP_LEFT;
        else if(isTop && isRight)
            return Position.TOP_RIGHT;
        else if(isTop)
            return Position.TOP_CENTER;
        else if(isBottom && isLeft)
            return Position.BOTTOM_LEFT;
        else if(isBottom && isRight)
            return Position.BOTTOM_RIGHT;
        else if(isBottom)
            return Position.BOTTOM_CENTER;
        else if(isLeft)
            return Position.LEFT_CENTER;
        else if(isRight)
            return Position.RIGHT_CENTER;
        else
            return Position.CENTER;
    }

    public boolean hasCaptureOccuredAt(int x, int y){

        if(board[y][x].isCaptured()){
            board[y][x].setCapturedBy(player);
            blueCaptured += player == Player.BLUE ? 1 : 0;
            redCaptured += player == Player.RED ? 1 : 0;
            return true;
        }
        return false;
    }

    @Override
    public void move(Move move) {
        // simply ignore other types of moves
        if(! (move instanceof  DBMove)){
            return;
        }
        Direction direction = ((DBMove)move).direction();
        int x = ((DBMove)move).x();
        int y = ((DBMove)move).y();

        boolean hasCaptureOccurred = false;

        if(direction == Direction.RIGHT){
            board[y][x + 1].clickAt(Direction.LEFT);

            hasCaptureOccurred = hasCaptureOccuredAt(x + 1, y);
        }
        // only moves with RIGHT or BOTTOM direction are generated
        else{
            board[y + 1][x].clickAt(Direction.TOP);

            hasCaptureOccurred = hasCaptureOccuredAt(x, y + 1);
        }
        board[y][x].clickAt(direction);

        hasCaptureOccurred =  hasCaptureOccuredAt(x, y) || hasCaptureOccurred;

        // save prev player
        prevPlayer = player;

        // a player who captures a tile gets to take another turn
        if(!hasCaptureOccurred){
            player = Player.getOpponent(player);
        }
    }

    @Override
    public Game getDeepCopy() {
        DBGame deepCopy = new DBGame();
        deepCopy.player = this.player;
        deepCopy.prevPlayer = player;
        deepCopy.blueCaptured = this.blueCaptured;
        deepCopy.redCaptured = this.redCaptured;

        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                deepCopy.board[y][x] = this.board[y][x].getDeepCopy();
            }
        }
        return deepCopy;
    }

    // TODO: optimize this, the number of legal moves is strictly decreasing
    @Override
    public ArrayList<Move> getAllLegalMoves() {

        ArrayList<Move> allLegalMoves = new ArrayList<Move>();

        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){

                if(board[y][x].isRightPossible()){
                    allLegalMoves.add(new DBMove(x, y, Direction.RIGHT));
                }
                if(board[y][x].isBottomPossible()){
                    allLegalMoves.add(new DBMove(x, y, Direction.BOTTOM));
                }
            }
        }
        return allLegalMoves;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getWinner() {
        if(blueCaptured >= MIN_CAPTURES_TO_WIN){
            return Player.BLUE;
        }
        else if(redCaptured >= MIN_CAPTURES_TO_WIN){
            return Player.RED;
        }
        // a draw is possible if the number of tiles is even
        else if(blueCaptured + redCaptured == WIDTH * HEIGHT){
            return Player.NOBODY_DRAW;
        }
        else{
            return Player.NOBODY_IN_PROGRESS;
        }
    }

    // TODO: make this drawing function less primitive
    @Override
    public String getString() {

        int charactersPerTileWidth = board[0][0].getConsoleRepresentation()[0].length;
        int charactersPerTileHeight = board[0][0].getConsoleRepresentation().length;

        // last subtraction term because we draw the tiles over one another (third column of left tile = first column of right tile etc)
        int characterWidth = charactersPerTileWidth * WIDTH - (WIDTH - 1);
        int characterHeight = charactersPerTileHeight * HEIGHT - (HEIGHT - 1);

        char[][] characters = new char[characterHeight][characterWidth];

        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0;  x < WIDTH; x++){

                // we want the tiles to overlap
                int startX = (charactersPerTileWidth - 1) * x;
                int startY = (charactersPerTileHeight - 1) * y;

                char[][] consoleRepresentation = board[y][x].getConsoleRepresentation();

                for(int i = 0; i < charactersPerTileHeight; i++){
                    for(int j = 0; j < charactersPerTileWidth; j++){
                        characters[startY + i][startX + j] = consoleRepresentation[i][j];
                    }
                }
            }
        }

        // convert the characters line by line
        String finalConsoleBoard = "current player: " + player + "\n";

        for(int y = 0; y < characterHeight; y++){
            finalConsoleBoard += String.valueOf(characters[y]) + "\n";
        }
        return finalConsoleBoard;
    }

    public String toString(){
        return getString();
    }

    public Player getPrevPlayer(){
        return prevPlayer;
    }
}
