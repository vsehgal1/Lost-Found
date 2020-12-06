package com.example.lostandfound

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

// This is for sending email once item has been claimed.
// It will let the user choose the email app and the data will be automatically filled in.
// Copy this function for use.
// By: Tae Jung

class ItemClaimConfirm: AppCompatActivity() {

    // Send Claim Confirmation Email
    // email -> Recipient
    fun sendClaimConfirmEmail (email: String) {
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
