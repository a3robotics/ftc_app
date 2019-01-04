package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;
import static java.lang.Math.round;

@Autonomous(name="Auto_AllPossiblePoints", group="Auto_AllPossiblePoints")
public class Auto_AllPossiblePoints extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    ModernRoboticsI2cGyro gyro = null;
    public void runOpMode() {

        robot.init(hardwareMap);
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        gyro.calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        waitForStart();
        runtime.reset();
        //driveByEncoder(36, .5);

        lowerFromLift();
        rotate(90);

        driveByTime(-64, 1);

        //runIntake(2, "backwards");

        rotate(25);
        driveByTime(100,1);

        killMotors();
    }

    private void lowerFromLift() {
        // For the first 8.0 seconds lower lift
        runtime.reset();
        while (runtime.time() < 8.0 && opModeIsActive()) {
            robot.motorLift.setPower(0.5);
        }
        robot.motorLift.setPower(0); // stop lift motor
    }

    private void runIntake(int time, String direction) {
        runtime.reset();
        if (!(direction.equals("forwards") || direction.equals("backwards"))) {
            telemetry.addLine("Error - Intake direction must be \"forwards\" or \"backwards\"");
        } else {
            while (runtime.time() < time && opModeIsActive()) {
                robot.intake.setPower(direction.equals("forwards") ? 1 : (direction.equals("backwards") ? -1 : 0));
            }
            robot.intake.setPower(0);
        }
    }

    private void driveByTime(double inches, double speed) {
        if(abs(speed)!=speed) {
            speed = abs(speed);
            inches *= -1;
        }
        runtime.reset();
        // time = constant * ( rate / distance )
        double k = 0.015625; //just some constant
        double time = k * (abs(inches)/speed);
        if(inches < 0) speed = -speed;
        while(runtime.time() < time && opModeIsActive()) {
            robot.motorR.setPower(speed);
            robot.motorL.setPower(speed);
        }
        robot.kill();
    }

    private void driveByTimeGyroAlign(double inches, double speed) { //NEEDS TESTING. This is a modified version of driveByTime() which takes into account of heading, and works to stay aligned while driving
        runtime.reset();
        gyro.resetZAxisIntegrator();
        double heading;
        if(abs(speed)!=speed) {
            speed = abs(speed);
            inches *= -1;
        }
        // time = constant * ( rate / distance )
        double k = 0.015625; //just some constant
        double time = k * (abs(inches)/speed);
        if(inches < 0) speed = -speed;
        while(runtime.time() < time && opModeIsActive()) {
            heading = gyro.getIntegratedZValue();
            if(heading > -5 && heading < 5) {
                robot.motorR.setPower(speed);
                robot.motorL.setPower(speed);
            } else if(heading <= -5) {
                robot.motorR.setPower(speed);
                robot.motorL.setPower(speed * .8);
            } else if(heading >= 5) {
                robot.motorR.setPower(speed * .8);
                robot.motorL.setPower(speed);
            }
        }
        robot.kill();
    }

    private void rotateByGyro(double degrees, double speed) { //NEEDS TESTING. This only works in the range of -179 < degrees < 179.
        //needed variables:
        // 1. Gyro reading. This would be best if it starts at a value of 180, since the reading is cartesian, and therefore is a number between 0-359, and resets back to zero if it gets above 359.
        double heading = gyro.getIntegratedZValue();
        gyro.resetZAxisIntegrator();
        if(degrees <= -179 || degrees >= 179) {
            telemetry.addData("DEGREES PAST AVAILABLE RANGE IN rotateByGyro(): ", degrees);
            telemetry.update();
            return;
        }
        if(degrees ==0) return;

        if(degrees < 0) {
            while(heading > degrees && opModeIsActive()) {
                heading = gyro.getIntegratedZValue();
                robot.motorL.setPower(-speed);
                robot.motorR.setPower(speed);
            }
        }  else while(heading < degrees && opModeIsActive()) {
            heading = gyro.getIntegratedZValue();
            robot.motorL.setPower(speed);
            robot.motorR.setPower(-speed);
        }
        killMotors();
    }

    private void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * abs(degrees), speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety

        while (runtime.time() < time && opModeIsActive()) {

            if (degrees < 0) {
                robot.motorL.setPower(-speed);
                robot.motorR.setPower(speed);
            } if (degrees > 0) {
                robot.motorL.setPower(speed);
                robot.motorR.setPower(-speed);
            } if(degrees == 0) {
                telemetry.addData("ERROR: ", "Parameter \"degrees\" equals zero in rotate()");
                telemetry.update();
            }
        }
        killMotors();
        runtime.reset();
    }

    private void killMotors() {
        runtime.reset();
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }

    // TODO: Make this work
    private void driveByEncoder(double inches, double speed) {

        /* ONLY WORKS FOR POSITIVE DISTANCES */

        long amtL, amtR;
        // double rWheel = 1.89, cWheel = 11.83;
        // needed info: radius and circumference of wheel. No function really.
        double ticks = 64.74 * inches;

        long initL = robot.motorL.getCurrentPosition();
        long initR = robot.motorL.getCurrentPosition();

        robot.motorL.setPower(inches>0?speed:-speed);
        robot.motorR.setPower(inches>0?speed:-speed);

        while (opModeIsActive()) { //This SHOULD work, but it may not. Needs testing.
            amtL = robot.motorL.getCurrentPosition()-initL;
            amtR = robot.motorR.getCurrentPosition()-initR;
            if (amtL < -ticks && amtR < -ticks) break;
        }

        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }
}
