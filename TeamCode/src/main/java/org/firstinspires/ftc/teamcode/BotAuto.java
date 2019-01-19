package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="BotAuto", group="BotAuto")
public class BotAuto extends LinearOpMode {

    private HardwareLiftBot robot = new HardwareLiftBot();
    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;
    ModernRoboticsI2cGyro gyro    = null;                    // Additional Gyro device
    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    public void runOpMode() {
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        robot.init(hardwareMap);
        waitForStart();

        // Actually do stuff
        while(opModeIsActive()) {
            telemetry.addData("Gyro Z Value", imu.getAngularOrientation().thirdAngle);
        }

        robot.kill();
    }

    // Functions for autonomous
    private void lowerFromLift() {
        // extend lift
        while (robot.motorLift.getCurrentPosition() < liftUpperLimit && opModeIsActive()) {
            robot.motorLift.setPower(0.25);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0);
        sleep(1000);
        // Then, forward robot
        while (robot.motorL.getCurrentPosition() > -30 && robot.motorL.getCurrentPosition() > -30 && opModeIsActive()) { // 30 picked as an arbitrary value. Optimize this.
            robot.motorL.setPower(-0.25);
            robot.motorR.setPower(-0.25);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }
        robot.motorLift.setPower(0);
        sleep(1000);

    }

    private void forward(int inches) {
        int amtL, amtR;
        double rWheel = 2.36;
        double degrees = 180*inches/rWheel;
        robot.motorL.setPower(.5);
        robot.motorR.setPower(.5);
        while(true) {
            amtL = robot.motorL.getCurrentPosition();
            amtR = robot.motorR.getCurrentPosition();
            if(amtL > degrees && amtR > degrees) break;
        }
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }

}
