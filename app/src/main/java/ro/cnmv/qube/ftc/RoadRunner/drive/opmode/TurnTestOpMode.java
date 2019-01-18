package ro.cnmv.qube.ftc.RoadRunner.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import ro.cnmv.qube.ftc.Gamepad;
import ro.cnmv.qube.ftc.RoadRunner.drive.SampleMecanumDriveBase;
import ro.cnmv.qube.ftc.RoadRunner.drive.SampleMecanumDriveREV;
import ro.cnmv.qube.ftc.RoadRunner.util.DashboardUtil;

/*
 * This is a simple routine to test turning capabilities. If this is consistently overshooting or
 * undershooting by a significant amount, re-run TrackWidthCalibrationOpMode.
 */
@Config
@Autonomous
public class TurnTestOpMode extends LinearOpMode {
    public static double currentAngle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);

        Gamepad gp1 = new Gamepad(gamepad1);

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {

            if(gp1.checkToggle(Gamepad.Button.B)) {
                currentAngle += 90;
            }
            if(gp1.checkToggle(Gamepad.Button.Y)) {
                currentAngle -= 90;
            }
            if(gp1.checkToggle(Gamepad.Button.A)) {
                Trajectory trajectory = drive.trajectoryBuilder()
                        .turnTo(currentAngle / 180.0 * Math.PI)
                        .build();
                drive.followTrajectory(trajectory);

                while(!isStopRequested() && drive.isFollowingTrajectory()) {
                    Pose2d currentPose = drive.getPoseEstimate();

                    TelemetryPacket packet = new TelemetryPacket();
                    Canvas fieldOverlay = packet.fieldOverlay();

                    packet.put("target", currentAngle);
                    packet.put("x", currentPose.getX());
                    packet.put("y", currentPose.getY());
                    packet.put("heading", currentPose.getHeading() * 180.0 / Math.PI);

                    fieldOverlay.setStrokeWidth(4);
                    fieldOverlay.setStroke("green");
                    DashboardUtil.drawSampledTrajectory(fieldOverlay, trajectory);

                    fieldOverlay.setFill("blue");
                    fieldOverlay.fillCircle(currentPose.getX(), currentPose.getY(), 3);

                    dashboard.sendTelemetryPacket(packet);

                    drive.update();
                }

                drive.setMotorPowers(0, 0, 0, 0);
            }



            TelemetryPacket packet = new TelemetryPacket();
            packet.put("heading", ((SampleMecanumDriveREV) drive).imu.getAngularOrientation().firstAngle * 180.0 / Math.PI);
            packet.put("current", currentAngle);
            dashboard.sendTelemetryPacket(packet);
        }
    }
}
