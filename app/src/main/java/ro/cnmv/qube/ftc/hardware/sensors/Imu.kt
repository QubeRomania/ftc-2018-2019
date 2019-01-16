package ro.cnmv.qube.ftc.hardware.sensors

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.HardwareMap

class Imu (hwMap: HardwareMap){
    val imu = hwMap.get(BNO055IMU::class.java, "imu")

    val heading get() = imu.getAngularOrientation().firstAngle

    init {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        imu.initialize(parameters)
    }

}
