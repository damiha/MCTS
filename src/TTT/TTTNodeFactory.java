package TTT;

import MCTS.Game;
import MCTS.Node;
import MCTS.NodeFactory;
import TTT.TTTNode;

public class TTTNodeFactory implements NodeFactory {

    @Override
    public Node createRootNode(Game game) {
        return new TTTNode(game);
    }
}
