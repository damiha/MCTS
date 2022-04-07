package DotsAndBoxes;

import MCTS.Move;

public record DBMove(int x, int y, Direction direction) implements Move {

    @Override
    public String getString() {
        return String.format("%d,%d,%s", x, y, direction);
    }

    public static DBMove fromString(String s){
        String[] components = s.split(",");
        int x = Integer.parseInt(components[0]);
        int y = Integer.parseInt(components[1]);

        Direction direction = switch (components[2]) {
            case "T" -> Direction.TOP;
            case "R" -> Direction.RIGHT;
            case "B" -> Direction.BOTTOM;
            case "L" -> Direction.LEFT;
            default -> null;
        };

        return new DBMove(x, y, direction);
    }
}
