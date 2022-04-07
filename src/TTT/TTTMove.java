package TTT;

import MCTS.Move;

public record TTTMove(int num) implements Move {

    public int getNumber() {
        return this.num;
    }

    @Override
    public String getString() {
        return "" + num;
    }
}
