package MCTS;

public class MonteCarloTreeSearch {

    Node root;

    public MonteCarloTreeSearch(Game game, NodeFactory nodeFactory){
        root = nodeFactory.createRootNode(game);
    }

    // CORRECT
    public Move getBestMove(int iterations){

        for(int i = 0; i < iterations; i++){
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
        for(Node child : root.children){
            System.out.println(child.move.getString() + " : [VAL: " + child.value + " VIS: " + child.ngames + "] - ");
        }
        System.out.println();
    }

    public Node select(){
        // take path along the tree that maximizes the ucb1 value
        Node current = root;

        // 1. selection
        while(current.untriedMoves.size() == 0 && current.children.size() != 0){
            current = current.uctSelectChild();
        }
        return current;
    }

    public void runStep(){

        Node current = select();
        current = current.expand();

        double reward = current.rollout();
        current.propagate(reward);
    }
}
