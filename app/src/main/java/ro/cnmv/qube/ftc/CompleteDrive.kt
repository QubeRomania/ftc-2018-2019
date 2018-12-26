package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Latcher

@TeleOp(name = "CompleteDrive", group = "Main")

class CompleteDrive: OpMode() {

    val gp1 = Gamepad(gamepad1)
    val gp2 = Gamepad(gamepad2)
    val endGame: Boolean
        get() = gp1.checkToggle(Gamepad.Button.START)

    override fun Hardware.run() {


        waitForStart()

        while(opModeIsActive()) {
            // OutTake
            if(endGame) {
                outTake.stop()
            } else {
                outTake.moveSlider((gamepad2.right_trigger - gamepad2.left_trigger).toDouble())
                outTake.dropMinerals(gp2.checkToggle(Gamepad.Button.A))
            }
            ///TODO: Intake


            // Latching
            if(endGame && outTake.isClosed()) { // Make sure the outTake slider is closed before bringing the latcher down
                latcher.latch(when(gp1.checkToggle(Gamepad.Button.A)) {
                    true -> Latcher.LatchPosition.INTERMEDIATE
                    false -> Latcher.LatchPosition.CLOSED
                })
            } else {
                latcher.latch(Latcher.LatchPosition.EXTENDED)
            }


        }
    }

}
