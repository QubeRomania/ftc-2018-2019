package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Intake
import ro.cnmv.qube.ftc.hardware.Latcher
import java.lang.Math.atan2
import kotlin.math.absoluteValue

@TeleOp(name = "CompleteDrive", group = "Main")
class CompleteDrive: OpMode() {
    override fun Hardware.run() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)
        var isTransfer = true

        waitForStart()

        while(opModeIsActive()) {
            val endGame = gp1.checkToggle(Gamepad.Button.START)
            // OutTake
            if(endGame) {
                outTake.close()
            } else {
                val power = (gamepad2.right_trigger - gamepad2.left_trigger).toDouble()
                outTake.moveSlider(power)

                if(power.absoluteValue < 0.1)outTake.dropMinerals(gp2.checkHold(Gamepad.Button.A))
                //if (!gp2.checkHold(Gamepad.Button.A)) outTake.intermediatePosition()
            }
            ///Intake
            intake.moveSlider((gp1.right_trigger - gp1.left_trigger).toDouble())

            if (gp1.checkToggle(Gamepad.Button.A)) isTransfer = !isTransfer

            if(outTake.outTakeSlider.currentPosition > 6000) {
                intake.rotate(Intake.ModeRotate.DROP)
            } else {
                if (isTransfer) intake.rotate(Intake.ModeRotate.TRANSFER)
                else intake.rotate(Intake.ModeRotate.OPEN)
            }
            if (gp2.right_stick_y > 0.5) intake.maturica(Intake.ModeMaturica.IN)
            else if (gp2.right_stick_y <= 0.5 && gp2.right_stick_y >= -0.5) intake.maturica(Intake.ModeMaturica.STOP)
            else if (gp2.right_stick_y < -0.5) intake.maturica(Intake.ModeMaturica.OUT)

            telemetry.addData("Outtake position", outTake.outTakeSlider.currentPosition)
            telemetry.addData("Target", outTake.outTakePosition)
            telemetry.addData("Intake position", intake.slideMotor.currentPosition)
            telemetry.addData("Target", intake.pos)
            telemetry.update()
            //Drive
            hw.motors.move(direction, speed, rotation)
        }
    }

    /// The direction in which the robot is translating.
    private val direction: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = -gamepad1.left_stick_y.toDouble()

            return atan2(y, x) / Math.PI * 180.0 - 90.0
        }

    /// Rotation around the robot's Z axis.
    private val rotation: Double
        get() = -gamepad1.right_stick_x.toDouble()

    /// Translation speed.
    private val speed: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = gamepad1.left_stick_y.toDouble()

            return Math.sqrt((x * x) + (y * y))
        }
}
