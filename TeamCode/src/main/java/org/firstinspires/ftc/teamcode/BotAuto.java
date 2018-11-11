package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Auto", group="Auto")
public class BotAuto extends LinearOpMode {

    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;

    private int liftUpperLimit = -16000;
    private int liftLowerLimit = 0;

    // y u no get this

    public void runOpMode() {
        lowerFromLift();
    }

    // Functions for autonomous
    private void lowerFromLift() {
        // extend lift
        while(motorLift.getCurrentPosition() < liftUpperLimit){
            motorLift.setPower(-0.5);
            telemetry.addData("Lift Encoder:",motorLift.getCurrentPosition());
        }
        // Then, back up robot
        while(motorL.getCurrentPosition()>-1000&&motorL.getCurrentPosition()>-1000){ // 1000 picked as an arbitrary value. Optimize this.
            motorL.setPower(-0.5);
            motorR.setPower(-0.5);
            telemetry.addData("Left Encoder Position:", motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", motorR.getCurrentPosition());
        }
        // Then, retract lift arm
        while(motorLift.getCurrentPosition() > liftLowerLimit){
            motorLift.setPower(0.5);
            telemetry.addData("Lift Encoder:",motorLift.getCurrentPosition());
        }
    }

}