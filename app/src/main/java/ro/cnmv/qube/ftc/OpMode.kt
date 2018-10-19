package ro.cnmv.qube.ftc

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import ro.cnmv.qube.ftc.hardware.Hardware

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
}

fun LinearOpMode.waitMillis(millis: Long) {
    val timer = ElapsedTime()
    while (opModeIsActive() && timer.milliseconds() <= millis)
        idle()
}
