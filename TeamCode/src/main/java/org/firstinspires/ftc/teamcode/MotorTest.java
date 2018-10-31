package org.firstinspires.ftc.teamcode;

//imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MotorTest",group="MotorTest")
public class MotorTest extends OpMode {

    //Variables
    private DcMotor motorL;
    private DcMotor motorR;

    public void init() {
        // In the app, go to config and set the motor name to "motorTest"
        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        motorL.setPower(0);
        motorR.setPower(0);
    }
    public void start() {
        // Nothing needs be done
    }
    public void loop() {
        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        // Set motor power to joystick Y value
        motorL.setPower(leftY);
        motorR.setPower(rightY);
    }
    public void stop() {
        // Stop the motor
        motorL.setPower(0);
        motorR.setPower(0);
    }

}
