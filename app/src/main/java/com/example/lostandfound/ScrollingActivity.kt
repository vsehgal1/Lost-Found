package com.example.lostandfound

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ScrollingActivity : AppCompatActivity() {

    // user id of the current user
    var UID: String = "";

    // Firebase Ref reference
    val fbref = FirebaseRef.create()

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView

    //button for test purposes
    private lateinit var addBut: Button
    private lateinit var addButs: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        // gets the UID of the user after login if empty
        if(UID.equals(""))
            UID = intent.getStringExtra("uid").toString();

        var arr1 = ArrayList<String>()
        arr1.add("pictureURL1")

        fbref.newSubmission(
            UID,
            "Lost Bag",
            "Brown Bag with Flaps",
            "Stamp Student Union",
            arr1,
            LocalDateTime.now(),
            "tag1 tag2"
        )

        var listener = object: OnGetDataListener {
            override fun onSuccess(snapshot: Object) {
                var list = snapshot as ArrayList<LostItemSubmission>
                Log.i(TAG, list.toString())
            }

            override fun onStart() {
            }

            override fun onFailure(error: Object) {
                var err = error as DatabaseError
                Log.i(TAG, err.message)
            }

        }
        fbref.fetchSubmissionsList(listener)



        // VIKRAM CODE STARTS HERE
        //get button reference
        addBut = findViewById(R.id.addItemA)
        addButs = findViewById(R.id.addItemB)
        //Get list view of scroll activity
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)
        //create testing data
        var dataArray = ArrayList<LostItem>()

        // create itemAdapter object
        val itemAdapter = ItemAdapter(this, dataArray)
        listView.adapter = itemAdapter

        addBut.setOnClickListener {
            dataArray.add(
                LostItem(
                    R.drawable.key_fig,
                    "Keys",
                    "Tawes Hall",
                    "Keys of type Keys"
                )
            )
            itemAdapter.flag = true
            itemAdapter.notifyDataSetChanged()
        }
        addButs.setOnClickListener {
            dataArray.add(
                LostItem(
                    R.drawable.iphone,
                    "iPhone",
                    "Eppley Center",
                    "Iphone XS"
                )
            )
            itemAdapter.flag = true
            itemAdapter.notifyDataSetChanged()
        }

            // create search view object
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.i("Click", "Reached searchView listener")
                    if (TextUtils.isEmpty(newText)) {
                        itemAdapter.filter("")
                        listView.clearTextFilter()
                    } else newText?.let { itemAdapter.filter(it) }
                    return false
                }

            })
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
            const val TAG = "Lost&Found";
            const val IMAGE_PATH = "images/";
        }
    }
