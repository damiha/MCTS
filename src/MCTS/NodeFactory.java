package MCTS;

public interface NodeFactory {

    Node createRootNode(Game game);

    Node createChildNode(Game game, Node parent, Move moveThatLedToPosition);
}
