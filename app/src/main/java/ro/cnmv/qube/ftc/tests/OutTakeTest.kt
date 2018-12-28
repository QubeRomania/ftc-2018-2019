package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

@TeleOp (name = "OutTake Test", group = "Tests")

class OutTakeTest: OpMode() {

    val gp1 = Gamepad(gamepad1)

    override fun Hardware.run() {

        waitForStart()

        while (opModeIsActive()) {
            outTake.moveSlider((gp1.right_trigger - gp1.left_trigger).toDouble())
            outTake.dropMinerals(gp1.checkToggle(Gamepad.Button.A))
        }
    }

}
