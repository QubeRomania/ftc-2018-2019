package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.*
import org.bytedeco.javacpp.avformat

/**
* Intake motors and servos subsystem
*
* This class controls the hardware which takes the minerals
 */

class Intake (hwMap: HardwareMap) {
    val slideMotor = hwMap.dcMotor.get("slideMotor") ?: throw Exception("Failed to find motor slideMotor")
    val rotateMotor = hwMap.dcMotor.get("rotateMotor") ?: throw Exception("Failed to find motor rotateMotor")
    val maturicaServo1 = hwMap.crservo.get("intakeServo1") ?: throw Exception("Failed to find crservo intakeServo1")
    val maturicaServo2 = hwMap.crservo.get("intakeServo2") ?: throw Exception("Failed to find crservo intakeServo2")

    init {
        slideMotor.direction = DcMotorSimple.Direction.FORWARD
        slideMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        stopSlide()
        slideMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER

        rotateMotor.direction = DcMotorSimple.Direction.FORWARD
        rotateMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        stopRotate()
        rotateMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rotateMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER

        maturicaServo1.direction = DcMotorSimple.Direction.FORWARD
        maturicaServo2.direction = DcMotorSimple.Direction.REVERSE
        stopMaturica()


    }

    /// Slide Motor functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun runSlideToPosition(position: Int, power: Double) {
        slideMotor.targetPosition = position
        slideMotor.power = power
    }

    fun stopSlide() {
        slideMotor.power = 0.0
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    /// Rotate Motor functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun runRotateToPosition(position: Int, power: Double) {
        rotateMotor.targetPosition = position
        rotateMotor.power = power
    }

    fun stopRotate() {
        rotateMotor.power = 0.0
    }

    enum class Mode{
        OPEN,
        CLOSE,
        TRANSFER
    }

    val OPEN_POSITION: Int = TODO()
    val CLOSE_POSITION: Int =  TODO()
    val TRANSFER_POSITION: Int = TODO()
    val MODE_POWER: Double = TODO()

    //Set Mode
    fun setMode(mode: Mode) {
        val pos = when(mode) {
            Mode.OPEN -> OPEN_POSITION
            Mode.CLOSE -> CLOSE_POSITION
            Mode.TRANSFER -> TRANSFER_POSITION
        }
        runRotateToPosition(pos, MODE_POWER)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    /// Maturica functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun setMaturicaPower(power: Double) {
        maturicaServo1.power = power
        maturicaServo2.power = power
    }

    fun stopMaturica() {
        maturicaServo1.power = 0.0
        maturicaServo2.power = 0.0
    }

    val POWER = 1.0
    fun maturica(af: Boolean) {
        if (af) setMaturicaPower(POWER)
        else setMaturicaPower(0.0)
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
