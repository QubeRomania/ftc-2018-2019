package ro.cnmv.qube.ftc

import com.acmerobotics.roadrunner.drive.Drive
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.PIDCoefficients
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import ro.cnmv.qube.ftc.hardware.Hardware
import ro.cnmv.qube.ftc.hardware.sensors.REVDistanceSensor
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class OpMode: LinearOpMode() {

    protected val hw by lazy {
        Hardware(hardwareMap, this)
    }

    val maxTimePerRotate = 1300
    val maxTimePerStrafe = 1500

    final override fun runOpMode() {
        hw.stop()

        preInit()

        while(!isStarted && !isStopRequested) {
            preInitLoop()
        }

        if(isStopRequested) return

        if (!opModeIsActive())
            return

        hw.run()

        hw.stop()
    }

    open fun preInit() {}

    open fun preInitLoop() {}

    /// Runs the op mode.
    abstract fun Hardware.run()

    private var lastRotationError = 0.0

    fun getHeadingCorrection(targetHeading: Double): Double {
        val pid = PIDCoefficients(RotatePID.p, RotatePID.i, RotatePID.d)

        // Determine the rotation error.
        var delta = targetHeading - hw.imu.heading

        if(delta.absoluteValue > 180.0) delta = -delta.sign*360.0 + delta

        val error = delta / 90.0

        // Calculate the PID.
        val correction = (pid.p * error)
        + (pid.i * (error + lastRotationError))
        + (pid.d * (error - lastRotationError))

        lastRotationError = error

        if (correction.absoluteValue < RotatePID.threshold)
            return RotatePID.slow * correction.sign

        return Range.clip(correction, -1.0, 1.0)
    }

    fun getDriveHeadingCorrection(targetHeading: Double): Double {
        val pid = PIDCoefficients(DriveHeadingPID.p, DriveHeadingPID.i, DriveHeadingPID.d)

        // Determine the rotation error.
        var delta = targetHeading - hw.imu.heading

        if(delta.absoluteValue > 180.0) delta = -delta.sign*360.0 + delta

        val error = delta / 90.0

        // Calculate the PID.
        val correction = (pid.p * error)
        + (pid.i * (error + lastRotationError))
        + (pid.d * (error - lastRotationError))

        lastRotationError = error
        return Range.clip(correction, -1.0, 1.0)
    }

    fun goTo(distanceCm: Double, targetHeading: Double) {
        if((targetHeading - hw.imu.heading).absoluteValue > 5)
            rotateTo(targetHeading)
        with (hw.motors) {
            resetPosition()
            val target = distanceCm * 17.5
            setTargetPosition(target.toInt())
            var maxSpeed = 0.0
            runToPosition()
            while (opModeIsActive() && areBusy) {
                maxSpeed = Math.min(maxSpeed + 0.1, 0.7)
                val correction = getDriveHeadingCorrection(targetHeading) * distanceCm.sign
                telemetry.addData("Heading error", correction)
                move(0.0, maxSpeed, correction)

                printPosition(telemetry)
                telemetry.update()
            }

            stop()
            runWithConstantVelocity()
        }
    }

    fun strafe(maxTime: Int, targetHeading: Double, speed: Double){
        with(hw.motors) {
            val timer = ElapsedTime()

            while(timer.milliseconds() < maxTime && opModeIsActive()) {

                val correction = getDriveHeadingCorrection(targetHeading)
                move(90.0, speed, correction)

                telemetry.addData("Speed", speed)
                telemetry.addData("timeLeft", maxTime - timer.milliseconds())
                telemetry.addData("HeadingCorrection", correction)

                telemetry.update()
            }
        }
        return
    }

    fun runWithVelocity(velocity: Double, time: Long) {
        with (hw.motors) {
            runWithConstantVelocity()
            translate(0.0, velocity)
            val timer = ElapsedTime()
            while (opModeIsActive() && timer.milliseconds() < time)
            ;
            stop()
        }
    }

    fun rotateTo(targetHeading: Double) {
        hw.motors.runWithConstantVelocity()
        val timer = ElapsedTime()
        var lastTime = timer.milliseconds()
        do {
            val correction = getHeadingCorrection(targetHeading)
            hw.motors.rotate(correction)

            val absError = (targetHeading - hw.imu.heading).absoluteValue

            if (absError > 1.0)
                lastTime = timer.milliseconds()

            telemetry.addData("Current", "%d", hw.imu.heading)
            telemetry.addData("Target", "%.2f", targetHeading)
            telemetry.addData("Rotation Correction", "%.2f", correction)
            telemetry.update()

            if (timer.milliseconds() > maxTimePerRotate) break;

        } while (opModeIsActive() && timer.milliseconds() - lastTime < 300)
        hw.motors.stop()
    }
}

fun LinearOpMode.waitMillis(millis: Long) {
    val timer = ElapsedTime()
    while (opModeIsActive() && timer.milliseconds() <= millis)
        idle()
}
