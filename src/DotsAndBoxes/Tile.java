package DotsAndBoxes;

import MCTS.Player;

import java.util.Arrays;

public class Tile{

    private int x;
    private int y;
    private Position position;

    private boolean[] clicked;
    private Player capturedBy;

    // TODO: optimize this, the class only needs 7 or so arrays as bitmasks, saves memory for each game
    // you don't need to copy this as it is constructed based on the position
    private boolean[] clickable;

    public Tile(int x, int y, Position position){
        this.x = x;
        this.y = y;
        this.position = position;

       clicked = new boolean[]{false, false, false, false};

        switch (position) {
            case TOP_LEFT -> clickable = new boolean[]{false, true, true, false};
            case TOP_CENTER -> clickable = new boolean[]{false, true, true, true};
            case TOP_RIGHT -> clickable = new boolean[]{false, false, true, true};
            case LEFT_CENTER -> clickable = new boolean[]{true, true, true, false};
            case RIGHT_CENTER -> clickable = new boolean[]{true, false, true, true};
            case BOTTOM_LEFT -> clickable = new boolean[]{true, true, false, false};
            case BOTTOM_CENTER -> clickable = new boolean[]{true, true, false, true};
            case BOTTOM_RIGHT -> clickable = new boolean[]{true, false, false, true};
            case CENTER -> clickable = new boolean[]{true, true, true, true};
        }
    }

    public boolean isCaptured(){
        return (!clickable[Direction.TOP.get()] || clicked[Direction.TOP.get()]) &&
                (!clickable[Direction.RIGHT.get()] || clicked[Direction.RIGHT.get()]) &&
                (!clickable[Direction.BOTTOM.get()] || clicked[Direction.BOTTOM.get()]) &&
                (!clickable[Direction.LEFT.get()] || clicked[Direction.LEFT.get()]);
    }

    public Tile getDeepCopy(){
        Tile deepCopy = new Tile(this.x, this.y, this.position);
        deepCopy.clicked = Arrays.copyOf(this.clicked, 4);
        deepCopy.capturedBy = this.capturedBy;

        return deepCopy;
    }

    public void clickAt(Direction direction){
        clicked[direction.get()] = true;
    }

    public void setCapturedBy(Player player){
        capturedBy = player;
    }

    public boolean isRightPossible(){
        return !clicked[Direction.RIGHT.get()] && clickable[Direction.RIGHT.get()];
    }

    public boolean isBottomPossible(){
        return !clicked[Direction.BOTTOM.get()] && clickable[Direction.BOTTOM.get()];
    }

    public char[][] getConsoleRepresentation(){

        char topChar = !clickable[Direction.TOP.get()] || clicked[Direction.TOP.get()] ? '_' : '.';
        char rightChar = !clickable[Direction.RIGHT.get()] || clicked[Direction.RIGHT.get()] ? '|' : '.';
        char bottomChar = !clickable[Direction.BOTTOM.get()] || clicked[Direction.BOTTOM.get()] ? '_' : '.';
        char leftChar = !clickable[Direction.LEFT.get()] || clicked[Direction.LEFT.get()] ? '|' : '.';

        char captureChar = capturedBy == Player.BLUE ? 'B' : (capturedBy == Player.RED ? 'R' : ' ');

        char[][] consoleRepresentation = {
                {'x',topChar, topChar, topChar, 'x'},
                {leftChar,' ', captureChar, ' ', rightChar},
                {'x',bottomChar, bottomChar, bottomChar, 'x'},

        };

        return consoleRepresentation;
    }
}
