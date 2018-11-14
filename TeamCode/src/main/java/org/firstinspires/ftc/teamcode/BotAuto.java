package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BotAuto", group="BotAuto")
public class BotAuto extends LinearOpMode {

    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;

    private int liftUpperLimit = 16000;
    private int liftLowerLimit = 0;

    public void runOpMode() {

        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        motorLift = hardwareMap.dcMotor.get("motorLift");

        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        motorL.setDirection(DcMotor.Direction.REVERSE);
        motorLift.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();


        lowerFromLift();

    }

    // Functions for autonomous
    private void lowerFromLift() {
        // extend lift
        while(motorLift.getCurrentPosition() < liftUpperLimit && opModeIsActive()){
            motorLift.setPower(0.25);
            telemetry.addData("Lift Encoder:",motorLift.getCurrentPosition());
        }
        motorLift.setPower(0);
        sleep(1000);
        // Then, back up robot
        while(motorL.getCurrentPosition()>-1000&&motorL.getCurrentPosition()>-1000 && opModeIsActive()){ // 1000 picked as an arbitrary value. Optimize this.
            motorL.setPower(-0.25);
            motorR.setPower(-0.25);
            telemetry.addData("Left Encoder Position:", motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", motorR.getCurrentPosition());
        }
        sleep(1000);
        // Then, retract lift arm
        while(motorLift.getCurrentPosition() > liftLowerLimit && opModeIsActive()){
            motorLift.setPower(-0.25);
            telemetry.addData("Lift Encoder:",motorLift.getCurrentPosition());
        }
    }
}