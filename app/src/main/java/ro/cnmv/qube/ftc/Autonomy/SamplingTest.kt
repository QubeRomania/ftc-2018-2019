package ro.cnmv.qube.ftc.Autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD

@Autonomous(name = "Sampling Test", group = "tests")
public class SamplingTest : AutonomyBase() {
    override fun Hardware.run() {

        waitForStart()

        var currentPosition = TFOD.Positions.UNKNOWN
        while (opModeIsActive()) {
            var now = tfod.getPosition()
            if(now != TFOD.Positions.UNKNOWN) currentPosition = now

            val pos = when(currentPosition) {
                TFOD.Positions.LEFT -> "Left"
                TFOD.Positions.RIGHT -> "Right"
                TFOD.Positions.CENTER -> "Center"
                else -> "UNKNOWN"
            }

            telemetry.update()
            sleep(1000)

            telemetry.addData("Position", pos)
            telemetry.update()
        }

        tfod.stop()
    }
}
