package org.firstinspires.ftc.teamcode;

//imports

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TankDrive2Controller",group="TankDrive2Controller")
@Disabled
public class TankDrive2Controller extends OpMode {

    private HardwareLiftBot robot = new HardwareLiftBot();

    private int liftUpperLimit = 16000;
    private int liftLowerLimit = 0;

    public void init() {robot.init(hardwareMap);}
    public void start() {}
    public void loop() {
        // get joystick Y values
        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;

        double driveSpeedFactor = 0.75;
        // Set motor power to joystick Y value
        robot.motorL.setPower(leftY * driveSpeedFactor);
        robot.motorR.setPower(rightY * driveSpeedFactor);

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
        int motorLpos =  robot.motorL.getCurrentPosition();
        telemetry.addData("Left Encoder Position:", motorLpos);
        int motorRpos =  robot.motorR.getCurrentPosition();
        telemetry.addData("Right Encoder Position:", motorRpos);
        // Lift encoder
        int motorLiftPos = robot.motorLift.getCurrentPosition();
        telemetry.addData("Lift Encoder:",motorLiftPos);
    }
}