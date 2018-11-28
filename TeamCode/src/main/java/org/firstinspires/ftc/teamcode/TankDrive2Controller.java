package org.firstinspires.ftc.teamcode;

//imports

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;

@TeleOp(name="TankDrive2Controller",group="TankDrive2Controller")
public class TankDrive2Controller extends OpMode {

    private DcMotor motorL;
    private DcMotor motorR;
    private DcMotor motorLift;

    private int liftUpperLimit = 16000;
    private int liftLowerLimit = 0;

    public void init() {
        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        // Start with all motors stopped
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
    }
    public void start() {

    }
    public void loop() {

        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        double driveSpeedFactor = 0.75;
        // Set motor power to joystick Y value
        motorL.setPower(leftY * driveSpeedFactor);
        motorR.setPower(rightY * driveSpeedFactor);


        getTelemetryData();
        if(gamepad1.back){
            killRobot();
        }
    }
    public void stop() {
        killRobot();
    }
    private void getTelemetryData() {
        // Send telemetry data - drive encoders
        int motorLpos = motorL.getCurrentPosition();
        telemetry.addData("Left Encoder Position:", motorLpos);
        int motorRpos = motorR.getCurrentPosition();
        telemetry.addData("Right Encoder Position:", motorRpos);
        // Lift encoder
        int motorLiftPos = abs(motorLift.getCurrentPosition());
        telemetry.addData("Lift Encoder:",motorLiftPos);
    }
    private void killRobot() {
        // Stop all motors
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
    }
}