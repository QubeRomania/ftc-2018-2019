package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Latching subsystem.
 *
 * This class controls the hardware for latching on / dropping from the lander.
 */

class Latcher (hwMap : HardwareMap) {
    val latchMotor = hwMap.dcMotor["latchMotor"] ?: throw Error("Failed to find motor latchMotor")

    val CLOSED: Int = TODO()
    val INTERMEDIATE: Int = TODO()
    val EXTENDED: Int = TODO()

    enum class LatchPosition {
        CLOSED, INTERMEDIATE, EXTENDED
    }

    init {
        latchMotor.direction = DcMotorSimple.Direction.FORWARD
        latchMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        latchMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun latch(position: LatchPosition) {
        latchMotor.targetPosition = when(position) {
            LatchPosition.CLOSED -> CLOSED
            LatchPosition.INTERMEDIATE -> INTERMEDIATE
            LatchPosition.EXTENDED -> EXTENDED
        }

        latchMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        latchMotor.power = 0.8
    }

    fun stop() {
        latchMotor.power = 0.0
    }

}

