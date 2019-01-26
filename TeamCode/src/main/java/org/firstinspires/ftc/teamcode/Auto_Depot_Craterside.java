package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Craterside Depot auto", group="Craterside Depot auto")
public class Auto_Depot_Craterside extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot(this);

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();

        robot.lowerLift();
        robot.rotateGyro(-90);
        robot.driveByTime(-34,1);
        robot.rotateGyro(-180);
        robot.driveByTime(19,1);
        robot.dispenseMarker();
        robot.rotateGyro(135);
        robot.driveByTime(84,1);
        robot.kill();
    }
}
