package com.example.photoviewer

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Element
import androidx.appcompat.app.AppCompatActivity
import com.example.photoviewer.ml.MobileFaceNet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.schema.TensorType.FLOAT32
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class FaceRecognition : AppCompatActivity {

    fun RecognizeFace(context: Context, face: Bitmap) {
        val model = MobileFaceNet.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

// Releases model resources if no longer used.
        model.close()
    }

}