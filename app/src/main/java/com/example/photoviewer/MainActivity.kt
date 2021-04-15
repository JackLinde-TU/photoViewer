package com.example.photoviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val storagePermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        val db = DatabaseHandler(context)

        btnInsert.setOnClickListener {
            checkPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionCode)
            if (etDesc.text.toString().isNotEmpty()) {
                choosePhotoFromGallery()
            } else {
                Toast.makeText(context, "Please enter a description.", Toast.LENGTH_LONG).show()
            }
        }

        btnRead.setOnClickListener {
            val data = db.readData()

            for (i in 0 until data.size) {

                /** Declare views */
                val imageView = ImageView(context)
                val textView = TextView(context)

                /** insert data into views */
                imageView.setImageBitmap(data[i].image)
                textView.text = data[i].desc

                /** set parameters for views */
                val params = LinearLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.layoutParams = params
                textView.layoutParams = params

                llDB.addView(imageView)
                llDB.addView(textView)

            }
        }

        btnDetect.setOnClickListener {
            val data = db.readData()
            ivDetectedFaces.setImageBitmap(data.last().image)
            val image = InputImage.fromBitmap(data.last().image, 0)
            val imgId = data.last().id
            FaceDetectionActivity().detectFaces(this, image, imgId)

        }

    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, storagePermissionCode)
    }

    private fun checkPermission(permission: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permission, requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == storagePermissionCode) {
            val contentUri = data?.data ?: return
            try {

                /** Run on background thread for better loading times */
                Thread {
                    val source = ImageDecoder.createSource(this.contentResolver, contentUri)
                    val bitmap = ImageDecoder.decodeBitmap(source)

                    /** Create a file in folder "Images" with an random name */
                    val randomString = (10000000000..99999999999).random()
                    val imgDir = getDir("Images", Context.MODE_PRIVATE)
                    if (!imgDir.exists()) {
                        imgDir.mkdir()
                    }
                    val img = File(imgDir, "$randomString.png")
                    val outputStream = FileOutputStream(img)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()

                    /** Insert data */
                    DatabaseHandler(this).insertData(randomString, img, etDesc.text.toString())
                }.start()
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}