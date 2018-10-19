package ro.cnmv.qube.ftc.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.DcMotor
import ro.cnmv.qube.ftc.OpMode
import ro.cnmv.qube.ftc.hardware.Hardware

@Autonomous(name = "Encoder test", group = "Tests")
class EncoderTest: OpMode() {
    override fun Hardware.run() {
        val motor = hardwareMap.get(DcMotor::class.java, "leftMotorEncoder")
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER

        while (opModeIsActive()) {
            telemetry.addData("Position", motor.currentPosition)
            telemetry.update()
        }
    }
}
