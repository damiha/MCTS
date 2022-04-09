package TicTacToe;

import MCTS.Move;
import MCTS.MoveFactory;
import MCTS.MoveFormatException;

public class TTTMoveFactory implements MoveFactory {
    @Override
    public Move createMoveFromString(String string) throws MoveFormatException {
        try{
            return new TTTMove(Integer.parseInt(string));
        }catch(NumberFormatException e){
            throw new MoveFormatException("An integer is required!");
        }
    }
}
