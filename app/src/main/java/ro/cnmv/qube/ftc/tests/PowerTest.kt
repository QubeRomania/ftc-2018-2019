package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Hardware

class PowerTest: LinearOpMode() {
    override fun runOpMode() {
        val intakeSlide = hardwareMap.dcMotor.get("intakeSlideMotor")
        val outTakeSlider = hardwareMap.dcMotor["outTakeSlider"]

        val gp1 = ro.cnmv.qube.ftc.Gamepad(gamepad1)
        val gp2 = ro.cnmv.qube.ftc.Gamepad(gamepad2)

        intakeSlide.power = (gp1.right_trigger - gp1.left_trigger).toDouble()
        outTakeSlider.power = (gp2.right_trigger - gp2.left_trigger).toDouble()
    }
}
