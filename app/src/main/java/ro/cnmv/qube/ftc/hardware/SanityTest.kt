package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.sensors.Imu
import ro.cnmv.qube.ftc.hardware.sensors.REVDistanceSensor

@TeleOp
class SanityTest : LinearOpMode(){

    override fun runOpMode() {

        val motors = DriveMotors(hardwareMap)
        //val intake = Intake(hardwareMap)
        //val outTake = OutTake(hardwareMap)
        //val latcher = Latcher(hardwareMap)
        //val imu = Imu(hardwareMap)
        //val distanceSensor = REVDistanceSensor(hardwareMap)
        //val marker = Marker(hardwareMap)

        waitForStart()

        while (opModeIsActive());
    }

}
