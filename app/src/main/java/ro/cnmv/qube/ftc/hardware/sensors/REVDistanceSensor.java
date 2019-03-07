package ro.cnmv.qube.ftc.hardware.sensors;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class REVDistanceSensor {
    //private Rev2mDistanceSensor sensor;

    public REVDistanceSensor(HardwareMap hwMap) {
//        sensor = (Rev2mDistanceSensor)hwMap.get(DistanceSensor.class, "distanceSensor");
    }

    public double getDistance() {
        return /*sensor.getDistance(DistanceUnit.CM);*/ 1;
    }
}
