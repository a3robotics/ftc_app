package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BotAuto", group="BotAuto")
public class BotAuto extends LinearOpMode {

    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;
    //private Robot robot = new Robot();

    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;

    public void runOpMode() {

        //robot.resetEncoders(motorL);
        //robot.resetEncoders(motorR);
        //robot.resetEncoders(motorLift);

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
        //drive(10, 0);

        killRobot();

    }

    // Functions for autonomous
    private void lowerFromLift() {
        // extend lift
        while (motorLift.getCurrentPosition() < liftUpperLimit && opModeIsActive()) {
            motorLift.setPower(0.25);
            telemetry.addData("Lift Encoder:", motorLift.getCurrentPosition());

        }
        motorLift.setPower(0);
        sleep(1000);
        // Then, forward robot
        while (motorL.getCurrentPosition() > -30 && motorL.getCurrentPosition() > -30 && opModeIsActive()) { // 30 picked as an arbitrary value. Optimize this.
            motorL.setPower(-0.25);

            motorR.setPower(-0.25);
            telemetry.addData("Left Encoder Position:", motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", motorR.getCurrentPosition());
        }
        motorLift.setPower(0);
        sleep(1000);
        // Then, retract lift arm
//        while (motorLift.getCurrentPosition() > liftLowerLimit && opModeIsActive()) {
//            motorLift.setPower(-0.25);
//            telemetry.addData("Lift Encoder:", motorLift.getCurrentPosition());
//        }
    }

    private void killRobot() {
        // Stop all motors
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
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
