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
        /*
        Test later and add code
         */
        robot.kill();
    }
}
