package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BotAuto", group="BotAuto")
public class BotAuto extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot();
    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;

    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();

        // Actually do stuff
        lowerFromLift();

        robot.kill();
    }

    // Functions for autonomous
    private void lowerFromLift() {
        // extend lift
        while (robot.motorLift.getCurrentPosition() < liftUpperLimit && opModeIsActive()) {
            robot.motorLift.setPower(0.25);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0);
        sleep(1000);
        // Then, forward robot
        while (robot.motorL.getCurrentPosition() > -30 && robot.motorL.getCurrentPosition() > -30 && opModeIsActive()) { // 30 picked as an arbitrary value. Optimize this.
            robot.motorL.setPower(-0.25);
            robot.motorR.setPower(-0.25);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }
        robot.motorLift.setPower(0);
        sleep(1000);

    }

    private void forward(int inches) {
        int amtL, amtR;
        double rWheel = 2.36;
        double degrees = 180*inches/rWheel;
        robot.motorL.setPower(.5);
        robot.motorR.setPower(.5);
        while(true) {
            amtL = robot.motorL.getCurrentPosition();
            amtR = robot.motorR.getCurrentPosition();
            if(amtL > degrees && amtR > degrees) break;
        }
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }

    // prototype functions
/*
    private void drive(int distance, int rotateAmt) {
        double rWheel = 2.36, rbot = 14; //radius of wheels, and the bot from it's rotational center to the front, in inches
        double encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
        double encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
        if (distance == 0) {
            if (rotateAmt == 0) {
                telemetry.addData("ERROR", "Both arguments of drive() are zero");
            } else {
                double rotateAmtRad = rotateAmt * 3.14159 / 180;
                while (rotateAmtRad < rotateAmtRad && rotateAmtRad < rotateAmtRad && opModeIsActive()) {
                    if(rotateAmt < 0) {
                        motorR.setPower(0.5);
                        motorL.setPower(-0.5);
                    }
                    else if(rotateAmt > 0) {
                        motorR.setPower(-0.5);
                        motorL.setPower(0.5);
                    }
                    encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
                    encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
                    rotateAmtRad = (encoderRadiansL+encoderRadiansR)/2 * rWheel / rbot;
                }
            }

        } else if (rotateAmt == 0) {
            double rotateRad = distance / rWheel * 180 / 3.14159; //radians
            while (abs(encoderRadiansL) < rotateRad && abs(encoderRadiansR) < rotateRad  && opModeIsActive()) {
                if (distance < 0) {
                    motorR.setPower(-0.5);
                    motorL.setPower(-0.5);
                } else if (distance > 0) {
                    motorR.setPower(0.5);
                    motorL.setPower(0.5);
                }
                encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
                encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
            }

        } else {
            // error!
            telemetry.addData("ERROR", "Arguments of drive() conflict");

        }
        //FOR RESETTING MOTORS: motorL.(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorL.setPower(0);
        motorR.setPower(0);
    }
    */
}
