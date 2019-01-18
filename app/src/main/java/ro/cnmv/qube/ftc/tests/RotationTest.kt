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
        var curCoef = 0;

        waitForStart()

        while (opModeIsActive()) {

            if (gp1.checkToggle(Gamepad.Button.X)) {
                while (true) {

                    if (gp1.checkToggle(Gamepad.Button.Y))
                        curCoef = (curCoef + 1) % 3
                    updatePID(gp1, curCoef)

                    if (gp1.checkToggle(Gamepad.Button.X)) break;

                    telemetry.addData("P", "%.2f", P)
                    telemetry.addData("I", "%.2f", I)
                    telemetry.addData("D", "%.2f", D)
                    if (curCoef == 0) telemetry.addData ("current is P", 0)
                    else if (curCoef == 1) telemetry.addData ("current is I", 0)
                    else telemetry.addData ("current is D", 0)
                    telemetry.update()
                }
            }

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
