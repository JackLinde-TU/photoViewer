package com.example.photoviewer

import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
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

    fun detectFaces(context: Context, image: InputImage) {

        // Set detector options
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()

        // Get detector
        val detector = FaceDetection.getClient(options)

        // Run detector
        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                Toast.makeText(this, "Successfully recognized face(s)", Toast.LENGTH_SHORT).show()

                for (face in faces) {
//                    graphicOverlay.add(FaceGraphic(graphicOverlay, face))

//                    val bounds = face.boundingBox
//                    val rotY = face.headEulerAngleY
//                    val rotZ = face.headEulerAngleZ
//
//                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
//                    val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failure: $e", Toast.LENGTH_SHORT).show()
            }
    }
}