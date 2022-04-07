package MCTS;

import java.util.Scanner;

public class REPL {

    private final Game game;
    private final NodeFactory nodeFactory;
    private final MoveFactory moveFactory;
    private final int iterations;
    private MonteCarloTreeSearch monteCarloTreeSearch;

    private final Scanner scanner;

    // TODO: make another monte carlo constructor that takes max calculation time as a parameter
    // TODO: add a settings / stats menu

    public REPL(Game game, NodeFactory nodeFactory, MoveFactory moveFactory, int iterations) {
        this.game = game;
        this.nodeFactory = nodeFactory;
        this.moveFactory = moveFactory;
        this.iterations = iterations;
        this.monteCarloTreeSearch = new MonteCarloTreeSearch(game, nodeFactory);

        this.scanner = new Scanner(System.in);
        System.out.println("----------   " + game.getTitle() + "   ----------\n");

    }

    public Player readInPlayer() {
        System.out.println("Do you want to be BLUE (starting player) or RED ?");
        System.out.print("Type B for BLUE / R for RED: ");

        char answer = scanner.nextLine().charAt(0);
        if(answer == 'B') {
            System.out.println("You're BLUE now.\n");
            return Player.BLUE;
        }
        System.out.println("You're RED now.\n");
        return Player.RED;
    }

    public void runREPL() {

        Player humanPlayer = readInPlayer();
        // print game on start, we need to know what's up
        printGameBoard();

        if(game.getPlayer() != humanPlayer) {
            Move bestMove = monteCarloTreeSearch.getBestMove(iterations);
            monteCarloTreeSearch.showDistribution();
            System.out.println("best move according to MCTS: " + bestMove.getString());
        }

        while (true) {

            readInMoveAndMakeIt();

            printGameBoard();


            if (game.isGameOver()) {
                printGameOver();
                return;
            }

            // TODO: don't build up the whole Monte Carlo Tree again
            if(game.getPlayer() != humanPlayer) {
                monteCarloTreeSearch = new MonteCarloTreeSearch(game, nodeFactory);
                Move bestMove = monteCarloTreeSearch.getBestMove(iterations);
                monteCarloTreeSearch.showDistribution();
                System.out.println("best move according to MCTS: " + bestMove.getString());
            }
        }
    }

    // just a quick fix, don't judge me
    public Move readInCLAndGetMove(){
        while(true){
            System.out.print("move: ");
            String command = scanner.nextLine();

            Move nextMove = null;
            try {
                nextMove = moveFactory.createMoveFromString(command);
                return nextMove;

            } catch (MoveFormatException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // bad codestyle again :|
    public void readInMoveAndMakeIt(){

        while(true){

            Move move = readInCLAndGetMove();

            try {
                game.makeUserMove(move);
                return;

            } catch (MoveFormatException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    public void printGameOver() {

        Player winner = game.getWinner();

        if (winner != Player.NOBODY_DRAW) {
            System.out.println("Player " + winner + " has won the game!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    public void printGameBoard() {
        System.out.println(game.getString());
    }
}
