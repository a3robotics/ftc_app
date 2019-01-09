package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareLiftBot;

@Autonomous(name="Auto_LiftUp", group="Auto_LiftUp")
// @Disabled
public class Auto_LiftUp extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();
        while(opModeIsActive() && runtime.time() < 10) { // 10 seconds??
            robot.motorLift.setPower(-0.5);
        }
        robot.kill();
    }
}
