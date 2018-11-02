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
    private DcMotor motorLift;
    private DcMotor armRotate;

    public void init() {
        // In the app, go to config and set the motor names
        motorL = hardwareMap.dcMotor.get("motorLeft");
        motorR = hardwareMap.dcMotor.get("motorRight");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        armRotate = hardwareMap.dcMotor.get("armRotate");
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        armRotate.setPower(0);
    }
    public void start() {
        // Nothing needs be done
    }
    public void loop() {
        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        double driveSpeedFactor = 0.75;
        // Set motor power to joystick Y value
        motorL.setPower(leftY*driveSpeedFactor);
        motorR.setPower(rightY*driveSpeedFactor);
        // Use dpad to lift and lower lift
        if(gamepad1.dpad_up){
            motorLift.setPower(-0.5); // half speed per too much rpm
        }else if(gamepad1.dpad_down){
            motorLift.setPower(0.5);
        }else{
            motorLift.setPower(0);
        }
        //rotate the base of the arm
        if(gamepad1.left_bumper) armRotate.setPower(-.5);
        else if(gamepad1.right_bumper) armRotate.setPower(.5);
        else armRotate.setPower(0);

        // Send telemetry data - drive encoders
        int motorLpos = motorL.getCurrentPosition();
        telemetry.addData("Left Encoder Position:", motorLpos);
        int motorRpos = motorR.getCurrentPosition();
        telemetry.addData("Right Encoder Position:", motorRpos);
        // Lift encoder
        int motorLiftPos = motorLift.getCurrentPosition();
        telemetry.addData("Lift Encoder:",motorLiftPos);
        //Arm rotation encoder
        int motorArmPos = armRotate.getCurrentPosition();
        telemetry.addData("Arm Rotation Encoder:",motorLiftPos);
    }
    public void stop() {
        // Stop the motor
        motorL.setPower(0);
        motorR.setPower(0);
        motorLift.setPower(0);
        armRotate.setPower(0);
    }

}
