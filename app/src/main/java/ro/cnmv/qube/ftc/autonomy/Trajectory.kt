package ro.cnmv.qube.ftc.Autonomy

import com.acmerobotics.dashboard.config.Config
import java.util.*
import kotlin.collections.ArrayList

@Config
object Trajectory {
    @JvmField var distance1 = 0.0
    @JvmField var heading1 = 0.0
    @JvmField var distance2 = 0.0
    @JvmField var heading2 = 0.0
    @JvmField var distance3 = 0.0
    @JvmField var heading3 = 0.0
    @JvmField var distance4 = 0.0
    @JvmField var heading4 = 0.0
    @JvmField var distance5 = 0.0
    @JvmField var heading5 = 0.0
    
    
    fun asArray() : ArrayList< Pair< Double, Double >> {
        val ret = arrayListOf(Pair(distance1, heading1), Pair(distance2, heading2), Pair(distance3, heading3), Pair(distance4, heading4), Pair(distance5, heading5))
        
        return ret
    }
}
