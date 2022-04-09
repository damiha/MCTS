package DotsAndBoxes;

import MCTS.Move;

// record means, equals() compares type and all values
public record DBMove(int x, int y, Direction direction) implements Move {

    @Override
    public String getString() {
        return String.format("%d,%d,%s", x, y, direction);
    }
}
