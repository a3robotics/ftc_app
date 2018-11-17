package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BotAuto", group="BotAuto")
public class BotAuto extends LinearOpMode {

    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;
    private Robot robot = new Robot();

    private int liftUpperLimit = 16000;
    private int liftLowerLimit = 0;

    public void runOpMode() {

        robot.resetEncoders(motorL);
        robot.resetEncoders(motorR);
        robot.resetEncoders(motorLift);

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

        drive(6, 0);

        motorLift.setPower(0);
        sleep(1000);
        // Then, retract lift arm
        while (motorLift.getCurrentPosition() > liftLowerLimit && opModeIsActive()) {
            motorLift.setPower(-0.25);
            telemetry.addData("Lift Encoder:", motorLift.getCurrentPosition());
        }
    }

    private void killRobot() {
        // Stop all motors
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
    }

    // prototype functions

    private void drive(int distance, int rotate) {
        double r = 2.36; //radius of wheels, in inches
        double encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
        double encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
        if (distance == 0) {
            if (rotate == 0) {
                telemetry.addData("ERROR", "Both arguments of drive() are zero");
            } else {
                double rotateRad = rotate * 3.14159 / 180;
                while (encoderRadiansL < rotateRad && encoderRadiansR < rotateRad) {
                    motorR.setPower(0.5);
                    motorL.setPower(0.5);
                    // don't forget to update encoder values
                    encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
                    encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
                }
            }
        } else if (rotate == 0) {
            double rotateRad = distance / r * 180 / 3.14159; //radians
            while (encoderRadiansL < rotateRad && encoderRadiansR < rotateRad) {
                motorR.setPower(0.5);
                motorL.setPower(0.5);
                encoderRadiansL = motorL.getCurrentPosition() * 3.14159 / 180;
                encoderRadiansR = motorR.getCurrentPosition() * 3.14159 / 180;
            }

        } else {
            // error!
            telemetry.addData("ERROR", "Arguments of drive() conflict");

        }
        //FOR RESETTING MOTORS: motorL.(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}