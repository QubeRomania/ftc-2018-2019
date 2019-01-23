package ro.cnmv.qube.ftc.autonomy

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import ro.cnmv.qube.ftc.hardware.Hardware

@Autonomous(name = "Trajectory Tester", group = "Tests")
class TrajectoryTester : AutonomyBase() {
    override fun preInit() {
        FtcDashboard.start()
    }

    override fun Hardware.run() {
        while (!isStarted) {
        }

        waitForStart()

        followTrajectory(Trajectory.asArray())
    }
}
