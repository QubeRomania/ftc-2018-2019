package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object StrafePID {
    @JvmField var p : Double = 0.05
    @JvmField var i : Double = 0.0
    @JvmField var d : Double = 0.01
    @JvmField var dist : Double = 0.1
    @JvmField var ratio : Double = 10.1
}
