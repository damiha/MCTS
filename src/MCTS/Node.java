package MCTS;

import java.util.ArrayList;
import java.util.Random;

public abstract class Node {

    // TODO: bad code style, implement getters and setters later on
    private Node parent;
    private final Game game;

    private Move moveThatLedToPosition;
    private final Player playerWhoTookMove;

    private final ArrayList<Node> children;
    private final ArrayList<Move> movesToTry;

    private int value;
    private int visits;

    // create root node
    public Node(Game game){

        this.game = game;
        this.playerWhoTookMove = game.getPrevPlayer();

        // if the game is over, there are no moves left to try => empty array list
        this.movesToTry = game.getWinner() == Player.NOBODY_IN_PROGRESS ? game.getAllLegalMoves() : new ArrayList<>();
        this.children = new ArrayList<>();

        this.value = 0;
        this.visits = 0;
    }
    // create child nodes
    public Node(Game game, Node parent, Move move){
        this(game);
        this.parent = parent;
        this.moveThatLedToPosition = move;
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

    public Node expand(NodeFactory nodeFactory){

        if(!isFullyExpanded()){

            Move randomMove = selectRandomMove();

            Game gameAfterMove = getGame().getDeepCopy();
            gameAfterMove.move(randomMove);

            getMovesToTry().remove(randomMove);
            Node newNode = nodeFactory.createChildNode(gameAfterMove, this, randomMove);
            getChildren().add(newNode);

            return newNode;
        }
        else{
            return this;
        }
    }

    public abstract Player rollout();

    public double getUCB1(){
        if(visits == 0){
            return Double.POSITIVE_INFINITY;
        }
        // parent wants to see the opponent (child) lose
        double exploitationTerm = (double) value / (double) visits;
        double explorationTerm = Math.sqrt(Math.log(parent.visits) / (double) visits);

        // theoretical exploration parameter (multi-armed bandit problem)
        double C = Math.sqrt(2.0);
        return exploitationTerm + C * explorationTerm;
    }

    public void propagate(Player winnerOfRollout){

        Node current = this;

        while(current != null){
            current.visits += 1.0;

            if(winnerOfRollout == current.playerWhoTookMove){
                current.value += 1;
            }
            else if(winnerOfRollout == Player.getOpponent(current.playerWhoTookMove)){
                current.value -= 1;
            }
            // nothing happens when the game is drawn!

            current = current.parent;
        }
    }

    public Move selectRandomMove(){
        // TODO: use one global random number generator
        int randomIndex = (new Random()).nextInt(movesToTry.size());
        return movesToTry.get(randomIndex);
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public int getVisits(){
        return visits;
    }

    public int getValue(){
        return value;
    }

    public Move getMoveThatLedToPosition(){
        return moveThatLedToPosition;
    }

    public ArrayList<Move> getMovesToTry(){
        return movesToTry;
    }

    public Game getGame(){
        return game;
    }

    public boolean isFullyExpanded(){
        return movesToTry.isEmpty();
    }

    public boolean isNotLeafNode(){
        return !children.isEmpty();
    }
}
