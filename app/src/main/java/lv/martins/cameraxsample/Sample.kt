package lv.martins.cameraxsample

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceView
import android.view.SurfaceHolder
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.widget.Button
import lv.martins.cameraxsample.R

class MainActivity2 : AppCompatActivity() {
    var camera: Camera? = null
    var surfaceView: SurfaceView? = null
    var surfaceHolder: SurfaceHolder? = null
    var captureImageButton: Button? = null
    var jpegCallback: PictureCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

//        captureImageButton = findViewById(R.id.captureImageButton);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView!!.holder
        jpegCallback = PictureCallback { data, camera ->
            // Save the image to storage here
        }
//        captureImageButton!!.setOnClickListener { camera!!.takePicture(null, null, jpegCallback) }
    }

    override fun onResume() {
        super.onResume()
        camera = Camera.open()
    }

    override fun onPause() {
        super.onPause()
        camera!!.release()
        camera = null
    }
}