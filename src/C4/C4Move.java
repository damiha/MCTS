package C4;

import MCTS.Move;

// record means, equals() compares type and all values
public record C4Move(int num) implements Move {
    @Override
    public String getString() {
        return "" + num;
    }
}
