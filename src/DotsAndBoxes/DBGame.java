package DotsAndBoxes;

import MCTS.Game;
import MCTS.Move;
import MCTS.MoveFormatException;
import MCTS.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class DBGame implements Game {

    private final int width;
    private final int height;
    private final int minCapturesToWin;

    private Player player;
    private Player prevPlayer;

    private int blueCaptured;
    private int redCaptured;

    private final Tile[][] board;

    public DBGame(int width, int height){

        this.width = width;
        this.height = height;
        this.minCapturesToWin = (width * height) / 2 + 1;

        // Blue player begins by convention
        player = Player.BLUE;
        prevPlayer = Player.BLUE;

        blueCaptured = 0;
        redCaptured = 0;

        board = new Tile[height][width];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                board[y][x] = new Tile(x, y);
            }
        }
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

        if(direction == Direction.TOP && y - 1 >= 0){
            board[y - 1][x].clickAt(Direction.BOTTOM);
            hasCaptureOccurred = hasCaptureOccuredAt(x, y - 1);
        }
        else if(direction == Direction.RIGHT && x + 1 < width){
            board[y][x + 1].clickAt(Direction.LEFT);
            hasCaptureOccurred = hasCaptureOccuredAt(x + 1, y);
        }
        else if(direction == Direction.BOTTOM && y + 1 < height){
            board[y + 1][x].clickAt(Direction.TOP);
            hasCaptureOccurred = hasCaptureOccuredAt(x, y + 1);
        }
        else if(direction == Direction.LEFT && x - 1 >= 0){
            board[y][x - 1].clickAt(Direction.RIGHT);
            hasCaptureOccurred = hasCaptureOccuredAt(x - 1, y);
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

    // input is already a canonical move
    @Override
    public void makeUserMove(Move userMove) throws MoveFormatException {

        DBMove dbMove = ((DBMove) userMove);

        if(dbMove.x() < 0 || dbMove.x() >= width){
            throw new MoveFormatException("x should be between 0 and " + (width - 1));
        }

        if(dbMove.y() < 0 || dbMove.y() >= height){
            throw new MoveFormatException("y should be between 0 and " + (height - 1));
        }

        if(!getAllLegalMoves().contains(dbMove)){
            throw new MoveFormatException("this move is illegal.");
        }

        move(dbMove);
    }

    @Override
    public Game getDeepCopy() {
        DBGame deepCopy = new DBGame(this.width, this.height);
        deepCopy.player = this.player;
        deepCopy.prevPlayer = player;
        deepCopy.blueCaptured = this.blueCaptured;
        deepCopy.redCaptured = this.redCaptured;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                deepCopy.board[y][x] = this.board[y][x].getDeepCopy();
            }
        }
        return deepCopy;
    }

    @Override
    public ArrayList<Move> getAllLegalMoves() {

        HashSet<Move> legalMoveSet = new HashSet<>();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){

                boolean[] clicked = board[y][x].getClicked();

                if(!clicked[Direction.TOP.get()]){
                    legalMoveSet.add(getCanonicalMove(x, y, Direction.TOP));
                }
                if(!clicked[Direction.RIGHT.get()]){
                    legalMoveSet.add(getCanonicalMove(x, y, Direction.RIGHT));
                }
                if(!clicked[Direction.BOTTOM.get()]){
                    legalMoveSet.add(getCanonicalMove(x, y, Direction.BOTTOM));
                }
                if(!clicked[Direction.LEFT.get()]){
                    legalMoveSet.add(getCanonicalMove(x, y, Direction.LEFT));
                }
            }
        }
        return new ArrayList<>(legalMoveSet);
    }

    // only the corner moves are left and top, all other moves get translated to bottom / right moves
    // this version gets used by the mcts, the other method is inside the move factory class
    public DBMove getCanonicalMove(int x, int y, Direction direction){
        if(direction == Direction.LEFT && x > 0){
            return new DBMove(x - 1, y, Direction.RIGHT);
        }
        else if(direction == Direction.TOP && y > 0){
            return new DBMove(x, y - 1, Direction.BOTTOM);
        }
        // already canonical version
        return new DBMove(x, y, direction);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getWinner() {
        if(blueCaptured >= minCapturesToWin){
            return Player.BLUE;
        }
        else if(redCaptured >= minCapturesToWin){
            return Player.RED;
        }
        // a draw is possible if the number of tiles is even
        else if(blueCaptured + redCaptured == width * height){
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
        int characterWidth = charactersPerTileWidth * width - (width - 1);
        int characterHeight = charactersPerTileHeight * height - (height - 1);

        char[][] characters = new char[characterHeight][characterWidth];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){

                // we want the tiles to overlap
                int startX = (charactersPerTileWidth - 1) * x;
                int startY = (charactersPerTileHeight - 1) * y;

                char[][] consoleRepresentation = board[y][x].getConsoleRepresentation();

                for(int i = 0; i < charactersPerTileHeight; i++){
                    System.arraycopy(consoleRepresentation[i], 0, characters[startY + i], startX, charactersPerTileWidth);
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

    @Override
    public String getTitle() {
        return "Dots and Boxes";
    }

    public String toString(){
        return getString();
    }

    public Player getPrevPlayer(){
        return prevPlayer;
    }
}
