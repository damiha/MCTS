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

    public static TTTMove fromString(String s) {
        return new TTTMove(Integer.parseInt(s));
    }
}
