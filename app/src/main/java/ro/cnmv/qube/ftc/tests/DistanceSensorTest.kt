package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

@Autonomous
class DistanceSensorTest : OpMode() {
    override fun Hardware.run() {
        waitForStart()
        while(opModeIsActive()) {
            telemetry.addData("Distance","%.2f", distanceSensor.distance)
            telemetry.update()
        }
    }
}
