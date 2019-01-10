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
    val SLIDEROPEN: Int = 2000
    val SLIDERCLOSE: Int = 0
    val MULTIPLIER: Int = 10

    val dropServo1 =  hwMap.servo["outTakeDrop1"] ?: throw Exception("Failed to find servo outTakeDrop1")
    val dropServo2 =  hwMap.servo["outTakeDrop2"] ?: throw Exception("Failed to find servo outTakeDrop2")
    val SERVODROP1: Double = 30.0
    val SERVOCLOSE1: Double = 160.0
    val SERVODROP2: Double = 0.0
    val SERVOCLOSE2: Double = 160.0

    val THRESHOLD: Int = 10 /// TODO: To be tested

    init {
        outTakeSlider.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outTakeSlider.direction = DcMotorSimple.Direction.FORWARD
        outTakeSlider.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        outTakeSlider.mode = DcMotor.RunMode.RUN_USING_ENCODER

        outTakeSlider.power = 0.0
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
        dropServo1.position = when(drop) {
            true -> SERVODROP1
            false -> SERVOCLOSE1
        }
        dropServo2.position = when(drop) {
            true -> SERVODROP2
            false -> SERVOCLOSE2
        }
    }

    fun stop() {
        outTakeSlider.power = 0.0
    }

    fun close() {
        dropServo1.position = SERVOCLOSE1
        dropServo2.position = SERVOCLOSE2
        outTakeSlider.targetPosition = SLIDERCLOSE
        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }
    fun isClosed(): Boolean {
        return outTakeSlider.currentPosition < THRESHOLD
    }
}
