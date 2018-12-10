package org.firstinspires.ftc.teamcode;

/**
 * TODO:
 * After lower:
 * Near/far targets
 * possible targets: marker zone and crater
 *
 * combinations:
 * move to near crater
 * move to far crater
 * move to near marker
 * move to far marker
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;

@Autonomous(name="BotAuto", group="BotAuto")
@Disabled
public class BotAuto extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot();
    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;

    public ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();
        boolean baileyCode = false;

        // Actually do stuff
        if(baileyCode == false) lowerLiftAndDriveIntoCrater(); //That's a mighty big function. I think I want to shorten it.

        if(baileyCode == true) { //owo, what dis?
            //If you make the code to work as thus, it is a lot less confusing, and it also allows for easier updating. Of course, the numbers here are only temporary, but it does a similar thing to lowerLiftAndDriveIntoCrater()
            lowerLift();
            drive(-12, .25);
            rotate(180);
            drive(12, 1);
            robot.kill();
            killMotors();
        }

        robot.kill();
    }

    // Functions for autonomous
    private void lowerLiftAndDriveIntoCrater() { // its java I'm allowed to be verbose like this

        // For the first 8 seconds lower lift
        while (runtime.time() < 8 && opModeIsActive()) {
            // 15 seconds
            robot.motorLift.setPower(-0.25);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0); // stop lift motor

        //rotate robot 180 degrees
        turn180();
        runtime.reset();

        // then drive backwards
        while (runtime.time() < 2 && opModeIsActive()) {
            robot.motorL.setPower(0.25);
            robot.motorR.setPower(0.25);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }

        //rotate robot 180 degrees
        turn180();
        runtime.reset();

        // then yeet yourself into the crater (go faster)
        while (runtime.time() < 0.75 && opModeIsActive()) {
            robot.motorL.setPower(-1);
            robot.motorR.setPower(-1);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }

        // kill the motors
        runtime.reset();
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);

    }

    private void turn180() {
        runtime.reset(); // redundancy for safety
        while (runtime.time() < 1.8 && opModeIsActive()) { //This means that in a ratio of (1.8s:180deg), that degree would be travelled for every 0.01 secs. Who needs gyros!
            robot.motorL.setPower(0.6);
            robot.motorR.setPower(-0.6);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }
        runtime.reset(); // redundancy for safety
    }

    private void lowerLift() {
        // For the first 8 seconds lower lift
        while (runtime.time() < 8 && opModeIsActive()) {
            // 15 seconds
            robot.motorLift.setPower(-0.25);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0); // stop lift motor
    }

    private void drive(double inches, double speed) { //updated forward for it to use the actual amount of ticks that our motors have per rotation (which is 356.3). Also, I added a speed parameter. --Bailey
        long amtL, amtR;
        double rWheel = 2.36, cWheel = 14.83; //needed info: radius and circumference of wheel. No function really.
        double ticks = 24.03 * inches; /*24.03 is the amount of ticks that the encoder goes through for every inch the robot travels, and if you multiply this ratio by the amount of inches
                                      you want to travel, then you get the amount of distance you want the bot to go.*/
        robot.motorL.setPower(speed);
        robot.motorR.setPower(speed);
        while(opModeIsActive()) { //This SHOULD work, but it may not. Needs testing.
            amtL = robot.motorL.getCurrentPosition();
            amtR = robot.motorR.getCurrentPosition();
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
            if(inches > 0) {
                robot.motorL.setPower(-speed);
                robot.motorR.setPower(-speed);
                if (amtL > ticks && amtR > ticks) break;
            }
            else if(inches < 0) {
                robot.motorL.setPower(speed);
                robot.motorR.setPower(speed);
                if (amtL < ticks && amtR < ticks) break;
            }
            else break;
        }
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }

    private void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * degrees, speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety

        while (runtime.time() < time && opModeIsActive()) {

            if (degrees < 0) {
                robot.motorL.setPower(-.6);
                robot.motorR.setPower(.6);

            } else if (degrees > 0) {
                robot.motorL.setPower(.6);
                robot.motorR.setPower(-.6);

            } else {
                telemetry.addData("ERROR: ", "Parameter degrees equals zero in rotate()");
            }
        }
        runtime.reset();
    }
    private void killMotors() {
        runtime.reset();
        robot.kill();
    }
}
