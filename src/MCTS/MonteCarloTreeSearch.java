package MCTS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MonteCarloTreeSearch {

    private Node root;
    private final Game game;
    private final NodeFactory nodeFactory;
    private final MCTSConfiguration mctsConfiguration;

    // not settings but stats (get updated during the search)
    private int rolloutsPerformed;
    private int nThreads = 1;

    // TODO: what other types of threadpools are there ? Do they offer a better performance ?
    // TODO: whats the ideal thread pool size ?
    private ThreadPoolExecutor threadPoolExecutor;

    public MonteCarloTreeSearch(Game game, NodeFactory nodeFactory, MCTSConfiguration mctsConfiguration){
        root = nodeFactory.createRootNode(game);
        this.game = game;
        this.nodeFactory = nodeFactory;
        this.mctsConfiguration = mctsConfiguration;

        rolloutsPerformed = 0;

        // only spin up thread pool when needed (has some considerable overhead)
        if(mctsConfiguration.isLeafParallelization()){
            nThreads = 16 * Runtime.getRuntime().availableProcessors();
            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
        }
    }

    // don't rebuild whole tree, but reuse old
    // call this function every move to keep everything up to date
    public void updateTreeFrom(Move moveTaken){

        for(Node child : root.getChildren()){
            if(child.getMoveThatLedToPosition().equals(moveTaken)){
                root = child;
                return;
            }
        }
        root = nodeFactory.createRootNode(game);
    }
    public Move getBestMove(){

        Move bestMove;
        rolloutsPerformed = 0;

        if(mctsConfiguration.getMode() == MCTSMode.FIXED_ITERATIONS){
            bestMove = getBestMove(mctsConfiguration.getIterations());
        }
        else{
            bestMove = getBestMove(mctsConfiguration.getSecondsToThink());
        }
        return bestMove;
    }

    public Move getBestMove(int iterations){

        rolloutsPerformed = 0;

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

    public void shutDownThreadPool(){
        if(threadPoolExecutor != null){
            threadPoolExecutor.shutdownNow();
        }
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

        String threadString = nThreads == 1 ? "1 thread." : (nThreads + " threads.");
        System.out.println(rolloutsPerformed + " rollouts performed by " + threadString);
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
            multiThreadedRolloutAndBackProp(current);
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

    public void multiThreadedRolloutAndBackProp(Node current){

        // using the threadpool for one rollout is just not worth the time!
        if(mctsConfiguration.getRolloutsPerLeaf() == 1){
            Player winnerOfRollout = current.rollout();
            current.singlePropagate(winnerOfRollout);
        }
        else{

            ArrayList<Future<Player>> futureWinners = new ArrayList<Future<Player>>(mctsConfiguration.getRolloutsPerLeaf());
            Player[] winners = new Player[mctsConfiguration.getRolloutsPerLeaf()];

            for(int i = 0; i < mctsConfiguration.getRolloutsPerLeaf(); i++){
                // submit a future (runnable that has a return value)
                futureWinners.add(threadPoolExecutor.submit(current::rollout));
            }

            // collect the finished work
            for(int i = 0; i < mctsConfiguration.getRolloutsPerLeaf(); i++){
                try {
                    winners[i] = futureWinners.get(i).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            current.multiPropagate(winners);
        }
    }
}
