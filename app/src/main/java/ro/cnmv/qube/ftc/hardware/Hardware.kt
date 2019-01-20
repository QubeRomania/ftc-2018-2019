package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import ro.cnmv.qube.ftc.hardware.sensors.Imu
import ro.cnmv.qube.ftc.hardware.sensors.REVDistanceSensor

class Hardware(hwMap: HardwareMap, opMode: LinearOpMode) {
    val motors = DriveMotors(hwMap)
    val intake = Intake(hwMap)
    val outTake = OutTake(hwMap)
    val latcher = Latcher(hwMap)
    val imu = Imu(hwMap)
    val distanceSensor = REVDistanceSensor(hwMap)
    val marker = Marker(hwMap)

    fun stop() {
        motors.stop()
        intake.stop()
        outTake.stop()
        latcher.stop()
    }
}
