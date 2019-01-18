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
        val value = 10
        var curCoef = 0

        waitForStart()

        while (opModeIsActive()) {

            if (gp1.checkToggle(Gamepad.Button.X)) {
                while (true) {

                    if (gp1.checkToggle(Gamepad.Button.Y))
                        curCoef = (curCoef + 1) % 3

                    updateDrivePID(gp1, curCoef)

                    if (gp1.checkToggle(Gamepad.Button.X)) break;

                    telemetry.addData("P", "%.2f", P2)
                    telemetry.addData("I", "%.2f", I2)
                    telemetry.addData("D", "%.2f", D2)
                    if (curCoef == 0) telemetry.addData ("current is P", 0)
                    else if (curCoef == 1) telemetry.addData ("current is I", 0)
                    else telemetry.addData ("current is D", 0)
                    telemetry.update()
                }
            }

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
