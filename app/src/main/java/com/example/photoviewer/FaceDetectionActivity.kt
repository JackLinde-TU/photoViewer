package com.example.photoviewer

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FaceDetectionActivity : AppCompatActivity() {

    fun imageFromPath(context: Context, uri: Uri) {
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun detectFaces(context: Context, image: InputImage, id: Int) {
        // Set detector options
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()

        // Get detector
        val detector = FaceDetection.getClient(options)

        // Run detector
        detector.process(image)
                .addOnSuccessListener { faces ->
                    Toast.makeText(context, "Successfully recognized face(s): ${faces.size}", Toast.LENGTH_LONG).show()
                    val bmp = image.bitmapInternal
                    val data = DatabaseHandler(context).readData()

                    for ((index,face) in faces.withIndex()) {
                        val bounds = face.boundingBox
                        val topLeftCornerX = bounds.left
                        val topLeftCornerY = bounds.top
                        val imageWidth = bounds.right - bounds.left
                        val imageHeight = bounds.bottom - bounds.top
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees
                        val faceBmp = Bitmap.createBitmap(bmp, topLeftCornerX, topLeftCornerY, imageWidth, imageHeight)
                        val faceBmp_cropped = Bitmap.createScaledBitmap(faceBmp, 122, 122, true)
                        Thread {
                            val fileNameCropped = "${data[id-1].name} ${File.pathSeparator} $index cropped.png"
                            context.openFileOutput(fileNameCropped, Context.MODE_PRIVATE).use {
                                faceBmp_cropped.compress(Bitmap.CompressFormat.PNG, 100, it)
                            }
//                            val imgFile = File(faceDir, "${index.toString()}.png")
//                            val outputStream = FileOutputStream(imgFile)
//                            faceBmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                            outputStream.close()
                        }.start()
                    }
                }
                .addOnFailureListener { e ->

                    Toast.makeText(context, "Failure: $e", Toast.LENGTH_SHORT).show()
                }

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
}