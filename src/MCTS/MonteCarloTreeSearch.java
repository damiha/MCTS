package MCTS;

import java.util.Comparator;

public class MonteCarloTreeSearch {

    private final Node root;
    private final NodeFactory nodeFactory;

    public MonteCarloTreeSearch(Game game, NodeFactory nodeFactory){
        root = nodeFactory.createRootNode(game);
        this.nodeFactory = nodeFactory;
    }

    public Move getBestMove(int iterations){

        // TODO: don't use iterations, make it time based
        for(int i = 0; i < iterations; i++){
            runStep();
        }
        double mostVisits = 0;
        Move bestMove = null;
        // minimize opponents win rate
        for(Node child : root.getChildren()){

            int childVisits = child.getVisits();

            if(childVisits > mostVisits){
                mostVisits = childVisits;
                bestMove = child.getMoveThatLedToPosition();
            }
        }
        return bestMove;
    }

    public void showDistribution(){

        // sort by visits => best moves up top
        // sorting is permanent (destroys randomness that was created during expansion) but tree gets rebuild for every move
        root.getChildren().sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                // we want the reverse order (elements with most visits should come first)
                return Integer.compare(o2.getVisits(), o1.getVisits());
            }
        });

        for(Node child : root.getChildren()){

            String playerString = "" + child.getPlayerWhoTookMove();
            String moveString = child.getMoveThatLedToPosition().getString();
            String statString = String.format("move: %s, evaluation: [val: %d, vis: %d, ucb1: %.4f] for Player %s", moveString, child.getValue(), child.getVisits(), child.getUCB1(), playerString);
            System.out.println(statString);
        }
        System.out.println();
    }

    public Node select(){
        // take path along the tree that maximizes the ucb1 value
        Node current = root;

        // 1. selection
        while(current.isFullyExpanded() && current.isNotLeafNode()){
            current = current.uctSelectChild();
        }

        return current;
    }

    public void runStep(){

        Node current = select();
        current = current.expand(nodeFactory);

        Player winnerOfRollout = current.rollout();
        current.propagate(winnerOfRollout);
    }
}
