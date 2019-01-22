package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous(name="BotAutoTENSOR", group="BotAuto")
@Disabled
public class BotAutoTENSOR extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AfJdUK3/////AAABmVMyOgQ5lUHOi07/wD3KiM5ZBRv0uo0sd8i+1/T4l5+ceTS3dH3l2lXQIxltojQpKqaay/V2qqGruK3xWStKhpTb9xeIv8GVLleK8aaEJsfuDt7ivAesWJFPKyWE5230KyrKYUnhj9v6d6MS9/ZybJIGsDXsif6XB2GDk8SUAToWbddTE+HjRU2mR1XmIAlyzQseew50flFuN2639STopNqJDki3drokkni82YOZ7CoiG5OgQ+LIRYG+57RKa69ak5nVws6eoxznR/P7ut9WKT2AAOYJ4y1D7kZBrdO2GyCfrn9RjJvXdM2Vb0RcIvzN11+IYHxd3GwP3DSN0pA+ET0XPMmpug6pM9kA7k8erE0O\n";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private HardwareLiftBot robot = new HardwareLiftBot(this);
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    private int liftUpperLimit = 14000;
    private int liftLowerLimit = 0;
    private int goldpos = -2;
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
        BNO055IMU imu;
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        ///ACTIVATE VUFORIA
        if (tfod != null) {
            tfod.activate();
        }
        runtime.reset();
        robot.lowerLift();

        // Ensure the robot is stationary, then reset the encoders and calibrate the gyro.
        robot.kill();
        robot.motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
        while (opModeIsActive() && goldpos == -2 && runtime.time() < 10) {
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
                                goldpos = 0;
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

        // CHOOSE PATH BASED ON MINERAL
        if (goldpos == -1) {//left PROGRAM
            robot.rotateGyro(-45);
        } else if (goldpos == 0) {//center PROGRAM
            robot.driveByTime(12,0.5);
        } else { //Right PROGRAM
           robot.rotateGyro(45);
        }

        // AFTER IT DETECTS MINERALS:
        robot.kill();
    }
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

}
