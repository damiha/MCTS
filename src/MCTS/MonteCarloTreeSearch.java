package MCTS;

public class MonteCarloTreeSearch {

    Node root;

    public MonteCarloTreeSearch(Game game, NodeFactory nodeFactory){
        root = nodeFactory.createRootNode(game);
        root.expandChildren();
    }

    public Move getBestMove(int iterations){

        for(int i = 0; i < iterations; i++){
            //showDistribution();
            runStep();
        }
        double mostVisited = 0;
        Move bestMove = null;
        // minimize opponents win rate
        for(Node child : root.children){
            if(child.ngames > mostVisited){
                mostVisited = child.ngames;
                bestMove = child.move;
            }
        }
        return bestMove;
    }

    public void showDistribution(){
        System.out.print("perspective of root: ");
        for(Node child : root.children){
            System.out.print(child.move.getString() + " : [W: " + child.nlosses + ", D: " + child.ndraws + ", L: " + child.nwins + "] - ");
        }
        System.out.println();
    }

    public void runStep(){

        // take path along the tree that maximizes the ucb1 value
        Node current = root;

        // showDistribution();

        // 1. selection
        while(!current.isLeaf()){

            double bestUCB1 = Double.NEGATIVE_INFINITY;
            Node bestNode = null;

            for(Node child : current.children){
                if(child.getUCB1() > bestUCB1){
                    bestUCB1 = child.getUCB1();
                    bestNode = child;
                }
            }
            current = bestNode;
        }

        if(current.ngames == 0) {
            current.performRollout();
        }
        else{
            current.expandChildren();

            if(!current.isLeaf()){
                current.children.get(0).performRollout();
            }
            else{
                // terminal state: just do a rollout
                current.performRollout();
            }
        }
    }
}
