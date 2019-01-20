package ro.cnmv.qube.ftc.hardware

import com.qualcomm.robotcore.hardware.HardwareMap

class Marker(hwMap : HardwareMap) {
    val marker = hwMap.servo["marker"]

    init {
        marker.position = 0.0
    }
}
