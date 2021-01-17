package com.example.photoviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        val db = DatabaseHandler(context)

        btnInsert.setOnClickListener {
            if (etName.text.toString().isNotEmpty() && etAge.text.toString().isNotEmpty()) {
                val user = User(etName.text.toString(), etAge.text.toString().toInt())
                db.insertData(user)
            } else {
                Toast.makeText(context, "Please fill-in empty inputs.", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener {
            val data = db.readData()
            tvResult.text = ""
            for (i in 0 until data.size) {
                tvResult.append(data[i].id.toString() + " " + data[i].name + " " + data[i].age + "\n")
            }
        }

        btnUpdate.setOnClickListener{
            db.updateData()
            btnRead.performClick()
        }

        btnDelete.setOnClickListener {
            db.deleteData()
            btnRead.performClick()
        }
    }
}