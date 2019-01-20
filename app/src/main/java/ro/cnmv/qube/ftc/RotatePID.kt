package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object RotatePID {
    @JvmField val p: Double = 0.8
    @JvmField val i: Double = 0.1
    @JvmField val d: Double = 1.5
}
