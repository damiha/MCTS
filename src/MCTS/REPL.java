package MCTS;

import java.util.Scanner;

public class REPL {

    private final Game game;
    private final NodeFactory nodeFactory;
    private final MoveFactory moveFactory;

    private final MCTSConfiguration mctsConfiguration;
    private final Scanner scanner;

    // TODO: add a settings / stats menu
    public REPL(Game game, NodeFactory nodeFactory, MoveFactory moveFactory, MCTSConfiguration mctsConfiguration) {
        this.game = game;
        this.nodeFactory = nodeFactory;
        this.moveFactory = moveFactory;

        this.scanner = new Scanner(System.in);
        this.mctsConfiguration = mctsConfiguration;

        System.out.println("----------   " + game.getTitle() + "   ----------");

        if(mctsConfiguration.getMode() == MCTSMode.FIXED_ITERATIONS){
            System.out.println("the MCTS uses a FIXED number of " + mctsConfiguration.getIterations() + " ITERATIONS\n");

        }else{
            System.out.println("the MCTS uses a FIXED TIME of " + mctsConfiguration.getSecondsToThink() + "s to think\n");
        }
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
            runMCTSandShowResult();
        }

        while (true) {

            readInMoveAndMakeIt();

            printGameBoard();


            if (game.isGameOver()) {
                printGameOver();
                return;
            }

            if(game.getPlayer() != humanPlayer) {
                runMCTSandShowResult();
            }
        }
    }

    // TODO: don't build up the whole Monte Carlo Tree again
    public void runMCTSandShowResult(){
        MonteCarloTreeSearch monteCarloTreeSearch = new MonteCarloTreeSearch(game, nodeFactory, mctsConfiguration);
        Move bestMove = monteCarloTreeSearch.getBestMove();
        monteCarloTreeSearch.showDistribution();
        System.out.println("best move according to MCTS: " + bestMove.getString());
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
