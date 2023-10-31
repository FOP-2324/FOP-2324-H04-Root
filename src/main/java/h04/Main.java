package h04;

import fopbot.World;
import h04.movement.MovableRobot;
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
        System.out.println("Hello World!");

        World.setVisible(true);
        World.setDelay(50);
        new MovableRobot(new MoveByWalk());
//        Robot robot = new Robot(0, 0, Direction.UP, 1000, RobotFamily.SQUARE_WHITE);
//        Robot robot2 = new Robot(1, 0);
//        Robot robot3 = new Robot(2, 0);


//        new RobotMover(new MoveByWalk()).addRobot(robot);
//        new Launcher().addRobot(robot2);
//        new Launcher().addRobot(robot3);


    }
}
