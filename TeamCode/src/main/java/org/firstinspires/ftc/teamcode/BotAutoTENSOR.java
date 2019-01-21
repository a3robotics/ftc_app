package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.round;

@Autonomous(name="BotAutoTENSSOR", group="BotAuto")
public class BotAutoTENSOR extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AfJdUK3/////AAABmVMyOgQ5lUHOi07/wD3KiM5ZBRv0uo0sd8i+1/T4l5+ceTS3dH3l2lXQIxltojQpKqaay/V2qqGruK3xWStKhpTb9xeIv8GVLleK8aaEJsfuDt7ivAesWJFPKyWE5230KyrKYUnhj9v6d6MS9/ZybJIGsDXsif6XB2GDk8SUAToWbddTE+HjRU2mR1XmIAlyzQseew50flFuN2639STopNqJDki3drokkni82YOZ7CoiG5OgQ+LIRYG+57RKa69ak5nVws6eoxznR/P7ut9WKT2AAOYJ4y1D7kZBrdO2GyCfrn9RjJvXdM2Vb0RcIvzN11+IYHxd3GwP3DSN0pA+ET0XPMmpug6pM9kA7k8erE0O\n";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    private HardwareLiftBot robot = new HardwareLiftBot(this);
    ModernRoboticsI2cGyro gyro    = null;                    // Additional Gyro device
    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;
    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;
 int goldpos = -2;
    public ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */

        // The IMU sensor object
        BNO055IMU imu;
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        /***
         * START OF GAME
         */
        waitForStart();
        ///ACTIVATE VUFORIA
        if (tfod != null) {
            tfod.activate();
        }
        runtime.reset();
        lowerLift();
        rotate(90);
        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        robot.motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (opModeIsActive() && goldpos == -2 && runtime.time() < 60)  {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Left");
                              goldpos = -1;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                             goldpos = 1;
                            } else {
                                telemetry.addData("Gold Mineral Position", "Center");
                             goldpos=0;
                            }
                        }
                        telemetry.addData("goldpos", goldpos);
                    }
                    telemetry.update();
                }
            }
        }
        //DONE LOOKING AT MINERALS
        telemetry.addData("goldpos", goldpos);
        telemetry.update();
        if(goldpos == -1){//left PROGRAM
/**
 * PLACE CODE
 */

        }
        else if(goldpos == 0){//center PROGRAM
            /**
             * PLACE CODE
             */
        }
        else{ //Right PROGRAM
/**
 * PLACE CODE
 */
        }
        /*
            rotate(90); //negative values go right, and positive values go left
            drive(-12, 1);
            killMotors();
        robot.kill();*/
    }
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    // Functions for autonomous
    private void lowerLiftAndDriveIntoCrater() { // its java I'm allowed to be verbose like this

        // For the first 8 seconds lower lift
        while (runtime.time() < 11 && opModeIsActive()) {
            // 15 seconds
            robot.motorLift.setPower(0.5);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0); // stop lift motor

        //rotate robot 90 degrees
        rotate(90);
        runtime.reset();

        // then drive backwards
        while (runtime.time() < 1.5 && opModeIsActive()) {
            robot.motorL.setPower(-0.25);
            robot.motorR.setPower(-0.25);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }

        //rotate robot 180 degrees
        //rotate(45);
        runtime.reset();

        // then yeet yourself into the crater (go faster)
        while (runtime.time() < 2 && opModeIsActive()) {
            robot.motorL.setPower(-1);
            robot.motorR.setPower(-1);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }

        // kill the motors
        runtime.reset();
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);

    }

    private void turn180() {
        runtime.reset(); // redundancy for safety
        while (runtime.time() < 1.8 && opModeIsActive()) { //This means that in a ratio of (1.8s:180deg), one degree would be travelled for every 0.01 secs. Who needs gyros!
            robot.motorL.setPower(0.6);
            robot.motorR.setPower(-0.6);
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
        }
        runtime.reset(); // redundancy for safety
    }

    private void lowerLift() {
        // For the first 14 seconds lower lift
        while (runtime.time() < 16 && opModeIsActive()) {
            // 15 seconds
            robot.motorLift.setPower(0.25);
            telemetry.addData("Lift Encoder:", robot.motorLift.getCurrentPosition());
        }
        robot.motorLift.setPower(0); // stop lift motor
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
        while(runtime.time() < time && opModeIsActive()) {
            robot.motorR.setPower(speed);
            robot.motorL.setPower(speed);
        }
        robot.kill();
    }

    private void rotateByGyro(double degrees, double speed) { //NEEDS TESTING. This only works in the range of -179 < degrees < 179.
        //needed variables:
        // 1. Gyro reading. This would be best if it starts at a value of 180, since the reading is cartesian, and therefore is a number between 0-359, and resets back to zero if it gets above 359.
        double heading = gyro.getIntegratedZValue();
        gyro.resetZAxisIntegrator();
        if(degrees <= -179 || degrees >= 179) {
            telemetry.addData("DEGREES PAST AVAILABLE RANGE IN rotateByGyro(): ", degrees);
            telemetry.update();
            return;
        }
        if(degrees ==0) return;

        if(degrees < 0) {
            while(gyro.getIntegratedZValue() > degrees && opModeIsActive()) {
                robot.motorL.setPower(speed);
                robot.motorR.setPower(-speed);
                telemetry.addData("Heading: ", gyro.getIntegratedZValue());
                telemetry.addData("Degrees: ", degrees);
                telemetry.update();
            }
        }  else while(gyro.getIntegratedZValue() < degrees && opModeIsActive()) {
            robot.motorL.setPower(-speed);
            robot.motorR.setPower(speed);
            telemetry.addData("Heading: ", gyro.getIntegratedZValue());
            telemetry.addData("Degrees: ", degrees);
            telemetry.update();
        }
        robot.kill();
    }

    private void rotate(int degrees) { //rotates the bot. This works based off of
        double time = 0.01 * abs(degrees), speed = 0.6; //SPEED MUST BE KEPT AT 0.6 FOR THIS FUNCTION TO WORK!!!!
        //0.01 is a ratio which is defined by how the bot turns 180 degrees for each 1.8 seconds while motor speeds are 0.6 (plus or minus)
        runtime.reset(); // redundancy for safety
        while (runtime.time() < time && opModeIsActive()) {

            if (degrees < 0) {
                robot.motorL.setPower(-speed);
                robot.motorR.setPower(speed);

            }
            if (degrees > 0) {
                robot.motorL.setPower(speed);
                robot.motorR.setPower(-speed);

            } if(degrees == 0) {
                telemetry.addData("ERROR: ", "Parameter \"degrees\" equals zero in rotate()");
            }
        }
        runtime.reset();
    }
    private void killMotors() {
        runtime.reset();
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
    }

}
