package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Crater auto", group="Crater auto")
public class Auto_Crater extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot(this);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();

        // Lower from Lift
        robot.lowerLift();

        // Turn towards depot
        robot.rotateGyro(-90);

        // Drive to crater
        robot.driveByTime(72,1);

        // kill
        robot.kill();
    }
}
