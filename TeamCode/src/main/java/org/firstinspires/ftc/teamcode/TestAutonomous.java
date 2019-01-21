package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="TestAutonomous", group="Test")
public class TestAutonomous extends LinearOpMode {

    private HardwareLiftBot robot;

    @Override
    public void runOpMode() {
        // Autonomous functions:

        // Test lift lowering
        robot.lowerLift();
        sleep(1000);

        // Test driving forwards
        robot.driveByTime(36,1);
        sleep(500);

        // Test driving backwards
        robot.driveByTime(-36,0.5);
        sleep(1000);

        // Test rotating left
        robot.rotateGyro(90);
        sleep(250);

        // Test rotating right
        robot.rotateGyro(-180);
        sleep(250);

        // Rotate left again
        robot.rotateGyro(90);
        sleep(1000);

        // Test run intake forwards

        // Test run intake backwards


    }
}
