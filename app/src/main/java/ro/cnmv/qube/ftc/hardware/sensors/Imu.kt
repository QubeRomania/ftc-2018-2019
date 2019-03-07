package ro.cnmv.qube.ftc.hardware.sensors

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.Gyroscope
import com.qualcomm.robotcore.hardware.HardwareMap

class Imu (hwMap: HardwareMap){

    val imu = PhoneGyro(hwMap)

    val heading get() = imu.heading

    init {
        imu.calibrate()
    }

}
