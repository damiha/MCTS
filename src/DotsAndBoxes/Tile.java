package DotsAndBoxes;

import MCTS.Move;
import MCTS.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Tile{

    private final int x;
    private final int y;

    private boolean[] clicked;
    private Player capturedBy;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;

       clicked = new boolean[]{false, false, false, false};
    }

    public boolean isCaptured(){
        return clicked[Direction.TOP.get()] && clicked[Direction.RIGHT.get()] && clicked[Direction.BOTTOM.get()] && clicked[Direction.LEFT.get()];
    }

    public Tile getDeepCopy(){
        Tile deepCopy = new Tile(this.x, this.y);
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

    public char[][] getConsoleRepresentation(){

        char topChar = clicked[Direction.TOP.get()] ? '_' : '.';
        char rightChar = clicked[Direction.RIGHT.get()] ? '|' : '.';
        char bottomChar = clicked[Direction.BOTTOM.get()] ? '_' : '.';
        char leftChar = clicked[Direction.LEFT.get()] ? '|' : '.';

        char captureChar = capturedBy == Player.BLUE ? 'B' : (capturedBy == Player.RED ? 'R' : ' ');

        return new char[][]{
                {'x',topChar, topChar, topChar, 'x'},
                {leftChar,' ', captureChar, ' ', rightChar},
                {'x',bottomChar, bottomChar, bottomChar, 'x'},

        };
    }

    public boolean[] getClicked(){
        return clicked;
    }
}
