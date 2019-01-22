package ro.cnmv.qube.ftc.hardware

import com.acmerobotics.dashboard.config.Config

@Config
object dropPos {
    @JvmField var Position = 0.45
    var left = Position
    var right = 1- Position
}
