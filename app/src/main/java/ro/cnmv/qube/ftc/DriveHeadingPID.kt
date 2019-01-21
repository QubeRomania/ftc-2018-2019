package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object DriveHeadingPID {
    @JvmField var p: Double = 1.8
    @JvmField var i: Double = 0.3
    @JvmField var d: Double = 1.0
}
