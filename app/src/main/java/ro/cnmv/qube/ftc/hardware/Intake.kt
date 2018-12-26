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
    val maturicaServo1 = hwMap.crservo.get("intakeServo1") ?: throw Exception("Failed to find crservo intakeServo1")
    val maturicaServo2 = hwMap.crservo.get("intakeServo2") ?: throw Exception("Failed to find crservo intakeServo2")

    init {
        setSlideDirection(DcMotorSimple.Direction.FORWARD)
        stopSlide()
        resetSlideEcndoer()

        setCrServoDirection(maturicaServo1, DcMotorSimple.Direction.FORWARD)
        setCrServoDirection(maturicaServo2, DcMotorSimple.Direction.REVERSE)
        stopMaturica()


    }

    /// SlideMotor functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Set Direction
    fun setSlideDirection(direction: DcMotorSimple.Direction) {
        slideMotor.direction = direction
    }

    //Reset Encoder
    fun resetSlideEcndoer() {
        slideMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    //Set Power
    fun setSlidePower(power: Double) {
        slideMotor.power = power
    }

    //Stop Motor
    fun stopSlide() {
        setSlidePower(0.0)
    }

    //Run To Position
    fun runSlideToPosition(position: Int, power: Double) {
        slideMotor.targetPosition = position
        slideMotor.power = power
    }

    val OPEN_POSITION: Int = TODO()
    val CLOSE_POSITION: Int =  TODO()
    val TRANSFER_POSITION: Int = TODO()
    val MODE_POWER: Double = TODO()

    enum class Mode{
        OPEN,
        CLOSE,
        TRANSFER
    }

    //Set Mode
    fun setMode(mode: Mode) {
        val pos = when(mode) {
            Mode.OPEN -> OPEN_POSITION
            Mode.CLOSE -> CLOSE_POSITION
            Mode.TRANSFER -> TRANSFER_POSITION
        }
        runSlideToPosition(pos, MODE_POWER)
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



    /// CrServo functions
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Set Direction
    fun setCrServoDirection(servo: CRServo, direction: DcMotorSimple.Direction) {
        servo.direction = direction
    }

    //Set Power
    fun setCrServoPower(servo: CRServo, power: Double) {
        servo.power = power
    }

    //Set System Power
    fun setMaturicaPower(power: Double) {
        maturicaServo1.power = power
        maturicaServo2.power = power
    }

    //Stop
    fun stopCrServo(servo: CRServo) {
        setCrServoPower(servo, 0.0)
    }

    //Stop System
    fun stopMaturica() {
        stopCrServo(maturicaServo1)
        stopCrServo(maturicaServo2)
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
