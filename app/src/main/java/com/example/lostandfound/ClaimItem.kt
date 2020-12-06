package com.example.lostandfound

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

//import com.bumptech.glide.Glide

class ClaimItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_item)
        //get reference to "CLAIM" button
        val claimButton = findViewById<Button>(R.id.claim)
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
        val imgURL = intent.getStringExtra("IMGUrl")
        val found = intent.getStringExtra("Found")
        val posted = intent.getStringExtra("Posted")

        //set values
        try{

            Glide.with(this).load(imgURL).into(imgView)
        } catch (e: Exception){
            e.printStackTrace()
        }
//        if (image != null) {
//            imgView.setImageResource(image)
//        }
        nameView.text = name
        locView.text = location
        descView.text = desc
        dateView.text = found
        datepostedView.text = posted

        claimButton.setOnClickListener {
            Log.i("Claim", "Pressed claim")
            Log.i("Claim", "reached itemclaimconfirm")
            val intent = Intent(Intent.ACTION_SEND)
            val mTitle = "Lost & Found: Item Claim Confirmation"
            val mMsg = "This email is to notify you that the item you have listed on Lost & Found has been claimed \n\n\n " +
                    "This is an automated message. Please DO NOT reply to this email."
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("vik@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, mTitle)
            intent.putExtra(Intent.EXTRA_TEXT, mMsg)
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Select Email"))
        }

    }
}