package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto_DriveToNearDepot", group="Auto_DriveToNearDepot")
public class Auto_DriveToNearDepot extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        robot.lowerFromLift();
        robot.rotate(180);
        robot.drive(-12, .25);
        robot.rotate(180);
        robot.drive(12, 1);
        // about 15 seconds in mas o menos

        // TODO: Run team marker servo

        robot.kill();
    }
}
