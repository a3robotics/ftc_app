package org.firstinspires.ftc.teamcode;

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
    public void runOpMode() {
        /* lower,
         * rotate 180,
         * drive 1 foot,
         * rotate 180,
         * drive 2 feet,
         * activate servo,
         * drive back 1 foot,
         * rotate ?? (120?),
         * drive ?? (6-8 ft)
         */
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        lowerFromLift();
        rotate(180);

        drive(-12, .25);
        rotate(180);

        drive(24, 0.5);

        // run intake for a second or two
        runtime.reset();
        while(runtime.time() < 2) {
            robot.intake.setPower(-1);
        }
        robot.intake.setPower(0);

        drive(-12, .25);

        rotate(120);
        drive(100,1);

        killMotors();
    }

    private void lowerFromLift() {
        // For the first 8.5 seconds lower lift
        runtime.reset();
        while (runtime.time() < 8.5) {
            // 15 seconds
            robot.motorLift.setPower(-0.25);
        }
        robot.motorLift.setPower(0); // stop lift motor
    }

    private void drive(double inches, double speed) { //updated forward for it to use the actual amount of ticks that our motors have per rotation (which is 356.3). Also, I added a speed parameter. --Bailey
        long amtL, amtR;
        double rWheel = 2.36, cWheel = 14.83; //needed info: radius and circumference of wheel. No function really.
        double ticks = 24.03 * inches; /*24.03 is the amount of ticks that the encoder goes through for every inch the robot travels, and if you multiply
                                      this ratio by the amount of inches you want to travel, robot.motorL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                                      robot.motorR.setMode(DcMotor.RunMode.RESET_ENCODERS);en you get the amount of distance you want the bot to go.*/
        long iTicks = round(ticks);

        robot.motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.motorL.setPower(speed);
//        robot.motorR.setPower(speed);
        if (inches > 0) {
            robot.motorL.setPower(-speed);
            robot.motorR.setPower(-speed);
        } else if (inches < 0) {
            robot.motorL.setPower(speed);
            robot.motorR.setPower(speed);
        }
        while (opModeIsActive()) { //This SHOULD work, but it may not. Needs testing.
            amtL = robot.motorL.getCurrentPosition();
            amtR = robot.motorR.getCurrentPosition();
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
            if (inches > 0) {
                if (amtL < -iTicks && amtR < -iTicks)
                    break;//Encoder Values are negative when the bot goes forward
            } else if (inches < 0) {
                if (amtL > iTicks && amtR > iTicks) break;
            } else break;
        }
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }
    private void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * abs(degrees), speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety

        while (runtime.time() < time && opModeIsActive()) {

            if (degrees < 0) {
                robot.motorL.setPower(-speed);
                robot.motorR.setPower(speed);

            }
            if (degrees > 0) {
                robot.motorL.setPower(speed);
                robot.motorR.setPower(-speed);

            } if(degrees == 0) {
                telemetry.addData("ERROR: ", "Parameter \"degrees\" equals zero in rotate()");
            }
        }
        runtime.reset();
    }
    private void killMotors() {
        runtime.reset();
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }
}
