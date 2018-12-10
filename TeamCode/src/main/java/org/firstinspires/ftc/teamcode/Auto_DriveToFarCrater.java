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

        robot.lowerFromLift();
        /*
        Test later and add code 
         */
        robot.kill();
    }
}
