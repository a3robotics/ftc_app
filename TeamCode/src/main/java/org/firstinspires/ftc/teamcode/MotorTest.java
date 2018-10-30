package org.firstinspires.ftc.teamcode;

//imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MotorTest",group="MotorTest")
public class MotorTest extends OpMode {

    //Variables
    private DcMotor motorTest;

    public void init() {
        // In the app, go to config and set the motor name to "motorTest"
        motorTest = hardwareMap.dcMotor.get("motorTest");
        motorTest.setPower(0);
    }
    public void start() {
        // Nothing needs be done
    }
    public void loop() {
        // get left joystick Y value
        double leftY = gamepad1.left_stick_y;
        // Set motor power to joystick Y value
        motorTest.setPower(leftY);
    }
    public void stop() {
        // Stop the motor
        motorTest.setPower(0);
    }
}
