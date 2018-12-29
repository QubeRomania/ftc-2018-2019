package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * OutTake subsystem.
 *
 * This class controls the hardware for placing minerals in the lander.
 */
class OutTake(hwMap: HardwareMap) {
    val outTakeSlider = hwMap.dcMotor["outTakeSlider"] ?: throw Exception("Failed to find motor outTakeSlider")

    var outTakePosition: Int = 0
    val SLIDEROPEN: Int = 2500
    val SLIDERCLOSE: Int = 0
    val MULTIPLIER: Int = TODO()

    val dropServo =  hwMap.servo["outTakeDrop"] ?: throw Exception("Failed to find servo outTakeDrop")
    val SERVODROP: Double = TODO()
    val SERVOCLOSE: Double = TODO()

    val THRESHOLD: Int = 10 /// TODO: To be tested

    init {
        outTakeSlider.direction = DcMotorSimple.Direction.REVERSE
        outTakeSlider.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        outTakeSlider.mode = DcMotor.RunMode.RUN_USING_ENCODER

        outTakePosition = 0
    }

    fun moveSlider(power: Double) {
        outTakePosition += (power*MULTIPLIER).toInt()
        outTakePosition = Math.min(outTakePosition, SLIDEROPEN)
        outTakePosition = Math.max(outTakePosition, SLIDERCLOSE)

        outTakeSlider.targetPosition = outTakePosition
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun dropMinerals(drop: Boolean) {
        dropServo.position = when(drop) {
            true -> SERVODROP
            false -> SERVOCLOSE
        }
    }

    fun stop() {
        dropServo.position = SERVOCLOSE
        outTakeSlider.targetPosition = SLIDERCLOSE
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun isClosed(): Boolean {
        return outTakeSlider.currentPosition < THRESHOLD
    }
}
