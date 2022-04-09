package Connect4;

import MCTS.Game;
import MCTS.Move;
import MCTS.Node;
import MCTS.NodeFactory;

public class C4NodeFactory implements NodeFactory {

    @Override
    public Node createRootNode(Game game) {
        return new C4Node(game);
    }

    @Override
    public Node createChildNode(Game game, Node parent, Move moveThatLedToPosition) {
        return new C4Node(game, parent, moveThatLedToPosition);
    }
}
