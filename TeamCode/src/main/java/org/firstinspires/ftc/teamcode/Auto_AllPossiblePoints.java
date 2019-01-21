package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import static java.lang.Math.abs;

@Autonomous(name="Auto_AllPossiblePoints", group="Auto_AllPossiblePoints")
public class Auto_AllPossiblePoints extends LinearOpMode {

    private HardwareLiftBot robot;

    private TensorFlowObjectDetectionFunc tensorflow = new TensorFlowObjectDetectionFunc(this);
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {

//        tensorflow.initTensorflow(hardwareMap);
        robot = new HardwareLiftBot(this);
        robot.init(hardwareMap);

        telemetry.addData(">", "Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();

//        // LOWER FROM LIFT AND FREE SELF
//        robot.lowerFromLift();
//
//        robot.rotate(-110); // lol should be 90
//        robot.driveByTime(64,0.5);
//        robot.rotate(60); // lol should be 45
//        robot.driveByTime(-150, 1);

//        // KNOCK GOLD MINERAL FROM POSITION
        int goldPos = tensorflow.getPos();
        if(goldPos == -1){
            telemetry.addData("goldPos: ", goldPos);
        }else{
            switch(goldPos) {
                case 0:
                    // Left
                    telemetry.addData("Gold pos:","left");
                    break;
                case 1:
                    // Center
                    telemetry.addData("Gold pos:","center");
                    break;
                case 2:
                    // Right
                    telemetry.addData("Gold pos:","right");
                    break;
            }
        }
//
////        // DEPOSIT TEAM MARKER
////        robot.runIntake(2, "backwards");
////
////        // YEET INTO SELF CRATER
////        // Todo: add option to yeet into opponent's crater
////        rotate(25);
////        driveByTime(100,1);
        runtime.reset();
        while(runtime.time() < 10 && opModeIsActive()) {

        }

        robot.kill();
    }
}
