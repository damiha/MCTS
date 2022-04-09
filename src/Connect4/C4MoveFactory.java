package Connect4;

import MCTS.Move;
import MCTS.MoveFactory;
import MCTS.MoveFormatException;

public class C4MoveFactory implements MoveFactory {
    @Override
    public Move createMoveFromString(String string) throws MoveFormatException {
        try{
            return new C4Move(Integer.parseInt(string));
        }catch(NumberFormatException e){
            throw new MoveFormatException("An integer between 1 and 7 is required!");
        }
    }
}
