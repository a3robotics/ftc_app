package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto_DriveToFarDepot", group="Auto_DriveToFarDepot")
public class Auto_DriveToFarDepot extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        robot.lowerFromLift();

        robot.rotate(180);
        robot.drive(-12, .25);
        robot.rotate(240);
        robot.drive(85, .5);

        robot.kill();
    }
}
