package TicTacToe;

import MCTS.Move;

// record means, equals() compares type and all values
public record TTTMove(int num) implements Move {

    public int getNumber() {
        return this.num;
    }

    @Override
    public String getString() {
        return "" + num;
    }
}
