public class MonteCarloTreeSearch {

    Node root;

    public MonteCarloTreeSearch(Game game){
        root = new Node(game.getDeepCopy());
        root.expandChildren();
    }

    public int getBestMove(int iterations){

        for(int i = 0; i < iterations; i++){
            runStep();
        }
        double bestWinRate = 0;
        int bestMove = 0;
        // minimize opponents win rate
        for(Node child : root.children){
            if(child.getWinRate() > bestWinRate){
                bestWinRate = child.getWinRate();
                bestMove = child.move;
            }
        }
        return bestMove;
    }

    public void showDistribution(){
        for(Node child : root.children){
            System.out.print(child.move + " : [" + child.nwins + "/" + child.ngames + "] - ");
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
        }
    }
}
