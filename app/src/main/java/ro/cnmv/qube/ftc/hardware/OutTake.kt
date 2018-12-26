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

    val sliderOpen: Int = TODO()
    val sliderClose: Int = TODO()

    val dropServo =  hwMap.servo["outTakeDrop"] ?: throw Exception("Failed to find servo outTakeDrop")
    val servoDrop: Double = TODO()
    val servoClose: Double = TODO()

    init {
        outTakeSlider.direction = DcMotorSimple.Direction.FORWARD
        outTakeSlider.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        outTakeSlider.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun moveSlider(open : Boolean) {
        outTakeSlider.targetPosition = when(open) {
            true -> sliderOpen
            false -> sliderClose
        }

        outTakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
        outTakeSlider.power = 0.8
    }

    fun dropMinerals(drop: Boolean) {
        dropServo.position = when(drop) {
            true -> servoDrop
            false -> servoClose
        }
    }
}
