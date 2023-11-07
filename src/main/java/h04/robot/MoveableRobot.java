package h04.robot;

import fopbot.Field;
import fopbot.Robot;
import h04.selection.FieldSelectionListener;
import h04.strategy.MoveStrategy;
import h04.strategy.MoveStrategyWithCounter;

public class MoveableRobot extends Robot implements FieldSelectionListener {

    private final MoveStrategy moveStrategy;

    public MoveableRobot(MoveStrategy moveStrategy) {
        super(0, 0);
        this.moveStrategy = moveStrategy;
    }

    @Override
    public void onFieldSelection(Field field) {
        moveStrategy.start(this, field);
        int currentMoveCount = moveStrategy instanceof MoveStrategyWithCounter mc ? mc.getMoveCount() : 1;
        for (int i = 0; i < currentMoveCount; i++) {
            this.turnLeft();
        }
    }
}
