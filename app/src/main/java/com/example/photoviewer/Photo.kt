package com.example.photoviewer

import android.graphics.Bitmap
import java.io.File

class Photo {
    var id: Int = 0
    lateinit var image: Bitmap
    var desc: String = ""

    constructor(photo: Bitmap, desc: String) {
        this.image = photo
        this.desc = desc
    }

    constructor() {
    }
}