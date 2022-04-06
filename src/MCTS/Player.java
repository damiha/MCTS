package MCTS;

public enum Player {
    BLUE,
    RED,
    NOBODY_IN_PROGRESS,
    NOBODY_DRAW;

    private Player opponent;

    static {
        BLUE.opponent = Player.RED;
        RED.opponent = Player.BLUE;

        NOBODY_IN_PROGRESS.opponent = Player.NOBODY_IN_PROGRESS;
        NOBODY_DRAW.opponent = Player.NOBODY_DRAW;
    }

    public Player getOpponent(){
        return opponent;
    }
}
