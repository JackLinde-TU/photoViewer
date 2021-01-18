package com.example.photoviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            choosePhotoFromGallery()
//            if (etName.text.toString().isNotEmpty() && etAge.text.toString().isNotEmpty()) {
//                val user = User(etName.text.toString(), etAge.text.toString().toInt())
//                db.insertData(user)
//            } else {
//                Toast.makeText(context, "Please fill-in empty inputs.", Toast.LENGTH_SHORT).show()
//            }
//            val uri: Uri = Uri.parse("/Pictures/photoViewer/DSC_9868.JPG")
//            ivImage.setImageURI(uri)
        }

        btnRead.setOnClickListener {
            val data = db.readData()
//            tvResult.text = ""
//            for (i in 0 until data.size) {
//                tvResult.append(data[i].id.toString() + " " + data[i].name + " " + data[i].age + "\n")
//            }
        }

        btnDelete.setOnClickListener {
            db.deleteData()
            btnRead.performClick()
        }

    }

    private fun choosePhotoFromGallery() {
        val galleryIntent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, storagePermissionCode)
    }

    private fun checkPermission(permission: Array<String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permission, requestCode)
        }
    }

    fun saveImage(bitmap: Bitmap) {
        return
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == storagePermissionCode) {
            val contentUri = data?.data ?: return
            try {
                val source = ImageDecoder.createSource(this.contentResolver, contentUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                val path = saveImage(bitmap)
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                ivImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}