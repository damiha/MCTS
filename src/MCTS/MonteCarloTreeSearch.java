package MCTS;

import java.util.Comparator;

public class MonteCarloTreeSearch {

    private final Node root;
    private final NodeFactory nodeFactory;
    private final MCTSConfiguration mctsConfiguration;

    // not settings but stats (get updated during the search)
    private int rolloutsPerformed;

    public MonteCarloTreeSearch(Game game, NodeFactory nodeFactory, MCTSConfiguration mctsConfiguration){
        root = nodeFactory.createRootNode(game);
        this.nodeFactory = nodeFactory;
        this.mctsConfiguration = mctsConfiguration;

        rolloutsPerformed = 0;
    }
    public Move getBestMove(){
        if(mctsConfiguration.getMode() == MCTSMode.FIXED_ITERATIONS){
            return getBestMove(mctsConfiguration.getIterations());
        }
        else{
            return getBestMove(mctsConfiguration.getSecondsToThink());
        }
    }

    public Move getBestMove(int iterations){

        for(int i = 0; i < iterations; i++){
            runStep();
        }
        rolloutsPerformed = iterations * mctsConfiguration.getRolloutsPerLeaf();
        return selectBestMove();
    }

    public Move getBestMove(double secondsToThink){

        long startTime = System.currentTimeMillis();

        while(((double)(System.currentTimeMillis() - startTime) / 1000) < secondsToThink){
            runStep();
            rolloutsPerformed += mctsConfiguration.getRolloutsPerLeaf();
        }
        return selectBestMove();
    }

    public Move selectBestMove(){
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


        System.out.println("Rollouts performed: " + rolloutsPerformed);
        // sort by visits => best moves up top
        // sorting is permanent (destroys randomness that was created during expansion) but tree gets rebuild for every move
        root.getChildren().sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                // we want the reverse order (elements with most visits should come first)
                return Integer.compare(o2.getVisits(), o1.getVisits());
            }
        });

        //  limit to the 5 most promising moves
        for(int i = 0; i < root.getChildren().size() && i < 5; i++){

            Node child = root.getChildren().get(i);
            String playerString = "" + child.getPlayerWhoTookMove();
            String moveString = child.getMoveThatLedToPosition().getString();
            String statString = String.format("%d. move: %s, evaluation: [val: %d, vis: %d, ucb1: %.4f] for Player %s", (i+1), moveString, child.getValue(), child.getVisits(), child.getUCB1(), playerString);
            System.out.println(statString);
        }
        System.out.println("...\n");
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

        if(mctsConfiguration.isLeafParallelization()){
            // TODO:
        }else{
            singleThreadedRolloutAndBackProp(current);
        }
    }

    public void singleThreadedRolloutAndBackProp(Node current){
        if(mctsConfiguration.getRolloutsPerLeaf() == 1){
            Player winnerOfRollout = current.rollout();
            current.singlePropagate(winnerOfRollout);
        }
        else{
            Player[] winners = new Player[mctsConfiguration.getRolloutsPerLeaf()];

            for(int i = 0; i < mctsConfiguration.getRolloutsPerLeaf(); i++){
                winners[i] = current.rollout();
            }
            current.multiPropagate(winners);
        }
    }
}
