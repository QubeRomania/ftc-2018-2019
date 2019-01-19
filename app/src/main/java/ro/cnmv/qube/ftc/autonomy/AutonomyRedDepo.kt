package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD

@Autonomous(name = "AutonomyRedDepo", group = "Autonomies")
class AutonomyRedDepo: AutonomyBase() {
    override fun preInit() {
        // TODO: object detection
        goldPosition = TFOD.Positions.CENTER
    }

    override fun Hardware.run() {
        // TODO: de-latch

        val trajectory = when (goldPosition) {
            TFOD.Positions.LEFT -> arrayListOf(
                Pair(15.0, 0.0),
                Pair(70.0, 30.0),
                Pair(-70.0, 30.0),
                Pair(0.0, 0.0)
            )
            TFOD.Positions.RIGHT -> arrayListOf(
                Pair(15.0, 0.0),
                Pair(70.0, -30.0),
                Pair(-70.0, -30.0),
                Pair(0.0, 0.0)
            )
            else -> arrayListOf(
                Pair(15.0, 0.0),
                Pair(70.0, 0.0),
                Pair(-70.0, 0.0),
                Pair(0.0, 0.0)
            )
        }

        followTrajectory(trajectory)

        followTrajectory(Trajectory.asArray())
    }
}
