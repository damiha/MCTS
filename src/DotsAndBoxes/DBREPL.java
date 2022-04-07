package DotsAndBoxes;

import MCTS.Game;

public class DBREPL {

    public static void main(String[] args) {
        Game game = new DBGame();

        System.out.println(game.getString());

        game.move(new DBMove(0,0,Direction.RIGHT));

        System.out.println(game.getString());
    }
}
