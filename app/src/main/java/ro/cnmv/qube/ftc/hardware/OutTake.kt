package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.absoluteValue

/**
 * OutTake subsystem.
 *
 * This class controls the hardware for placing minerals in the lander.
 */
class OutTake(hwMap: HardwareMap) {
    companion object {
        const val SLIDER_OPEN = 8000
        const val SLIDER_CLOSE = 0
        const val DELTA = 300
        const val DELTA2 = 1000
        const val MULTIPLIER = 40

        const val SERVO_DROP_LEFT = 1.0
        const val SERVO_CLOSE_LEFT = 0.0
        const val SERVO_DROP_RIGHT = 0.0
        const val SERVO_CLOSE_RIGHT = 1.0

        var SERVO_INTERMEDIATE_LEFT = dropPos.left
        var SERVO_INTERMEDIATE_RIGHT = dropPos.right
        // TODO: To be tested
        const val THRESHOLD = 10
    }

    val outTakeSlider = hwMap.dcMotor["outTakeSlider"] ?: throw Exception("Failed to find motor outTakeSlider")

    var outTakePosition: Int = 0

    val dropLeft =  hwMap.servo["dropLeft"] ?: throw Exception("Failed to find servo outTakeDrop1")
    val dropRight =  hwMap.servo["dropRight"] ?: throw Exception("Failed to find servo outTakeDrop2")

    init {
        outTakeSlider.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outTakeSlider.direction = DcMotorSimple.Direction.REVERSE
        outTakeSlider.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        outTakeSlider.mode = DcMotor.RunMode.RUN_USING_ENCODER

        outTakeSlider.power = 0.0
        outTakePosition = 0
    }

    fun moveSlider(power: Double) {
        outTakePosition += (power * MULTIPLIER).toInt()
        outTakePosition = Math.min(outTakePosition, SLIDER_OPEN)
        outTakePosition = Math.max(outTakePosition, SLIDER_CLOSE)

        outTakeSlider.targetPosition = outTakePosition
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun dropMinerals(drop: Boolean) {
        dropLeft.position = when(drop) {
            true -> SERVO_DROP_LEFT
            false -> SERVO_CLOSE_LEFT
        }
        dropRight.position = when(drop) {
            true -> SERVO_DROP_RIGHT
            false -> SERVO_CLOSE_RIGHT
        }
    }

    fun intermediatePosition() {
        if (outTakePosition > DELTA && outTakePosition < SLIDER_OPEN - DELTA2) {
            dropLeft.position = SERVO_INTERMEDIATE_LEFT
            dropRight.position = SERVO_INTERMEDIATE_RIGHT
        } else {
            dropLeft.position = SERVO_CLOSE_LEFT
            dropRight.position = SERVO_CLOSE_RIGHT
        }
    }
    fun stop() {
        outTakeSlider.power = 0.0
    }

    fun close() {
        dropLeft.position = SERVO_CLOSE_LEFT
        dropRight.position = SERVO_CLOSE_RIGHT
        outTakeSlider.targetPosition = SLIDER_CLOSE
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 1.0
    }

    fun isClosed(): Boolean {
        return outTakeSlider.currentPosition < THRESHOLD
    }
}
