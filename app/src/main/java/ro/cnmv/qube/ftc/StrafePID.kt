package ro.cnmv.qube.ftc

import com.acmerobotics.dashboard.config.Config

@Config
object StrafePID {
    @JvmField var p : Double = 0.1
    @JvmField var i : Double = 0.1
    @JvmField var d : Double = 0.1
    @JvmField var ratio : Double = 1.1
}
