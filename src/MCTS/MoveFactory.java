package MCTS;

public interface MoveFactory {
    public Move createMoveFromString(String string) throws MoveFormatException;
}
