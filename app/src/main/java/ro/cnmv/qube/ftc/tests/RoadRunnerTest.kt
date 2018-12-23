package ro.cnmv.qube.ftc.tests

import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

/*
    This is a class to check if we can import roadRunner libraries
 */
@Autonomous(name = "RoadRunner Test", group = "Tests")
class RoadRunnerTest: OpMode() {
    override fun Hardware.run() {

        var test : TrajectoryBuilder
        waitForStart()

        while (opModeIsActive()) {
            sleep(1000)
        }
    }
}
