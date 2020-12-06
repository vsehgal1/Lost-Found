package com.example.lostandfound

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError

//import com.bumptech.glide.Glide

class ClaimItem : AppCompatActivity() {
    val fbref = FirebaseRef.create()
    lateinit var email : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_item)

        // create back option for toolbar
        val back = supportActionBar
        back!!.title = "Lost & Found"
        back.setDisplayHomeAsUpEnabled(true)


        //get reference to "CLAIM" & "DELETE" button
        val claimButton = findViewById<Button>(R.id.claim)
        val deleteButton = findViewById<Button>(R.id.delete)

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
        val uid = intent.getStringExtra("UID")
        val location = intent.getStringExtra("Location")
        val desc = intent.getStringExtra("Desc")
        val imgURL = intent.getStringExtra("IMGUrl")
        val found = intent.getStringExtra("Found")
        val posted = intent.getStringExtra("Posted")
        val myUID = intent.getStringExtra("MyUID")

        //change button visibilities based on  who the user is
        if(myUID == uid){
            claimButton.visibility = View.INVISIBLE
            deleteButton.visibility = View.VISIBLE
        }
        else{
            // do nothing
            // if ids don't match then delete will be invisible but claim will be visible
        }
        //set values
        try{
            Glide.with(this).load(imgURL).into(imgView)
        } catch (e: Exception){
            e.printStackTrace()
        }

        nameView.text = name
        locView.text = location
        descView.text = desc
        dateView.text = found
        datepostedView.text = posted

        //get lost item poster email
        Log.i("Claim","here")
        var listener = object: OnGetDataListener {
            override fun onSuccess(snapshot: Object) {
                var user = snapshot as User
                email = user.email
                Log.i("Claim", "Getting EMail")
                Log.i("Claim", email)

            }

            override fun onStart() {
            }

            override fun onFailure(error: Object) {
                var err = error as DatabaseError
                Log.i(ScrollingActivity.TAG, err.message)
                Toast.makeText(applicationContext,
                    "NETWORK ERROR - Please check your network connection",
                    Toast.LENGTH_SHORT).show()
            }

        }
        if (uid != null) {
            fbref.findUserByID(uid, listener)
        }
        claimButton.setOnClickListener {
            Log.i("Claim", "Pressed claim")
            Log.i("Claim", "reached itemclaimconfirm")
            val intent = Intent(Intent.ACTION_SEND)
            val mTitle = "Lost & Found: Item Claim Confirmation"
            val mMsg = "This email is to notify you that the item you have listed on Lost & Found has been claimed \n\n\n " +
                    "This is an automated message. Please DO NOT reply to this email."
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, mTitle)
            intent.putExtra(Intent.EXTRA_TEXT, mMsg)
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Select Email"))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}