package org.firstinspires.ftc.teamcode;

//imports

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TankDriveBot",group="TankDriveBot")
public class TankDriveBot extends OpMode {

    private HardwareLiftBot robot = new HardwareLiftBot(this);
    private int liftUpperLimit = 16000;
    private int liftLowerLimit = 0;
    private ElapsedTime runtime = new ElapsedTime();

    public void init() {robot.init(hardwareMap);}
    public void start() {}

    public void loop() {
        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        double driveSpeedFactor = 0.75;
        // Set motor power to joystick Y value
        robot.motorL.setPower(leftY * -driveSpeedFactor);
        robot.motorR.setPower(rightY * -driveSpeedFactor);

        // Use right trigger and bumper to lift and lower lift
        if (gamepad1.right_bumper) {
            robot.motorLift.setPower(-0.5); // half speed per too much rpm
        } else if (gamepad1.right_trigger > 0) { // being at all pressed
            robot.motorLift.setPower(0.5);
        } else {
            robot.motorLift.setPower(0);
        }

        if (gamepad1.left_bumper){
            robot.intake.setPower(-1);
        }else if (gamepad1.left_trigger > 0){
            robot.intake.setPower(1);
        } else {
            robot.intake.setPower(0);
        }

        if(gamepad1.x){
            robot.marker.setPower(1);
        }else{
            robot.marker.setPower(0);
        }

        getTelemetryData();
        if(gamepad1.back){
            robot.kill();
        }
    }

    public void stop() {
        robot.kill();
    }

    private void getTelemetryData() {
        // Send telemetry data - drive encoders
        int motorLpos = robot.motorL.getCurrentPosition();
        telemetry.addData("Left Encoder Position:", motorLpos);
        int motorRpos = robot.motorR.getCurrentPosition();
        telemetry.addData("Right Encoder Position:", motorRpos);
        // Lift encoder
        int motorLiftPos = robot.motorLift.getCurrentPosition();
        telemetry.addData("Lift Encoder:",motorLiftPos);

    }

}
