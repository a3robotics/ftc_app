package org.firstinspires.ftc.teamcode;

//imports

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="TankDriveBot",group="TankDriveBot")
public class TankDriveBot extends OpMode {

    //Variables
    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;
    private DcMotor motorArmRotate;

    //private Servo servoArmBase;
    private CRServo servoArmElbow;

    // encoder vals
    private int liftUpperLimit = -16000;
    private int liftLowerLimit = 0;

    public void init() {
        // In the app, go to config and set the motor names
        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        motorArmRotate = hardwareMap.dcMotor.get("motorArmRotate");
        //servos
        //servoArmBase = hardwareMap.servo.get("servoArmBase");
        servoArmElbow = hardwareMap.crservo.get("servoElbow");

        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);
        motorLift.setDirection(DcMotor.Direction.REVERSE);

        // Start with all motors stopped
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        motorArmRotate.setPower(0);
        servoArmElbow.setPower(0);
        //servoArmBase.setPosition(0.5);

    }

    public void start() {
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
        if(gamepad1.right_bumper && (motorLift.getCurrentPosition()>liftUpperLimit || gamepad1.y)){
            motorLift.setPower(-0.5); // half speed per too much rpm
        }else if(gamepad1.right_trigger > 0 && (motorLift.getCurrentPosition()<liftLowerLimit || gamepad1.y)){ // being at all pressed
            motorLift.setPower(0.5);
        }else{
            motorLift.setPower(0);
        }
        //-16000

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
            servoArmElbow.setPower(1);
        }else if(gamepad1.dpad_down){
            servoArmElbow.setPower(-1);
        }else{
            servoArmElbow.setPower(0);
        }

        getTelemetryData();

    }


    public void stop() {
        // Stop all motors
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        motorArmRotate.setPower(0);
        servoArmElbow.setPower(0);
    }

    private void getTelemetryData() {
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

}
