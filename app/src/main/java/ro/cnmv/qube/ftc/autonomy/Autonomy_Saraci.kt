package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD
import ro.cnmv.qube.ftc.waitMillis

@Autonomous
class Autonomy_Saraci : AutonomyBase() {
    val strafeDist = 11.0
    val timer = ElapsedTime()

    override fun preInit() {
        // TODO: object detection
        goldPosition = TFOD.Positions.UNKNOWN
        hw.marker.marker.position = 0.0
    }

    override fun preInitLoop() {
        telemetry.addData("GoldPos", goldPosition)
        telemetry.addLine("READY")
        telemetry.update()
    }

    override fun Hardware.run() {

        timer.reset()

        removeGold()
        tfod.stop()

        followTrajectory(arrayListOf(Pair(-90.0, -115.0), Pair(0.0, -45.0)))
        strafe (1000, -45.0, strafeSpeed)
        followTrajectory(arrayListOf(Pair(-120.0, -45.0)))
        strafe(1000, -45.0, strafeSpeed)
        followTrajectory(arrayListOf(Pair(0.0, 45.0)))
        dropMarker()
        waitMillis(500)

        followTrajectory(when(goldPosition) {
            TFOD.Positions.CENTER -> arrayListOf(Pair(-40.0, 45.0), Pair (40.0, 45.0))
            TFOD.Positions.LEFT -> arrayListOf(Pair(-70.0,30.0), Pair (70.0, 30.0))
            else -> arrayListOf(Pair(-50.0, 100.0), Pair(50.0, 100.0))
        })
        followTrajectory(arrayListOf(Pair(0.0, -45.0)))
        strafe(1000, -45.0, strafeSpeed)
        followTrajectory(arrayListOf(Pair(160.0, -45.0)))

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()
        waitMillis(1000);
    }
}
