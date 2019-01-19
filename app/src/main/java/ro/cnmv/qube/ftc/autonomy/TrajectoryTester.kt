package ro.cnmv.qube.ftc.autonomy

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.hardware.Hardware

@Autonomous(name = "Trajectory Tester", group = "Tests")
class TrajectoryTester : AutonomyBase() {

    override fun Hardware.run() {

        while (!isStarted) {
            sampling()
        }

        waitForStart()

        followTrajectory(Trajectory.asArray())
    }
}
