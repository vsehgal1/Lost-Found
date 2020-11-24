package com.example.lostandfound

import android.Manifest
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class FirebaseRef: AppCompatActivity() {

    val storage = Firebase.storage;
    val storageRef = storage.getReference();

    /**
     * Function: uploadImage
     * Desc: Takes in an image file and the path of that file and uploads it to Firebase storage
     */
    public fun uploadImage(fName: String, fpath: String) {
        // setReferences to database
        val fileRef = storageRef.child(fName);
        val filePathRef = storageRef.child(IMAGE_PATH + fName);

        if(fileRef.name != filePathRef.name) {
            Log.i(TAG, "ERROR: File path and file name mismatch");
            return;
        }

        // upload image via stream
        val imgFile = File(fpath) as File;
        if(!imgFile.exists()) {
            Log.i(TAG, "ERROR: File Does not Exist");
            return;
        }

        val stream = FileInputStream(imgFile);
        val uploadTask = fileRef.putStream(stream);
        uploadTask.addOnFailureListener() {
            Log.i(TAG, "ERROR: Failed to Upload");
        }.addOnSuccessListener {  taskSnapshot ->
            Log.i(TAG, "Image Upload Success!");
        }
    }

    /**
     * Function: newSubmission
     * Adds a new Lost Item submission onto Firebase database
     */
    public fun newSubmission(userid: String, name: String, description: String, location: String, pictureURLs: ArrayList<String>, dateFound: LocalDateTime, tags: ArrayList<String>) {
        val database = Firebase.database
        val myRef = database.getReference(SUBMISSIONS_PATH);
        val id = database.getReference(SUBMISSIONS_PATH).push().key;

        val pattern = "yyyy-MM-dd HH:mm:ss"
        val current_time = LocalDateTime.now();
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


        val toAdd = LostItemSubmission(id!!, userid, name, location, description, pictureURLs, dateFound.format(formatter), current_time.format(formatter), tags);
        myRef.child(id).setValue(toAdd);

    }

    companion object {
        fun create(): FirebaseRef = FirebaseRef();
        const val TAG = "Lost&Found-FirebaseRef"
        const val IMAGE_PATH = "images/"
        const val SUBMISSIONS_PATH = "submissions/"
    }
}

data class LostItemSubmission(val id: String = "", val userid: String="", val name: String ="", val location: String ="", val description: String ="", val pictureURLs: ArrayList<String>, val dateFound: String, val dateSubmitted: String, val tags: ArrayList<String>);