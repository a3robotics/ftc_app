package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.round;

public class HardwareLiftBot {

    public DcMotor motorL = null;
    public DcMotor motorR = null;
    public DcMotor motorLift = null;

    public CRServo intake = null;

    HardwareMap hwMap =  null;
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareLiftBot(){

    }

    public void init (HardwareMap ahwMap) {
        hwMap = ahwMap;

        motorL = hwMap.dcMotor.get("motorLeft");
        motorR = hwMap.dcMotor.get("motorRight");
        motorLift = hwMap.dcMotor.get("motorLift");
        motorLift.setDirection(DcMotor.Direction.REVERSE);
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        intake = hwMap.crservo.get("intake");

        // Start with all motors stopped
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        intake.setPower(0);
    }

    public void kill () {
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        intake.setPower(0);
    }

    public void drive(double inches, double speed) { //updated forward for it to use the actual amount of ticks that our motors have per rotation (which is 356.3). Also, I added a speed parameter. --Bailey
        long amtL, amtR;
        double rWheel = 2.36, cWheel = 14.83; //needed info: radius and circumference of wheel. No function really.
        double ticks = 24.03 * inches; /*24.03 is the amount of ticks that the encoder goes through for every inch the robot travels, and if you multiply
                                      this ratio by the amount of inches you want to travel, throbot.motorL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                                      robot.motorR.setMode(DcMotor.RunMode.RESET_ENCODERS);en you get the amount of distance you want the bot to go.*/
        long iTicks = round(ticks);

        motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (inches > 0) {
            motorL.setPower(-speed);
            motorR.setPower(-speed);
        } else if (inches < 0) {
            motorL.setPower(speed);
            motorR.setPower(speed);
        }
        while (true) { //This SHOULD work, but it may not. Needs testing.
            amtL = motorL.getCurrentPosition();
            amtR = motorR.getCurrentPosition();
            if (inches > 0) {
                if (amtL < -iTicks && amtR < -iTicks)
                    break;//Encoder Values are negative when the bot goes forward
            } else if (inches < 0) {
                if (amtL > iTicks && amtR > iTicks) break;
            } else break;
        }
        motorL.setPower(0);
        motorR.setPower(0);
    }

    public void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * degrees, speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety
        while (runtime.time() < time) {
            if (degrees < 0) {
                motorL.setPower(-.6);
                motorR.setPower(.6);
            } else if (degrees > 0) {
                motorL.setPower(.6);
                motorR.setPower(-.6);
            } else {
                break;
            }
        }
        runtime.reset();
    }

    public void lowerFromLift() {
        // For the first 8.5 seconds lower lift
        runtime.reset();
        while (runtime.time() < 8.5) {
            // 15 seconds
            motorLift.setPower(-0.25);
        }
        motorLift.setPower(0); // stop lift motor
    }
}
