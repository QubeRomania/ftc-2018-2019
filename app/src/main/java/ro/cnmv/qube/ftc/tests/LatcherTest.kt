package ro.cnmv.qube.ftc.tests

import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Latcher

class LatcherTest: OpMode() {

    val gp1 = Gamepad(gamepad1)

    override fun Hardware.run() {

        waitForStart()

        while (opModeIsActive()) {
            if (gp1.checkHold(Gamepad.Button.Y)) latcher.latch(Latcher.LatchPosition.EXTENDED)
            else if (gp1.checkHold(Gamepad.Button.B)) latcher.latch(Latcher.LatchPosition.INTERMEDIATE)
            else if (gp1.checkHold((Gamepad.Button.A))) latcher.latch(Latcher.LatchPosition.CLOSED)
        }
    }
}
