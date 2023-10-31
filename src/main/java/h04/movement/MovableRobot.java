package h04.movement;

import fopbot.Direction;
import fopbot.Field;
import fopbot.Robot;
import h04.strategy.MoveStrategy;
import h04.selection.FieldSelectionListener;
import h04.selection.KeyboardFieldSelector;
import h04.selection.MouseFieldSelector;

public class MovableRobot extends Robot implements FieldSelectionListener {

    private final MoveStrategy moveStrategy;

    public MovableRobot(MoveStrategy moveStrategy) {
        super(0, 0);
        this.moveStrategy = moveStrategy;
    }

    @Override
    public void onFieldSelection(Field field) {
        moveStrategy.start(this, field);
    }
}
