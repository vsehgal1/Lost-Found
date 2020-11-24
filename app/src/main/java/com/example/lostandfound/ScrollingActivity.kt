package com.example.lostandfound

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ScrollingActivity : AppCompatActivity() {

    val storage = Firebase.storage;
    val storageRef = storage.getReference();
    private lateinit var listView : ListView
    //button for test purposes
    private lateinit var addBut : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("This", "just started onCreate")

        //requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0);
        val fbref = FirebaseRef.create();
        //fbref.uploadImage("wowowne.jpg", "storage/emulated/0/Download/wowowne.jpg")
        val pictureList = ArrayList<String>();
        pictureList.add("url1");
        pictureList.add("url2");
        val date = LocalDateTime.now();
        fbref.newSubmission("123456","Lost Bag", "Brown and empty", "Stamp Student Union", pictureList, date, pictureList);


        setContentView(R.layout.activity_scrolling)
//        setSupportActionBar(findViewById(R.id.toolbar))

//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        // VIKRAM CODE STARTS HERE
        //get button reference
        addBut = findViewById(R.id.addItem)
        //Get list view of scroll activity
        listView = findViewById(R.id.listView)

        //create testing data
        var dataArray = ArrayList<LostItem>()
        dataArray.add(
            LostItem(
            R.drawable.key_fig,
            "Keys",
            "Tawes Hall",
            "Keys of type Keys")
        )

        // create itemAdapter object
        val itemAdapter = ItemAdapter(this, dataArray)
        listView.adapter = itemAdapter

        addBut.setOnClickListener {
            dataArray.add(
                LostItem(
                    R.drawable.key_fig,
                    "Keys",
                    "Tawes Hall",
                    "Keys of type Keys")
            )

            itemAdapter.notifyDataSetChanged()
        }
        // VIKRAM CODE ENGS HERE
    }


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