import java.util.ArrayList;
import java.util.Collections;

public class Node {

    Node parent;
    Game game;
    int move;

    ArrayList<Node> children;

    double nwins;
    double ngames;

    // create root node
    public Node(Game game){
        this.game = game;
        this.children = new ArrayList<>();

        nwins = 0.0;
        ngames = 0.0;
    }
    // create child nodes
    public Node(Game game, Node parent, int move){
        this(game);
        this.parent = parent;
        this.move = move;
    }

    public void expandChildren(){
        ArrayList<Integer> allLegalMoves = game.getAllLegalMoves();

        for(Integer legalMove : allLegalMoves){

            Game gameAfterMove = game.getDeepCopy();
            gameAfterMove.move(legalMove);
            children.add(new Node(gameAfterMove, this, legalMove));
        }
    }

    public boolean isLeaf(){
        return children.size() == 0;
    }

    // parent != null, since only called in child nodes
    public double getUCB1(){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        double exploitationTerm = nwins / (double) ngames;
        double explorationTerm = Math.sqrt(Math.log(parent.ngames) / ngames);

        double C = 2.0;
        return exploitationTerm + C * explorationTerm;
    }

    public double getWinRate(){
        if(ngames == 0.0)
            return 0.0;
        return nwins / ngames;
    }

    public void propagate(char winnerOfRollout){

        Node current = this;

        while(current != null){
            current.ngames += 1.0;

            if(winnerOfRollout == 'D'){
                current.nwins += 0.5;
            }
            else{
                current.nwins += (winnerOfRollout != current.game.player ? 1.0 : 0.0);
            }
            current = current.parent;
        }
    }

    char rollout(){

        // maybe this is already a terminal state ?
        if(game.whoWon() != '-'){
            return game.whoWon();
        }

        Game tempGame = game.getDeepCopy();
        ArrayList<Integer> movesFromHere = tempGame.getAllLegalMoves();
        Collections.shuffle(movesFromHere);

        for(Integer move : movesFromHere){

            tempGame.move(move);

            // did we win after making the move
            if(tempGame.whoWon() != '-'){
                return tempGame.whoWon();
            }
        }
        throw new RuntimeException("Simulation finished without a result. This should never happen!");
    }

    public void performRollout(){
        char winnerOfRollout = rollout();
        propagate(winnerOfRollout);
    }
}
