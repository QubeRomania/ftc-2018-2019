package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.hardware.Servo
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.sensors.TFOD
import ro.cnmv.qube.ftc.waitMillis
import java.io.File

abstract class AutonomyBase : OpMode() {
    val tfod: TFOD by lazy {
        TFOD(hardwareMap, telemetry)
    }

    var goldPosition = TFOD.Positions.UNKNOWN

    init {
    }

    protected fun followTrajectory(steps: ArrayList<Pair<Double, Double>>){
        steps.forEach { follow(it) }
    }

    private fun follow(step: Pair<Double, Double>) {
        val (distance, heading) = step
        goTo(distance, heading)
    }

    fun sampling() {
        val now = tfod.position
        if(now != TFOD.Positions.UNKNOWN) goldPosition = now
    }

    fun removeGold() {
        val trajectory = when (goldPosition) {
            TFOD.Positions.LEFT -> arrayListOf(
                    Pair(15.0, 0.0),
                    Pair(50.0, 30.0),
                    Pair(-50.0, 30.0)
            )
            TFOD.Positions.RIGHT -> arrayListOf(
                    Pair(15.0, 0.0),
                    Pair(50.0, -30.0),
                    Pair(-50.0, -30.0)
            )
            else -> arrayListOf(
                    Pair(15.0, 0.0),
                    Pair(50.0, 0.0),
                    Pair(-50.0, 0.0)
            )
        }

        followTrajectory(trajectory)
    }


    fun dropMarker() {
        hw.marker.marker.position = 1.0
        waitMillis(500)
        hw.marker.marker.position = 0.0
    }

}
