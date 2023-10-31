package h04.strategy;

import fopbot.Field;
import fopbot.Robot;

import static fopbot.Direction.*;

public class MoveByWalk implements MoveStrategy, MoveStrategyWithCounter {

    private int moveCount;

    @Override
    public void start(Robot r, Field target) {
        moveCount = 0;
        var tX = target.getX();
        var tY = target.getY();
        for (int i = 0; i < 4; i++) {
            var rD = r.getDirection();
            while (rD == UP && r.getY() < tY || rD == RIGHT && r.getX() < tX || rD == DOWN && r.getY() > tY || rD == LEFT && r.getX() > tX) {
                r.move();
                moveCount++;
            }
            if (r.getX() == tX && r.getY() == tY) {
                break;
            }
            r.turnLeft();
        }

    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }
}
