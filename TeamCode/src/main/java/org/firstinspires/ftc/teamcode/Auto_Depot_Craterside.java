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
        //in robot.rotate(degrees), CW -> negative, CCW -> positive
        robot.lowerLift();
        robot.rotateGyro(90);
        robot.driveByTime(-76,1);//go into the friendly depot
        robot.dispenseMarker();
        robot.rotateGyro(-45);//direct bot towards enemy crater
        robot.driveByTime(84,1); //yeet into enemy craterside (not friendly one to reduce traffic)
        robot.kill();
    }
}
