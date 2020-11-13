package com.example.lostandfound

import android.Manifest
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.*
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream


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

    companion object {
        fun create(): FirebaseRef = FirebaseRef();
        const val TAG = "Lost&Found-FirebaseRef"
        const val IMAGE_PATH = "images/"
    }
}