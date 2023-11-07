package h04;

import fopbot.Robot;
import fopbot.RobotFamily;
import fopbot.World;
import h04.robot.MoveableRobot;
import h04.robot.RobotMover;
import h04.selection.KeyboardFieldSelector;
import h04.selection.MouseFieldSelector;
import h04.strategy.MoveByTeleport;
import h04.strategy.MoveByWalk;


/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        World.setVisible(true);
        main01();
        main02();
    }

    public static void main01() {
        MouseFieldSelector mfs = new MouseFieldSelector();
        MoveByWalk mbw = new MoveByWalk();
        RobotMover rm = new RobotMover(mbw);
        mfs.setFieldSelectionListener(rm);
        Robot r1 = new Robot(1, 3, RobotFamily.SQUARE_RED);
        Robot r2 = new Robot(4, 2, RobotFamily.SQUARE_GREEN);
        Robot r3 = new Robot(6, 9, RobotFamily.SQUARE_BLUE);
        rm.addRobot(r1);
        rm.addRobot(r2);
        rm.addRobot(r3);
    }

    public static void main02() {
        KeyboardFieldSelector kfs = new KeyboardFieldSelector();
        MoveByTeleport mbt = new MoveByTeleport();
        MoveableRobot mr = new MoveableRobot(mbt);
        kfs.setFieldSelectionListener(mr);
    }
}
