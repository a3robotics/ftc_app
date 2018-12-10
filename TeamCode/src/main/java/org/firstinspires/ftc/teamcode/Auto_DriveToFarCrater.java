package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto_DriveToFarCrater", group="Auto_DriveToFarCrater")
public class Auto_DriveToFarCrater extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        // blue arrow: lower, drive 1 foot, rotate ?? (90-120), drive ?? (6-8 ft), activate servo

        robot.lowerFromLift();
        robot.rotate(180);
        robot.drive(-12, .25);
        robot.rotate(240);
        robot.drive(85, .5);

        // activate servo

        robot.kill();
    }
}