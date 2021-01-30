package com.example.photoviewer

import android.graphics.Bitmap
import java.io.File

class Photo {
    var id: Int = 0
    var name: Long = 0
    lateinit var image: Bitmap
    var desc: String = ""

    constructor(name: Long, photo: Bitmap, desc: String) {
        this.name = name
        this.image = photo
        this.desc = desc
    }

    constructor() {
    }
}