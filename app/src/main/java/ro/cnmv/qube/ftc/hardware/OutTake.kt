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
    companion object {
        const val SLIDER_OPEN = 2000
        const val SLIDER_CLOSE = 0
        const val MULTIPLIER = 10

        const val SERVO_DROP_LEFT = 0.5
        const val SERVO_CLOSE_LEFT = 0.0
        const val SERVO_DROP_RIGHT = 0.45
        const val SERVO_CLOSE_RIGHT = 1.0

        const val SERVO_INTERMEDIATE_LEFT = 0.35
        const val SERVO_INTERMEDIATE_RIGHT = 0.6
        // TODO: To be tested
        const val THRESHOLD = 10
    }

    val outTakeSlider = hwMap.dcMotor["outTakeSlider"] ?: throw Exception("Failed to find motor outTakeSlider")

    var outTakePosition: Int = 0

    val dropLeft =  hwMap.servo["dropLeft"] ?: throw Exception("Failed to find servo outTakeDrop1")
    val dropRight =  hwMap.servo["dropRight"] ?: throw Exception("Failed to find servo outTakeDrop2")

    init {
        outTakeSlider.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outTakeSlider.direction = DcMotorSimple.Direction.FORWARD
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
        dropLeft.position = SERVO_INTERMEDIATE_LEFT
        dropRight.position = SERVO_INTERMEDIATE_RIGHT
    }
    fun stop() {
        outTakeSlider.power = 0.0
    }

    fun close() {
        dropLeft.position = SERVO_CLOSE_LEFT
        dropRight.position = SERVO_CLOSE_RIGHT
        outTakeSlider.targetPosition = SLIDER_CLOSE
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun isClosed(): Boolean {
        return outTakeSlider.currentPosition < THRESHOLD
    }
}
