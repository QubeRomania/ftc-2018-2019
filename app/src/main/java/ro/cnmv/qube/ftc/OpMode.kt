package ro.cnmv.qube.ftc

import com.acmerobotics.roadrunner.drive.Drive
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

        if (correction.absoluteValue < 0.1 && correction.absoluteValue > 0.001)
            return 0.1 * correction.sign

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

        if (correction.absoluteValue < 0.1 && correction.absoluteValue > 0.001)
            return 0.1 * correction.sign

        return Range.clip(correction, -1.0, 1.0)
    }

    fun goTo(distanceCm: Double, targetHeading: Double) {
        rotateTo(targetHeading)
        with (hw.motors) {
            resetPosition()
            val target = distanceCm * 17.5
            setTargetPosition(target.toInt())
            var maxSpeed = 0.0
            runToPosition()
            while (opModeIsActive() && areBusy) {0
                maxSpeed = Math.min(maxSpeed + 0.01, 0.4)
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

    fun strafe(distanceCm: Double, targetHeading: Double){
        val pid = PIDCoefficients(StrafePID.p, StrafePID.i, StrafePID.d)

        with(hw.motors) {
            var lastError = 0.0
            var error = 0.0
            val timer = ElapsedTime()

            val totalTime = ElapsedTime()
            while(timer.milliseconds() < 300 && opModeIsActive() && totalTime.milliseconds() < 5000) {
                lastError = error
                error = (hw.distanceSensor.distance - distanceCm)

                val speed = (pid.p*error + pid.i*(error + lastError) + pid.d*(error - lastError)) / StrafePID.ratio

                val correction = getDriveHeadingCorrection(targetHeading)

                move(90.0, speed, correction)

                telemetry.addData("DistanceError", error)
                telemetry.addData("Speed", speed)
                telemetry.addData("HeadingCorrection", correction)

                telemetry.update()

                if(error > 1.0) timer.reset()
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

            if (absError > 0.1)
                lastTime = timer.milliseconds()

            telemetry.addData("Current", "%.2f", hw.imu.heading)
            telemetry.addData("Target", "%.2f", targetHeading)
            telemetry.addData("Rotation Correction", "%.2f", correction)
            telemetry.update()
        } while (opModeIsActive() && timer.milliseconds() - lastTime < 300)
        hw.motors.stop()
    }
}

fun LinearOpMode.waitMillis(millis: Long) {
    val timer = ElapsedTime()
    while (opModeIsActive() && timer.milliseconds() <= millis)
        idle()
}
