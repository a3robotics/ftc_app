package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto_AllPossiblePoints", group="Auto_AllPossiblePoints")
public class Auto_AllPossiblePoints extends LinearOpMode {
    private HardwareLiftBot robot = new HardwareLiftBot();
    private ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        /* purple arrow:
         * lower,
         * rotate 180,
         * drive 1 foot,
         * rotate 180,
         * drive 2 feet,
         * activate servo,
         * drive back 1 foot,
         * rotate ?? (120?),
         * drive ?? (6-8 ft)
         */
        robot.init(hardwareMap);
        waitForStart();
        runtime.reset();

        robot.lowerFromLift();
        robot.rotate(180);

        robot.drive(-12, .25);
        robot.rotate(180);

        robot.drive(24, 0.5);
        // activate servo
        robot.drive(-12, .25);

        robot.rotate(120);
        robot.drive(100,1);

        robot.kill();
    }
}
