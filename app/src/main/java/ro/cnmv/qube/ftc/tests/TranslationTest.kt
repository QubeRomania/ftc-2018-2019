package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

@TeleOp(name = "TranslationTest", group = "Tests")
public class TranslationTest : OpMode() {

    override fun Hardware.run() {
        var currentPosition = 0.0
        val gp1 = Gamepad(gamepad1)
        val value = 100

        waitForStart()

        while (opModeIsActive()) {
            if (gp1.checkToggle(Gamepad.Button.A)) currentPosition += value
            if (gp1.checkToggle(Gamepad.Button.B)) currentPosition -= value
            if (gp1.checkToggle(Gamepad.Button.Y)) {
                goTo(currentPosition, 0.0)
                currentPosition = 0.0
            }
            telemetry.addData("Target", currentPosition)
            telemetry.update()
        }
    }
}
