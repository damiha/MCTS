package MCTS;

import java.util.ArrayList;

public abstract class Node {

    // TODO: bad code style, implement getters and setters later on
    public Node parent;
    public Game game;
    public Move move;

    public ArrayList<Node> children;
    public ArrayList<Move> untriedMoves;

    public double value;
    public double ngames;

    // create root node
    public Node(Game game){
        this.game = game;

        this.untriedMoves = game.getAllLegalMoves();
        this.children = new ArrayList<>();

        value = 0.0;
        ngames = 0.0;
    }
    // create child nodes
    public Node(Game game, Node parent, Move move){
        this(game);
        this.parent = parent;
        this.move = move;
    }

    public Node uctSelectChild(){

        double maxScore = Double.NEGATIVE_INFINITY;
        Node bestChild = null;

        for(Node child : children){
            double score = child.getUCB1();

            if(score > maxScore){
                maxScore = score;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public abstract Node expand();

    public abstract double rollout();

    public abstract double getUCB1();

    public void propagate(double reward){

        Node current = this;

        while(current != null){
            current.ngames += 1.0;
            current.value += reward;
            reward = -reward;
            current = current.parent;
        }
    }
}
