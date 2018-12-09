package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareLiftBot {

    public DcMotor motorL = null;
    public DcMotor motorR = null;
    public DcMotor motorLift = null;

    public CRServo intake = null;

    HardwareMap hwMap =  null;
    private ElapsedTime period = new ElapsedTime();

    public HardwareLiftBot(){

    }

    public void init (HardwareMap ahwMap) {
        hwMap = ahwMap;

        motorL = hwMap.dcMotor.get("motorLeft");
        motorR = hwMap.dcMotor.get("motorRight");
        motorLift = hwMap.dcMotor.get("motorLift");
        // left motor is backwards
        motorL.setDirection(DcMotor.Direction.REVERSE);

        intake = hwMap.crservo.get("intake");

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
}
