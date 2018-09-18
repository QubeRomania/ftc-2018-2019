package ro.cnmv.qube.ftc

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.LinearLayout
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.bytedeco.javacv.AndroidFrameConverter
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.javacpp.opencv_core.*
import org.bytedeco.javacpp.opencv_imgproc.*

@Autonomous(name = "OpenCV", group = "Tests")
class OpenCVTest: LinearOpMode() {
    override fun runOpMode() {
        val log = telemetry.log()

        // Retrieve the Android application context
        val ctx = hardwareMap.appContext
        val activity = ctx as Activity

        val cameraMonitorViewId = ctx.resources.getIdentifier("cameraMonitorViewId", "id", ctx.packageName)
        val cameraMonitorView = activity.findViewById(cameraMonitorViewId) as LinearLayout

        // Use the camera device indicated by this ID
        val cameraId = 0

        val camera = Camera.open(cameraId)
        camera.setDisplayOrientation(90)

        /*
        activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)

        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            throw Error("Camera permission not granted")
        }
        */

        ctx.runOnUiThread {
            log.add("Setting up camera display")

            val imagesLayout = LinearLayout(ctx)
            imagesLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            imagesLayout.orientation = LinearLayout.VERTICAL

            val surfaceView = SurfaceView(ctx)
            imagesLayout.addView(surfaceView)

            val imageView = ImageView(ctx)
            imagesLayout.addView(imageView)

            log.add("Setting up camera")

            val params = camera.parameters

            val picSizes = params.supportedPreviewSizes
            picSizes.forEach { Log.e("PicSize", "${it.width}x${it.height}") }

            val width = 640
            val height = 480

            with(params) {
                setPreviewSize(width, height)
                previewFormat = ImageFormat.NV21

                focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            }

            camera.parameters = params

            val surfaceHolder = surfaceView.holder
            surfaceHolder.setFixedSize(width, height)

            cameraMonitorView.addView(imagesLayout)

            surfaceHolder.addCallback(object: SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                }

                override fun surfaceCreated(holder: SurfaceHolder) {
                    camera.setPreviewDisplay(holder)

                    camera.startPreview()

                    val frameConverter = AndroidFrameConverter()
                    val converter = OpenCVFrameConverter.ToMat()

                    val rotated = Mat()
                    val gray = Mat()
                    val thresh = Mat()
                    val contours = MatVector()

                    camera.setPreviewCallback { buffer, _ ->
                        val frame = frameConverter.convert(buffer, width, height)
                        val image = converter.convertToMat(frame)

                        rotate(image, rotated, ROTATE_90_CLOCKWISE)

                        cvtColor(rotated, gray, COLOR_BGR2GRAY)

                        threshold(gray, thresh, 127.0, 255.0, THRESH_BINARY)

                        findContours(thresh, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE)

                        drawContours(rotated, contours, -1, Scalar(0.0, 255.0, 255.0, 255.0))

                        val newFrame = converter.convert(rotated)
                        val bitmap = frameConverter.convert(newFrame)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            })
        }

        waitForStart()

        log.add("Starting OpenCV test")

        val seconds = 4
        log.add("Waiting for %d seconds", seconds)

        val elapsed = ElapsedTime()
        while (opModeIsActive() && elapsed.seconds() < seconds) {
            sleep(25)
        }

        camera.stopPreview()
        camera.release()
    }
}
