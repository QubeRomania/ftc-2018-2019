package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.PIDCoefficients
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import ro.cnmv.qube.ftc.hardware.Hardware
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class OpMode: LinearOpMode() {
    protected val hw by lazy {
        Hardware(hardwareMap, this)
    }

    final override fun runOpMode() {
        hw.stop()

        preInit()

        waitForStart()

        if (!opModeIsActive())
            return

        hw.run()

        hw.stop()
    }

    open fun preInit() {}

    /// Runs the op mode.
    abstract fun Hardware.run()

    private var lastRotationError = 0.0

    fun getHeadingCorrection(targetHeading: Double): Double {
        val pid = PIDCoefficients(0.8, 0.0, 1.5)

        // Determine the rotation error.
        val error = (targetHeading - hw.imu.heading) / 90.0

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
        with (hw.motors) {
            resetPosition()
            setTargetPosition(distanceCm.toInt())
            var maxSpeed = 0.0
            runToPosition()
            while (opModeIsActive() && areBusy) {
                maxSpeed = Math.min(maxSpeed + 0.01, 0.4)
                val correction = getHeadingCorrection(targetHeading) * distanceCm.sign
                move(0.0, maxSpeed, correction)

                printPosition(telemetry)
                telemetry.update()
            }
            stop()
            runWithConstantVelocity()
        }
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

            if (absError > 0.0)
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
