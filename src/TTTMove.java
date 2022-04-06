import MCTS.Move;

public class TTTMove implements Move {

    private final int num;

    public TTTMove(int num){
        this.num = num;
    }

    public int getNumber(){
        return this.num;
    }

    @Override
    public String getString() {
        return "" + num;
    }

    public static TTTMove fromString(String s){
        return new TTTMove(Integer.parseInt(s));
    }
}
