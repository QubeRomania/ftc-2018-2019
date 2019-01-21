package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object RotatePID {
    @JvmField var p: Double = 0.8
    @JvmField var i: Double = 0.1
    @JvmField var d: Double = 1.5
    @JvmField var angle: Double = 0.1
    @JvmField var slow: Double = 0.1
    @JvmField var threshold: Double = 0.1
}
