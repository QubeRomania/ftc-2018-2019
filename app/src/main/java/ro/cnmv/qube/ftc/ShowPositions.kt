package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.Hardware

@TeleOp (name = "ShowPosition", group = "Utility")

class ShowPositions:OpMode() {

    override fun Hardware.run() {
        motors.resetPosition()
        waitForStart()
        while (opModeIsActive()) {
            telemetry.addData("Intake Slider Position", intake.slideMotor.currentPosition)
            telemetry.addData("Outake Slider Position", outTake.outTakeSlider.currentPosition)
            telemetry.addData("Intake Rotate Position", intake.rotateMotor.currentPosition)
            motors.printPosition(telemetry)
            telemetry.update()
        }
    }
}
