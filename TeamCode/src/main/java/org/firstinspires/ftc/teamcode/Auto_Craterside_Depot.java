package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Craterside Depot auto", group="Craterside Depot auto")
public class Auto_Craterside_Depot extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot(this);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();

        robot.lowerLift();
        robot.rotateGyro(-90);
        robot.driveByTime(-34,1);
        robot.rotateGyro(90);
        robot.driveByTime(34,1);
        robot.rotateGyro(-45);
        robot.driveByTime(72,1);
        robot.dispenseMarker();
        robot.rotateGyro(-90);
        robot.driveByTime(78,1);
        robot.kill();
    }
}
