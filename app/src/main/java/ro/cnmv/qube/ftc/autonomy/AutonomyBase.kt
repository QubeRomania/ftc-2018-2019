package ro.cnmv.qube.ftc.autonomy

import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.sensors.TFOD

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

    fun dropMarker() {

    }

}
