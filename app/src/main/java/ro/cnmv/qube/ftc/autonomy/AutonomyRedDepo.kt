package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD
import ro.cnmv.qube.ftc.waitMillis
import java.util.*

@Autonomous(name = "AutonomyRedDepo", group = "Autonomies")
class AutonomyRedDepo: AutonomyBase() {

    val strafeDist = 10.0
    val timer = ElapsedTime()

    override fun preInit() {
        // TODO: object detection
        goldPosition = TFOD.Positions.RIGHT
        hw.marker.marker.position = 0.0
    }

    override fun preInitLoop() {
        sampling()
    }



    override fun Hardware.run() {
        // TODO: de-latch

        timer.reset()
        removeGold()

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        followTrajectory(arrayListOf(Pair(100.0, 65.0), Pair(0.0, -45.0)))

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        strafe (strafeDist, -45.0)

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        followTrajectory(arrayListOf(Pair(100.0, -45.0)))

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        strafe(strafeDist, -45.0)

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        followTrajectory(arrayListOf(Pair(0.0, -135.0)))

        dropMarker()

        waitMillis(500)

        followTrajectory(arrayListOf(Pair(0.0, -45.0)))
        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        strafe(strafeDist, -45.0)

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()

        followTrajectory(arrayListOf(Pair(-170.0, -45.0)))

        telemetry.addData("Elapsed time", timer.seconds())
        telemetry.update()
    }
}
