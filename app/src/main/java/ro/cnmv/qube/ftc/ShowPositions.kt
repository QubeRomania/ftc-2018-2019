package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.Hardware

@TeleOp (name = "ShowPosition", group = "Utility")

class ShowPositions:OpMode() {

    override fun Hardware.run() {
        waitForStart()
        while (opModeIsActive()) {
            telemetry.addData("Intake Position: ", intake.getIntakePosiiton())
            telemetry.addData("Outake Position: ", outTake.outTakePosition)
            motors.printPosition(telemetry)
            telemetry.update()git
        }
    }
}
