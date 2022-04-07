package DotsAndBoxes;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.NodeFactory;

public class DBNodeFactory implements NodeFactory {
    @Override
    public Node createRootNode(Game game) {
        return new DBNode(game);
    }

    @Override
    public Node createChildNode(Game game, Node parent, Move moveThatLedToPosition) {
        return new DBNode(game, parent, moveThatLedToPosition);
    }
}
