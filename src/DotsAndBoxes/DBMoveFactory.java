package DotsAndBoxes;

import MCTS.Move;
import MCTS.MoveFactory;
import MCTS.MoveFormatException;

public class DBMoveFactory implements MoveFactory {
    @Override
    public Move createMoveFromString(String string) throws MoveFormatException {
        String[] components = string.split(",");

        int x = -1;
        int y = -1;

        try {
            x = Integer.parseInt(components[0]);
            y = Integer.parseInt(components[1]);
        }catch(NumberFormatException e){
            throw new MoveFormatException("x and y have to be integers.");
        }catch(ArrayIndexOutOfBoundsException e){
            throw new MoveFormatException("move requires three components: x,y,direction.");
        }

        Direction direction = switch (components[2]) {
            case "T" -> Direction.TOP;
            case "R" -> Direction.RIGHT;
            case "B" -> Direction.BOTTOM;
            case "L" -> Direction.LEFT;
            default -> null;
        };

        if(direction == null){
            throw new MoveFormatException("direction has to be either 'T', 'R', B' or 'L'");
        }
        return getCanonicalMove(new DBMove(x, y, direction));
    }

    public DBMove getCanonicalMove(DBMove move){
        if(move.direction() == Direction.LEFT && move.x() > 0){
            return new DBMove(move.x() - 1, move.y(), Direction.RIGHT);
        }
        else if(move.direction() == Direction.TOP && move.y() > 0){
            return new DBMove(move.x(), move.y() - 1, Direction.BOTTOM);
        }
        // bottom and right moves already canonical version
        return move;
    }
}
