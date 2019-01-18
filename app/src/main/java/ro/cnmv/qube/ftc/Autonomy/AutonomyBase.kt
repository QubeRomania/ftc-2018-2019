package ro.cnmv.qube.ftc.Autonomy

import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD


abstract class AutonomyBase : OpMode() {
    val tfod: TFOD by lazy {
        TFOD(hardwareMap, telemetry)
    }

    var goldPosition = TFOD.Positions.UNKNOWN

    init {

    }

    fun followTrajectory(steps : ArrayList<Pair<Double, Double>>){
        steps.forEach { follow(it) }
    }

    fun follow(step : Pair< Double, Double >) {
        val(distance, heading) = step

        if(distance == 0.0) {
            rotateTo(heading)
        } else {
            goTo(distance, heading)
        }
    }

    fun sampling() {
        val now = tfod.getPosition()
        if(now != TFOD.Positions.UNKNOWN) goldPosition = now
    }

    fun dropMarker() {

    }

}
