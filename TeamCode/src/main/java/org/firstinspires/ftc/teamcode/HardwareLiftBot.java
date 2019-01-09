package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class HardwareLiftBot {

    public DcMotor motorL = null;
    public DcMotor motorR = null;
    public DcMotor motorLift = null;

    public CRServo intake = null;

    private HardwareMap hwMap =  null;
    private ElapsedTime runtime = new ElapsedTime();

    private LinearOpMode parent;

    public HardwareLiftBot(LinearOpMode parent_){
        parent = parent_;
    }
    public HardwareLiftBot(){
        parent = null;
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

    public void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * degrees, speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety
        while (runtime.time() < time && parent.opModeIsActive()) {
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

    public void driveByTime(double inches, double speed) {
        if(abs(speed)!=speed) {
            speed = abs(speed);
            inches *= -1;
        }
        runtime.reset();
        // time = constant * ( rate / distance )
        double k = 0.015625; //just some constant
        double time = k * (abs(inches)/speed);
        if(inches < 0) speed = -speed;
        while(runtime.time() < time && parent.opModeIsActive()) {
            motorR.setPower(speed);
            motorL.setPower(speed);
        }
        kill();
    }

    public void lowerFromLift() {
        // For the first 8.0 seconds lower lift
        runtime.reset();
        while (runtime.time() < 8.0 && parent.opModeIsActive()) {
            motorLift.setPower(0.5);
        }
        motorLift.setPower(0); // stop lift motor
    }

    public void runIntake(int time, String direction) {
        runtime.reset();
        if (!(direction.equals("forwards") || direction.equals("backwards"))) {
            parent.telemetry.addLine("Error - Intake direction must be \"forwards\" or \"backwards\"");
        } else {
            while (runtime.time() < time && parent.opModeIsActive()) {
                intake.setPower(direction.equals("forwards") ? 1 : (direction.equals("backwards") ? -1 : 0));
            }
            intake.setPower(0);
        }
    }
}
