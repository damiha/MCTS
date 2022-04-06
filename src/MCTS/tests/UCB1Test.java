package MCTS.tests;

public class UCB1Test {

    public static void main(String[] args) {
        // THE UCB1-Function works as expected
        System.out.println(getUCB1(1, 1, 7));
        System.out.println(getUCB1(0, 1, 7));
        System.out.println(getUCB1(2, 2, 10));
    }

    public static double getUCB1(double nwins, double ngames, double parentvisits){
        if(ngames == 0.0){
            return Double.POSITIVE_INFINITY;
        }
        // parent wants to see the opponent (child) lose
        double exploitationTerm = nwins / ngames;
        double explorationTerm = Math.sqrt(Math.log(parentvisits) / ngames);

        double C = Math.sqrt(2.0);
        return exploitationTerm + C * explorationTerm;
    }
}
