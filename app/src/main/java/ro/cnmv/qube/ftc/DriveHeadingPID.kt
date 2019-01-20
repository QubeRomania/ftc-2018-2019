package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object DriveHeadingPID {
    @JvmField val p: Double = 1.8
    @JvmField val i: Double = 0.3
    @JvmField val d: Double = 1.0
}
