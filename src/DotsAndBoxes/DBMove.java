package DotsAndBoxes;

import MCTS.Move;

public record DBMove(int x, int y, Direction direction) implements Move {

    @Override
    public String getString() {
        return String.format("%d,%d,%s", x, y, direction);
    }
}
