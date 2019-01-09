package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class TensorflowDetector {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AfJdUK3/////AAABmVMyOgQ5lUHOi07/wD3KiM5ZBRv0uo0sd8i+1/T4l5+ceTS3dH3l2lXQIxltojQpKqaay/V2qqGruK3xWStKhpTb9xeIv8GVLleK8aaEJsfuDt7ivAesWJFPKyWE5230KyrKYUnhj9v6d6MS9/ZybJIGsDXsif6XB2GDk8SUAToWbddTE+HjRU2mR1XmIAlyzQseew50flFuN2639STopNqJDki3drokkni82YOZ7CoiG5OgQ+LIRYG+57RKa69ak5nVws6eoxznR/P7ut9WKT2AAOYJ4y1D7kZBrdO2GyCfrn9RjJvXdM2Vb0RcIvzN11+IYHxd3GwP3DSN0pA+ET0XPMmpug6pM9kA7k8erE0O\n";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private ElapsedTime runtime = new ElapsedTime();


    public void initTensorflow(HardwareMap hwmap) {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod(hwmap);
        }
    }

    public int getMineralPosition () {
        runtime.reset();
        while (runtime.time() < 5) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
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
                                //telemetry.addData("Gold Mineral Position", "Left");
                                return 0;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                //telemetry.addData("Gold Mineral Position", "Right");
                                return 2;
                            } else {
                                //telemetry.addData("Gold Mineral Position", "Center");
                                return 1;
                            }
                        }
                    }
                }
            }
        }
        return -1; // If within 5 seconds it has failed to ID the minerals and return a location, return an error
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        /** SET CAMERA DIRECTION ON PHONE */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod(HardwareMap hmap) {
        int tfodMonitorViewId = hmap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hmap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
