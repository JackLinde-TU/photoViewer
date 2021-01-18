package com.example.photoviewer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.google.mlkit.vision.face.*

class DrawRect(context: Context, var faceObject: List<Face>) : View(context) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val pen = Paint()
        for (face in faceObject) {
            pen.color = Color.GREEN
            pen.strokeWidth = 8f
            pen.style = Paint.Style.STROKE
            val box = face.boundingBox
            canvas?.drawRect(box, pen)
        }
    }
}