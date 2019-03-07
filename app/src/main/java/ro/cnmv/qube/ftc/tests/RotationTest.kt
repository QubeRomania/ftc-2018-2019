package ro.cnmv.qube.ftc.tests

import android.text.style.UpdateAppearance
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.RotatePID
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.Gyroscope
import ro.cnmv.qube.ftc.waitMillis

@TeleOp(name = "RotationTest", group = "Tests")
public class RotationTest : OpMode() {

    override fun Hardware.run() {
        waitForStart()

        var lastAngle = 0.0
        while (opModeIsActive()) {
            rotateTo(RotatePID.angle)
            while (RotatePID.angle == lastAngle) idle()
            lastAngle = RotatePID.angle
        }
    }
}
