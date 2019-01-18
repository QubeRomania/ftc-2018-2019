package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Latcher

@TeleOp (name = "LatcherTest", group = "Tests")

class LatcherTest: OpMode() {

    override fun Hardware.run() {
        val gp1 = Gamepad(gamepad1)
        val step = 10.0;
        val power = 1.0
        var position:Double = 0.0;
        waitForStart()

        while (opModeIsActive()) {
//            if (gp1.checkHold(Gamepad.Button.Y)) latcher.latch(Latcher.LatchPosition.EXTENDED)
//            else if (gp1.checkHold(Gamepad.Button.B)) latcher.latch(Latcher.LatchPosition.INTERMEDIATE)
//            else if (gp1.checkHold((Gamepad.Button.A))) latcher.latch(Latcher.LatchPosition.CLOSED)
            position += (gp1.right_trigger.toDouble() - gp1.left_trigger.toDouble()) * step
            latcher.runLatchToPosition(position.toInt(), power)
            telemetry.addData("Target", position.toInt())
            telemetry.addData("Position", latcher.latchMotor.currentPosition)
            telemetry.update()
        }
    }
}
