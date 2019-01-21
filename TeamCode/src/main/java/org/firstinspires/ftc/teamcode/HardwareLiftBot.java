package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

public class HardwareLiftBot {

    public DcMotor motorL = null;
    public DcMotor motorR = null;
    public DcMotor motorLift = null;
    public CRServo intake = null;
    private BNO055IMU imu;
    private HardwareMap hwMap =  null;
    private ElapsedTime period = new ElapsedTime();
    private ElapsedTime runtime;
    private LinearOpMode parent;

    public HardwareLiftBot(LinearOpMode _parent){
        parent = _parent;

    }
    public HardwareLiftBot(OpMode _parent){
        parent = null;

    }

    public void init (HardwareMap ahwMap) {
        hwMap = ahwMap;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        motorL = hwMap.dcMotor.get("motorLeft");
        motorR = hwMap.dcMotor.get("motorRight");
        motorLift = hwMap.dcMotor.get("motorLift");
        intake = hwMap.crservo.get("intake");
        // left motor is backwards
        motorR.setDirection(DcMotor.Direction.REVERSE);
        motorLift.setDirection(DcMotor.Direction.REVERSE);
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

    public void lowerLift() {
        // For the first 14 seconds lower lift
        runtime.reset();
        while (runtime.time() < 14 && parent.opModeIsActive()) {
            // 15 seconds
            motorLift.setPower(0.25);
            parent.telemetry.addData("Lift Encoder:", motorLift.getCurrentPosition());
        }
        motorLift.setPower(0); // stop lift motor
    }

    private void driveByTime(double inches, double speed) {
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

    private void rotateGyro(float degrees) {
        float currentHeading; //CW -> negative, CCW -> positive
        String dir = null;
        float degreesLoop = imu.getAngularOrientation().firstAngle + degrees;
        if(degreesLoop > 180)  degreesLoop = degreesLoop - 360;
        if(degreesLoop < -180) degreesLoop = degreesLoop + 360;

        if(degreesLoop > 0) degreesLoop -= 5;
        if(degreesLoop < 0) degreesLoop += 5;


        while(parent.opModeIsActive()) {
            currentHeading = imu.getAngularOrientation().firstAngle;
            parent.telemetry.addData("Var Heading", currentHeading);
            parent.telemetry.addData("Gyro Heading", imu.getAngularOrientation().firstAngle);
            parent.telemetry.addData("degrees", degrees);
            parent.telemetry.addData("degreesLoop", degreesLoop);
            parent.telemetry.addData("direction", dir);
            parent.telemetry.update();

            if(degreesLoop < 0 && degrees > 0) { //CCW Special Case
                dir = "CCW Special";
                motorL.setPower(-.5);
                motorR.setPower(.5);
                if(currentHeading < 0 && currentHeading > degreesLoop) break;
            }
            else if(degreesLoop < 0 && degrees < 0) { //CW
                dir = "CW";
                motorL.setPower(.5);
                motorR.setPower(-.5);
                if(currentHeading < degreesLoop) break;
            }
            else if(degreesLoop > 0 && degrees < 0) { //CW Special Case
                dir = "CW Special";
                motorL.setPower(.5);
                motorR.setPower(-.5);
                if(currentHeading > 0 && currentHeading < degreesLoop) break;
            }

            else if(degreesLoop > 0 && degrees > 0) { //CCW
                dir = "CCW";
                motorL.setPower(-.5);
                motorR.setPower(.5);
                if(currentHeading > degreesLoop) break;
            }
            else break;
        }
        motorL.setPower(0);
        motorR.setPower(0);
    }
}
