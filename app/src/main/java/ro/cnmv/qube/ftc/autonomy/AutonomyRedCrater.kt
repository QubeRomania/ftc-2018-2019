package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD
import ro.cnmv.qube.ftc.waitMillis

@Autonomous
class AutonomyRedCrater : AutonomyBase() {
    val strafeDist = 11.0
    val timer = ElapsedTime()

    override fun preInit() {
        // TODO: object detection
        goldPosition = TFOD.Positions.RIGHT
        val mata = tfod.isGold()
        hw.marker.marker.position = 0.0
    }

    override fun preInitLoop() {
        telemetry.addData("GoldPos", goldPosition)
        telemetry.addLine("READY")
        telemetry.update()
    }

    override fun Hardware.run() {
        // TODO: de-latch

        timer.reset()

        removeGold()
        tfod.stop()

        followTrajectory(arrayListOf(Pair(-90.0, -115.0), Pair(0.0, -45.0)))
        strafe (11.0, -45.0, maxTimePerStrafe)
        followTrajectory(arrayListOf(Pair(-120.0, -45.0)))
        strafe(18.0, -45.0, maxTimePerStrafe)
        followTrajectory(arrayListOf(Pair(0.0, 45.0)))
        dropMarker()
        waitMillis(500)

        followTrajectory(arrayListOf(Pair(0.0, -45.0)))
        strafe(13.0, -45.0, maxTimePerStrafe)
        followTrajectory(arrayListOf(Pair(160.0, -45.0)))

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()
        waitMillis(1000);
    }
}
