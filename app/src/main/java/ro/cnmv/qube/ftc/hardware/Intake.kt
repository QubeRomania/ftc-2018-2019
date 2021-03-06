package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.*
import org.bytedeco.javacpp.avformat

/**
* Intake motors and servos subsystem
*
* This class controls the hardware which takes the minerals
 */

class Intake (hwMap: HardwareMap) {
    val slideMotor = hwMap.dcMotor.get("intakeSlideMotor") ?: throw Exception("Failed to find motor intakeSlideMotor")
    val rotateMotor = hwMap.dcMotor.get("intakeRotateMotor") ?: throw Exception("Failed to find motor intakeRotateMotor")
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
        rotateMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rotateMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        stopRotate()

        maturicaServo1.direction = DcMotorSimple.Direction.FORWARD
        maturicaServo2.direction = DcMotorSimple.Direction.REVERSE
        stopMaturica()


    }

    /// Slide Motor functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    var pos = 0
    val MULTIPLIER: Int = 200
    val SLIDER_CLOSE: Int = 0
    val SLIDER_OPEN: Int = 3000


    fun runSlideToPosition(position: Int, power: Double) {
        slideMotor.targetPosition = position
        slideMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        slideMotor.power = power
    }

    fun moveSlider(power: Double) {
        pos += (power*MULTIPLIER).toInt()
        pos = Math.min(pos, SLIDER_OPEN)
        pos = Math.max(pos, SLIDER_CLOSE)

        runSlideToPosition(pos, 0.8)
    }

    fun stopSlide() {
        slideMotor.power = 0.0
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    /// Rotate Motor functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun runRotateToPosition(position: Int, power: Double) {
        rotateMotor.targetPosition = position
        rotateMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        rotateMotor.power = power
    }

    fun stopRotate() {
        rotateMotor.power = 0.0
    }

    enum class ModeRotate{
        OPEN,
        CLOSE,
        TRANSFER,
        DROP
    }

    val OPEN_POSITION: Int = 850
    val CLOSE_POSITION: Int =  0
    val TRANSFER_POSITION: Int = 200
    val DROP_POSITION: Int = 100
    val MODE_POWER: Double = 0.8
    val UP_POWER: Double = 0.3
    val DOWN_POWER: Double = 0.15

    fun rotate(mode: ModeRotate) {
        val pos = when(mode) {
            ModeRotate.OPEN -> OPEN_POSITION
            ModeRotate.CLOSE -> CLOSE_POSITION
            ModeRotate.TRANSFER -> TRANSFER_POSITION
            else -> DROP_POSITION
        }

        val power = when(mode) {
            ModeRotate.OPEN -> DOWN_POWER
            ModeRotate.DROP -> DOWN_POWER
            else -> UP_POWER
        }
        runRotateToPosition(pos, power)
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

    enum class ModeMaturica {
        IN,
        OUT,
        STOP
    }
    val POWER = 1.0
    fun maturica(mode: ModeMaturica) {
        val power = when(mode) {
            ModeMaturica.IN -> POWER
            ModeMaturica.OUT -> -POWER
            ModeMaturica.STOP -> 0.0
        }
        setMaturicaPower(power)
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun stop() {
        stopSlide()
        stopRotate()
        stopMaturica()
    }
}
