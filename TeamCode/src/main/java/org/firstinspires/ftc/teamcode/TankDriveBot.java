package org.firstinspires.ftc.teamcode;

//imports

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TankDriveBot",group="TankDriveBot")
public class TankDriveBot extends OpMode {

    //Variables
    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;
    private DcMotor motorArmRotate;

    private Servo servoArmBase;
    private Servo servoArmElbow;

    public void init() {
        // In the app, go to config and set the motor names
        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        motorArmRotate = hardwareMap.dcMotor.get("motorArmRotate");
        //servos
        servoArmBase = hardwareMap.servo.get("servoArmBase");
        servoArmElbow = hardwareMap.servo.get("servoArmElbow");
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        motorArmRotate.setPower(0);
    }
    public void start() {
        // Set servos to init pos
        servoArmElbow.setPosition(0);
        servoArmBase.setPosition(0);

        // Nothing else needs be done
    }
    public void loop() {
        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        double driveSpeedFactor = 0.75;
        // Set motor power to joystick Y value
        motorL.setPower(leftY*driveSpeedFactor);
        motorR.setPower(rightY*driveSpeedFactor);

        // Use right trigger and bumper to lift and lower lift
        if(gamepad1.right_bumper){
            motorLift.setPower(-0.5); // half speed per too much rpm
        }else if(gamepad1.right_trigger > 0){ // being at all pressed
            motorLift.setPower(0.5);
        }else{
            motorLift.setPower(0);
        }

        // Use dpad to rotate arm on lift
        if(gamepad1.dpad_left){
            motorArmRotate.setPower(-0.1); // low speed
        }else if(gamepad1.dpad_right){
            motorArmRotate.setPower(0.1);
        }else{
            motorArmRotate.setPower(0);
        }

        // Run servos
        //dpad up/down and left trigger/bumper
        if(gamepad1.dpad_up){
            servoArmBase.setPosition( servoArmBase.getPosition() + 0.05 ); // Add a tiny bit to the position
        }else if(gamepad1.dpad_down){
            servoArmBase.setPosition( servoArmBase.getPosition() - 0.05 ); // Sub a tiny bit from the position
        }
        if(gamepad1.left_bumper){
            servoArmElbow.setPosition( servoArmElbow.getPosition() + 0.05 ); // Add a tiny bit to the position
        }else if(gamepad1.left_trigger > 0){ // being at all pressed
            servoArmElbow.setPosition( servoArmElbow.getPosition() - 0.05 ); // Sub a tiny bit from the position
        }

        // Send telemetry data - drive encoders
        int motorLpos = motorL.getCurrentPosition();
        telemetry.addData("Left Encoder Position:", motorLpos);
        int motorRpos = motorR.getCurrentPosition();
        telemetry.addData("Right Encoder Position:", motorRpos);
        // Lift encoder
        int motorLiftPos = motorLift.getCurrentPosition();
        telemetry.addData("Lift Encoder:",motorLiftPos);
        //Arm rotation encoder
        int motorArmPos = motorArmRotate.getCurrentPosition();
        telemetry.addData("Arm Rotation Encoder:",motorArmPos);
    }
    public void stop() {
        // Stop the motor
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        motorArmRotate.setPower(0);
    }

    // Extra functions


}
