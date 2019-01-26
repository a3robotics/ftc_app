package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="COMPETITION Depot side", group="COMPETITION AUTO")
public class COMPETITION_AUTO extends LinearOpMode {

    /** COMPETITION AUTO
     *
     *  DO NOT EDIT THIS AUTO UNLESS YOU ARE 101% SURE YOUR CODE IS CONSISTENT!
     *  DO TESTING AND EXPERIMENTING IN OTHER AUTOS
     *
     *  Basically, this auto should be ready to go anytime with no last minute changes or bug fixes
     *  The last thing we want is to have a bug right before competition because we were
     *  working on new stuff. This auto intends to ensure that consistency by only using proven
     *  code and never experimental features.
     *
     *  If you do not know if you can add your code to this auto, talk to Ben.
     *
     *  Features we want that need to be worked on (programming or mechanical):
     *  - Team marker deposit needs to be developed and tested (15 pts value)
     *  - Tensorflow code needs to be debugged and tested (2/3 * 25 pts value)
     *  - Tensorflow code needs to be encapsulated so it can be called from any auto
     *
     *  TODO: If you're out of ideas for projects, work on one of the above.
     */

    private HardwareLiftBot robot = new HardwareLiftBot(this);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // Initialize auto
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        /* Basic strategy:
         *
         *  1) Lower from lift (30 pts)
         *  2) Drive to depot (1/3 chance of 25 pts if gold marker is in the middle)
         *  3) Drive to crater (10 pts)
         */

        // Lower from Lift
        robot.lowerLift();

        // Turn towards depot
        robot.rotateGyro(90);

        // Drive to depot
        robot.driveByTime(-72,1);

        // Turn towards crater
        robot.rotateGyro(45);

        // Drive to crater
        robot.driveByTime(120,1);

    }
}
