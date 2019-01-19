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

        const val SERVO_DROP1 = 30.0
        const val SERVO_CLOSE1 = 160.0
        const val SERVO_DROP2 = 0.0
        const val SERVO_CLOSE2 = 160.0

        // TODO: To be tested
        const val THRESHOLD = 10
    }

    val outTakeSlider = hwMap.dcMotor["outTakeSlider"] ?: throw Exception("Failed to find motor outTakeSlider")

    var outTakePosition: Int = 0

    val dropServo1 =  hwMap.servo["outTakeDrop1"] ?: throw Exception("Failed to find servo outTakeDrop1")
    val dropServo2 =  hwMap.servo["outTakeDrop2"] ?: throw Exception("Failed to find servo outTakeDrop2")

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
        dropServo1.position = when(drop) {
            true -> SERVO_DROP1
            false -> SERVO_CLOSE1
        }
        dropServo2.position = when(drop) {
            true -> SERVO_DROP2
            false -> SERVO_CLOSE2
        }
    }

    fun stop() {
        outTakeSlider.power = 0.0
    }

    fun close() {
        dropServo1.position = SERVO_CLOSE1
        dropServo2.position = SERVO_CLOSE2
        outTakeSlider.targetPosition = SLIDER_CLOSE
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun isClosed(): Boolean {
        return outTakeSlider.currentPosition < THRESHOLD
    }
}
