package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
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


    private HardwareLiftBot robot = new HardwareLiftBot();
    ModernRoboticsI2cGyro gyro    = null;                    // Additional Gyro device
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

    private void drive(double inches, double speed) { //updated forward for it to use the actual amount of ticks that our motors have per rotation (which is 356.3). Also, I added a speed parameter. --Bailey
        long amtL, amtR;
        double rWheel = 2.36, cWheel = 14.83; //needed info: radius and circumference of wheel. No function really.
        double ticks = 24.03 * inches; /*24.03 is the amount of ticks that the encoder goes through for every inch the robot travels, and if you multiply
                                      this ratio by the amount of inches you want to travel, throbot.motorL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                                      robot.motorR.setMode(DcMotor.RunMode.RESET_ENCODERS);en you get the amount of distance you want the bot to go.*/
        long iTicks = round(ticks);

        robot.motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.motorL.setPower(speed);
//        robot.motorR.setPower(speed);
        if (inches > 0) {
            robot.motorL.setPower(-speed);
            robot.motorR.setPower(-speed);
        } else if (inches < 0) {
            robot.motorL.setPower(speed);
            robot.motorR.setPower(speed);
        }
        while (opModeIsActive()) { //This SHOULD work, but it may not. Needs testing.
            amtL = robot.motorL.getCurrentPosition();
            amtR = robot.motorR.getCurrentPosition();
            telemetry.addData("Left Encoder Position:", robot.motorL.getCurrentPosition());
            telemetry.addData("Right Encoder Position:", robot.motorR.getCurrentPosition());
            if (inches > 0) {
                if (amtL < -iTicks && amtR < -iTicks)
                    break;//Encoder Values are negative when the bot goes forward
            } else if (inches < 0) {
                if (amtL > iTicks && amtR > iTicks) break;
            } else break;
        }
        robot.motorL.setPower(0);
        robot.motorR.setPower(0);
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
