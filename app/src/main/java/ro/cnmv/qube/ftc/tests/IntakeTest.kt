package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.Intake

@TeleOp (name = "Intake Test", group = "Tests")

class IntakeTest: OpMode() {

    override fun Hardware.run() {
        val gp1 = Gamepad(gamepad1)

        waitForStart()

        while (opModeIsActive()) {
            intake.moveSlider((gp1.right_trigger - gp1.left_trigger).toDouble())

            if (gp1.checkHold(Gamepad.Button.A))  intake.rotate(Intake.ModeRotate.TRANSFER)
            else if (gp1.checkHold(Gamepad.Button.B)) intake.rotate(Intake.ModeRotate.OPEN)
            else if (gp1.checkHold(Gamepad.Button.Y)) intake.rotate(Intake.ModeRotate.CLOSE)

            if (gp1.left_stick_y > 0.5) intake.maturica(Intake.ModeMaturica.IN)
            else if (gp1.left_stick_y <= 0.5 && gp1.left_stick_y >= -0.5) intake.maturica(Intake.ModeMaturica.STOP)
            else if (gp1.left_stick_y < -0.5) intake.maturica(Intake.ModeMaturica.OUT)

            telemetry.addData("Slider position", intake.slideMotor.currentPosition)
            telemetry.update()
        }


    }
}
