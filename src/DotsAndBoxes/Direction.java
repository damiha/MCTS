package DotsAndBoxes;

enum Direction {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3 );

    private final int val;

    Direction(int val){
        this.val = val;
    }

    int get(){
        return this.val;
    }
}
