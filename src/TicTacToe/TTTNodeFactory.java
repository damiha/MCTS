package TicTacToe;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.NodeFactory;

public class TTTNodeFactory implements NodeFactory {

    @Override
    public Node createRootNode(Game game) {
        return new TTTNode(game);
    }

    @Override
    public Node createChildNode(Game game, Node parent, Move moveThatLedToPosition) {
        return new TTTNode(game, parent, moveThatLedToPosition);
    }
}
