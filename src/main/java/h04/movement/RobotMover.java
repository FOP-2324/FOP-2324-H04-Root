package h04.movement;

import fopbot.Field;
import fopbot.Robot;
import h04.strategy.MoveStrategyWithCounter;
import h04.strategy.MoveStrategy;
import h04.selection.FieldSelectionListener;
import h04.selection.KeyboardFieldSelector;
import h04.selection.MouseFieldSelector;

public class RobotMover implements FieldSelectionListener {

    private Robot[] robots = new Robot[0];

    private final MoveStrategy moveStrategy;

    public RobotMover(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }


    public void addRobot(Robot robot) {
        var oldRobots = robots;
        this.robots = new Robot[oldRobots.length + 1];
        for (int i = 0; i < oldRobots.length; i++) {
            robots[i] = oldRobots[i];
        }
        robots[oldRobots.length] = robot;
    }

    @Override
    public void onFieldSelection(Field field) {
        for (Robot r : robots) {
            moveStrategy.start(r, field);
            int currentMoveCount = moveStrategy instanceof MoveStrategyWithCounter mc ? mc.getMoveCount() : 1;
            for (int i = 0; i < currentMoveCount; i++) {
                r.putCoin();
            }
        }
    }
}
