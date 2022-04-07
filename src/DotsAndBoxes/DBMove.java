package DotsAndBoxes;

import MCTS.Move;

enum Direction{
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

public record DBMove(int x, int y, Direction direction) implements Move {

    @Override
    public String getString() {
        return null;
    }

    public DBMove fromString(String s){
        String[] components = s.split(",");
        int x = Integer.parseInt(components[0]);
        int y = Integer.parseInt(components[1]);
        Direction direction = components[2].equals("R") ? Direction.RIGHT : Direction.BOTTOM;

        return new DBMove(x, y, direction);
    }
}
