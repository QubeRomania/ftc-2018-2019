package ro.cnmv.qube.ftc.tests

import android.text.style.UpdateAppearance
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

@TeleOp(name = "RotationTest", group = "Tests")
public class RotationTest : OpMode() {

    override fun Hardware.run() {
        var currentAngle = 0.0
        val gp1 = Gamepad(gamepad1)

        val value = 5

        waitForStart()

        while (opModeIsActive()) {

            if (gp1.checkToggle(Gamepad.Button.A)) currentAngle += value
            if (gp1.checkToggle(Gamepad.Button.B)) currentAngle -= value
            if (gp1.checkToggle(Gamepad.Button.Y))
                rotateTo(currentAngle)

            telemetry.addData("First", hw.imu.imu.getAngularOrientation().firstAngle)
            telemetry.addData("Second", hw.imu.imu.getAngularOrientation().secondAngle)
            telemetry.addData("Third", hw.imu.imu.getAngularOrientation().thirdAngle)
            telemetry.addData("Target", currentAngle)
            telemetry.update()
        }
    }
}
