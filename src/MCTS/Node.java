package MCTS;

import java.util.ArrayList;
import java.util.Collections;

public class Node {

    Node parent;
    Game game;
    Move move;

    ArrayList<Node> children;

    double nwins;
    double ndraws;
    double nlosses;
    double ngames;


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

    public void expandChildren(){
        ArrayList<Move> allLegalMoves = game.getAllLegalMoves();

        for(Move legalMove : allLegalMoves){

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(legalMove);
            children.add(new Node(gameAfterMove, this, legalMove));
        }
    }

    public Move getMove(){
        return move;
    }

    public boolean isLeaf(){
        return children.size() == 0;
    }

    // parent != null, since only called in child nodes
    public double getUCB1(){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        double exploitationTerm = (1 * nwins  + 1 * ndraws - 1 * nlosses) / ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = 2.0;
        return exploitationTerm + C * explorationTerm;
    }

    public double getWinRate(){
        if(ngames == 0.0)
            return 0.0;
        return nwins / ngames;
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
                    current.nlosses += 1;
                }
                else{
                    current.nwins += 1;
                }
            }
            current = current.parent;
        }
    }

    Player rollout(){

        // maybe this is already a terminal state ?
        if(game.whoWon() != Player.NOBODY_IN_PROGRESS){
            return game.whoWon();
        }

        Game tempGame = game.getDeepCopy();
        ArrayList<Move> movesFromHere = tempGame.getAllLegalMoves();
        Collections.shuffle(movesFromHere);

        for(Move move : movesFromHere){

            tempGame.move(move);

            // did we win after making the move
            if(tempGame.whoWon() != Player.NOBODY_IN_PROGRESS){
                return tempGame.whoWon();
            }
        }
        throw new RuntimeException("Simulation finished without a result. This should never happen!");
    }

    public void performRollout(){
        Player winnerOfRollout = rollout();
        propagate(winnerOfRollout);
    }
}
