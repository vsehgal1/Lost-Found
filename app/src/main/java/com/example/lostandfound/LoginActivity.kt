package com.example.lostandfound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// The Login Page
class LoginActivity : AppCompatActivity() {

    val storage = Firebase.storage;
    val storageRef = storage.getReference();

    lateinit var loginButton: Button
    lateinit var createButton: Button
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Testing Firebase
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Test")

        //requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0);
        val fbref = FirebaseRef.create();
        fbref.uploadImage("wowowne.jpg", "storage/emulated/0/Download/wowowne.jpg")

        setContentView(R.layout.activity_login)

        // Tae' Code Starts Here

        loginButton = findViewById(R.id.login_button)
        createButton = findViewById(R.id.new_account)
        emailText = findViewById(R.id.email)
        passwordText = findViewById(R.id.password)

        val scrollingIntent = Intent(
            this@LoginActivity,
            ScrollingActivity::class.java
        )

        // Login Button
        loginButton.setOnClickListener {
            if (emailText != null && passwordText != null &&
                    !emailText.text.isBlank() && !passwordText.text.isBlank()) { // Null Check
                val email = emailText.text.toString()
                val password = passwordText.text.toString()

                Log.i(TAG, "email: " + email)
                Log.i(TAG, "password: " + password)

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {// Login Success

                        // Check email verification status
                        FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
                            // Email Verified
                            if (firebaseUser.isEmailVerified) {

                                uid = firebaseUser.uid

                                Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                                Log.i(TAG, "Login Success")
                                // Go to next page after login
                                startActivity(scrollingIntent)
                                Log.i(TAG, "Email Verified")

                            } else {
                                // User still needs to verify email
                                Toast.makeText(
                                    this,
                                    "Check Your Email And Verify",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.i(TAG, "Email Verification Needed")
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Login Failed
                        Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG).show()
                        Log.i(TAG, "Login Failed")
                    }
            }
            else { // Input Invalid
                Toast.makeText(this, "Please Enter Correct Input", Toast.LENGTH_LONG).show()
            }
        }

        // Create Account Button
        createButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, AccountCreateActivity::class.java)
            startActivity(intent)
        }
    }

    // Tae's Code Ends Here


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        fun create(): FirebaseRef = FirebaseRef();
        const val TAG = "Lost&Found"
        const val IMAGE_PATH = "images/"
    }
}