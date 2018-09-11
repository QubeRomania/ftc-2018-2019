package ro.cnmv.qube.ftc

import android.app.Activity
import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.bytedeco.javacv.AndroidFrameConverter
import org.bytedeco.javacv.OpenCVFrameGrabber

@Autonomous(name = "OpenCV", group = "Tests")
class OpenCVTest: LinearOpMode() {
    override fun runOpMode() {
        val log = telemetry.log()

        waitForStart()

        log.add("Starting OpenCV test")

        // Retrieve the Android application context
        val ctx = hardwareMap.appContext

        log.add("Setting up camera grabber")
        // Use the camera device indicated by this ID
        val cameraId = 0
        val grabber = OpenCVFrameGrabber(cameraId)
        val converter = AndroidFrameConverter()

        log.add("Capturing a frame")

        // Start video capture, grab a single frame
        grabber.start();
        val frame = grabber.grab()
        grabber.stop()

        log.add("Setting up camera display")

        val activity = ctx as Activity

        val cameraMonitorViewId = ctx.resources.getIdentifier("cameraMonitorViewId", "id", ctx.packageName)
        val cameraMonitorView = activity.findViewById(cameraMonitorViewId) as LinearLayout

        // Create a new ImageView to display the converted image
        val imageView = ImageView(ctx)
        val bitmap = converter.convert(frame)
        imageView.setImageBitmap(bitmap)

        cameraMonitorView.addView(imageView)

        val seconds = 2
        log.add("Waiting for %f seconds", seconds)

        val elapsed = ElapsedTime()
        while (opModeIsActive() && elapsed.seconds() < seconds) {
            sleep(25)
        }
    }
}
