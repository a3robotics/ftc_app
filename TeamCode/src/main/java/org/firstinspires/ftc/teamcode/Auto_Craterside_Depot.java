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
        //in robot.rotate(degrees), CW -> negative, CCW -> positive
        robot.lowerLift();
        robot.rotateGyro(90);
        robot.driveByTime(-34,1);//hit center mineral
        robot.rotateGyro(90);
        robot.driveByTime(-34,1);//free of crater
        robot.rotateGyro(45);
        robot.driveByTime(-72,1);//in depot
        robot.dispenseMarker();
        robot.driveByTime(84,1);//drive back to friendly team's craterside
        robot.kill();
    }
}
