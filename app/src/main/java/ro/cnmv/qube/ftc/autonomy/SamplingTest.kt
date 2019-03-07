package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.TFOD

@Autonomous(name = "Sampling Test", group = "tests")
public class SamplingTest : AutonomyBase() {
    override fun Hardware.run() {

        waitForStart()

        var foundGold = false
        while (opModeIsActive()) {

            foundGold = tfod.isGold()

            telemetry.addLine(when(foundGold) {
                true -> "GOLD"
                false -> "NO GOLD"
            })
            telemetry.update()

            sleep(1000)
        }

        tfod.stop()
    }
}
