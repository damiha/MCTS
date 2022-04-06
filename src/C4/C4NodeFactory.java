package C4;

import MCTS.Game;
import MCTS.Node;
import MCTS.NodeFactory;
import TTT.TTTNode;

public class C4NodeFactory implements NodeFactory {

    @Override
    public Node createRootNode(Game game) {
        return new C4Node(game);
    }
}
