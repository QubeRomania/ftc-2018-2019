package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Intake
import ro.cnmv.qube.ftc.hardware.Latcher
import java.lang.Math.atan2

@TeleOp(name = "CompleteDrive", group = "Main")

class CompleteDrive: OpMode() {


    override fun Hardware.run() {

        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)
        var isTransfer:Boolean = true

        waitForStart()

        while(opModeIsActive()) {

            val endGame = gp1.checkToggle(Gamepad.Button.START)
            // OutTake
            if(endGame) {
                outTake.close()
            } else {
                outTake.moveSlider((gamepad2.right_trigger - gamepad2.left_trigger).toDouble())
                outTake.dropMinerals(gp2.checkToggle(Gamepad.Button.A))
            }
            ///Intake
            intake.moveSlider((gp1.right_trigger - gp1.left_trigger).toDouble())

            if (gp1.checkToggle(Gamepad.Button.A)) isTransfer = !isTransfer

            if (isTransfer) intake.rotate(Intake.ModeRotate.TRANSFER)
            else intake.rotate(Intake.ModeRotate.OPEN)

            if (gp2.left_stick_y > 0.5) intake.maturica(Intake.ModeMaturica.IN)
            else if (gp2.left_stick_y <= 0.5 && gp2.left_stick_y >= -0.5) intake.maturica(Intake.ModeMaturica.STOP)
            else if (gp2.left_stick_y < -0.5) intake.maturica(Intake.ModeMaturica.OUT)

            // Latching
            if(endGame && outTake.isClosed()) { // Make sure the outTake slider is closed before bringing the latcher down
                latcher.latch(when(gp1.checkToggle(Gamepad.Button.A)) {
                    true -> Latcher.LatchPosition.INTERMEDIATE
                    false -> Latcher.LatchPosition.CLOSED
                })
            } else {
                latcher.latch(Latcher.LatchPosition.EXTENDED)
            }

            //Drive
            hw.motors.move(direction, speed, rotation)
        }
    }

    /// The direction in which the robot is translating.
    val direction: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = -gamepad1.left_stick_y.toDouble()

            return atan2(y, x) / Math.PI * 180.0 - 90.0
        }

    /// Rotation around the robot's Z axis.
    val rotation: Double
        get() = -gamepad1.right_stick_x.toDouble()

    /// Translation speed.
    val speed: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = gamepad1.left_stick_y.toDouble()

            return Math.sqrt((x * x) + (y * y))
        }

}
