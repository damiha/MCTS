package C4;

import MCTS.Move;

public record C4Move(int num) implements Move {
    @Override
    public String getString() {
        return "" + num;
    }
}
