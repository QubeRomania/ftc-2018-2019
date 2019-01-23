package ro.cnmv.qube.ftc.tests

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import ro.cnmv.qube.ftc.Gamepad
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.StrafePID
import ro.cnmv.qube.ftc.hardware.Hardware

@Config
object pula {
    @JvmField var constant = 1000.0
}
@Autonomous
class AutoStrafeTest : OpMode() {
    override fun Hardware.run() {
        val timer = ElapsedTime()
        motors.runWithConstantVelocity()
        waitForStart()
        timer.reset()

        var lastDist = 0.0
        while(opModeIsActive()) {
            if (StrafePID.dist == lastDist) idle()
            lastDist = StrafePID.dist
            strafe(StrafePID.dist, 0.0, maxTimePerStrafe)
        }
    }
}
