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

    fun removeGold() {
        followTrajectory(arrayListOf(Pair(15.0, 0.0)))


        if (tfod.isGold()) {
            followTrajectory(arrayListOf(
                    Pair(40.0, 0.0),
                    Pair(-40.0, 0.0)
            ))

            goldPosition = TFOD.Positions.CENTER
            return
        }

        followTrajectory(arrayListOf(Pair(0.0, -35.0)))

        if (tfod.isGold()) {
            followTrajectory(arrayListOf(
                    Pair(50.0, -35.0),
                    Pair(-50.0, -35.0)
            ))

            goldPosition = TFOD.Positions.RIGHT
            return
        }

        followTrajectory(arrayListOf(Pair(0.0, 35.0),
                Pair(50.0, 35.0),
                Pair(-50.0, 35.0)))

        goldPosition = TFOD.Positions.LEFT
    }


    fun dropMarker() {
        hw.marker.marker.position = 1.0
        waitMillis(500)
        hw.marker.marker.position = 0.0
    }

}
