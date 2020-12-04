package com.example.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class ClaimItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_item)
        //get image and text view id
        val imgView = findViewById<ImageView>(R.id.imageHold)
        val nameView = findViewById<TextView>(R.id.titleHold)
        val locView = findViewById<TextView>(R.id.locationHold)
        val descView = findViewById<TextView>(R.id.descHold)
        val dateView = findViewById<TextView>(R.id.dtHold)
        val datepostedView= findViewById<TextView>(R.id.dtpHold)
        // retrieve intent data
        val intent = intent
        val name = intent.getStringExtra("Name")
        val location = intent.getStringExtra("Location")
        val desc = intent.getStringExtra("Desc")
        val image = intent.getStringExtra("Image")?.toInt()
        val found = intent.getStringExtra("Found")
        val posted = intent.getStringExtra("Posted")

        //set values
        if (image != null) {
            imgView.setImageResource(image)
        }
        nameView.text = name
        locView.text = location
        descView.text = desc
        dateView.text = found
        datepostedView.text = posted

    }
}