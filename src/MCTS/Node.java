package MCTS;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Node {

    // TODO: bad code style, implement getters and setters later on
    public Node parent;
    public Game game;
    public Move move;

    public ArrayList<Node> children;

    public double nwins;
    public double ndraws;
    public double nlosses;
    public double ngames;

    // create root node
    public Node(Game game){
        this.game = game;
        this.children = new ArrayList<>();

        nwins = 0.0;
        ndraws = 0.0;
        nlosses = 0.0;
        ngames = 0.0;
    }
    // create child nodes
    public Node(Game game, Node parent, Move move){
        this(game);
        this.parent = parent;
        this.move = move;
    }

    public abstract void expandChildren();

    public abstract Player rollout();

    public abstract double getUCB1();

    public boolean isLeaf(){
        return children.size() == 0;
    }

    public void propagate(Player winnerOfRollout){

        Node current = this;

        while(current != null){
            current.ngames += 1.0;

            if(winnerOfRollout == Player.NOBODY_DRAW){
                current.ndraws += 1.0;
            }
            else{
                if(winnerOfRollout == current.game.getPlayer()){
                    current.nwins += 1;
                }
                else{
                    current.nlosses += 1;
                }
            }
            current = current.parent;
        }
    }

    public void performRollout(){
        Player winnerOfRollout = rollout();
        propagate(winnerOfRollout);
    }
}
