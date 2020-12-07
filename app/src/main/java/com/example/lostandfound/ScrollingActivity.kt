package com.example.lostandfound

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DatabaseError

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
    private lateinit var refreshButton: Button

    // create local array
    private lateinit var dataArray : ArrayList<LostItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        // gets the UID of the user after login if empty
        if(UID.equals(""))
            UID = intent.getStringExtra("uid").toString();

        //new changes here
        var arr1 = ArrayList<String>()
        arr1.add("https://media-cdn.yoogiscloset.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/1/315012_01_1.jpg")


        //get button reference
        addBut = findViewById(R.id.addItemA)
        addButs = findViewById(R.id.addItemB)
        refreshButton = findViewById(R.id.refreshButton)
        //Get list view of scroll activity
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)

        //Local phone instance array
        dataArray = ArrayList<LostItem>()

        // create itemAdapter object
        val itemAdapter = ItemAdapter(this, dataArray)
        listView.adapter = itemAdapter

        addBut.setOnClickListener {
            val intent = Intent(this@ScrollingActivity, EnterLostItemActivity::class.java)
            intent.putExtra("UID", UID)
            startActivity(intent)

            itemAdapter.flag = true
            itemAdapter.notifyDataSetChanged()
        }
        addButs.setOnClickListener {
            dataArray.add(
                LostItem(
                    UID, "fd",
                    "https://www.imore.com/sites/imore.com/files/styles/xlarge/public/field/image/2016/12/iphone-7-jet-black-on-wood.jpeg?itok=GwcpsZF4",
                    "iPhone",
                    "Eppley Center",
                    "Iphone XS",
                    "11/24/2020",
                    "12/04/2020",
                    false
                )
            )
            itemAdapter.flag = true
            itemAdapter.notifyDataSetChanged()
        }

        refreshButton.setOnClickListener {
            Log.i("Click", "Refresh Clicked")
            updateLocalList()
            itemAdapter.notifyDataSetChanged()
        }

        // Retrieve firebase data:
        //populate local arraylist from with fetched submissions from global arraylist
        var listener = object: OnGetDataListener {
            override fun onSuccess(snapshot: Object) {
                var list = snapshot as ArrayList<LostItemSubmission>
                fbref.lostItemsList = list
                for(i:LostItemSubmission in fbref.lostItemsList){
                    dataArray.add(
                        LostItem(
                            i.userid, i.id, i.pictureURLs[0], i.name,
                            i.location, i.description, i.dateFound, i.dateSubmitted,
                            i.status
                        )
                    )
                }
                itemAdapter.notifyDataSetChanged()

            }

            override fun onStart() {
            }

            override fun onFailure(error: Object) {
                var err = error as DatabaseError
                Log.i(TAG, err.message)
                Toast.makeText(
                    applicationContext,
                    "NETWORK ERROR - Please check your network connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        fbref.fetchSubmissionsList(listener)

        // set listview items to onclick listener
        listView.setOnItemClickListener { parent, view, position, id ->
            val it : LostItem = itemAdapter.getItem(position) as LostItem
//            Toast.makeText(applicationContext,it.name,Toast.LENGTH_SHORT).show()
            val name = it.name.toString()
            val uid = it.uid.toString()
            val id = it.id
            val imgURL = it.imgURL
            val location = it.locationFound.toString()
            val desc = it.desc.toString()
            val dateFound = it.dateFound
            val datePosted = it.datePosted
            val selfUID = UID
            val status = it.status.toString()

            //send intent to Claimitem.kt
            val intent = Intent(this, ClaimItem::class.java)
            intent.putExtra("Name", name)
            intent.putExtra("UID", uid)
            intent.putExtra("ID", id)
            intent.putExtra("MyUID", selfUID)
            intent.putExtra("IMGUrl", imgURL)
            intent.putExtra("Location", location)
            intent.putExtra("Desc", desc)
            intent.putExtra("Found", dateFound)
            intent.putExtra("Posted", datePosted)
            intent.putExtra("Status", status)
            startActivity(intent)
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

    fun displayArray(arrList: ArrayList<LostItem>): String {
        for (i in arrList){
            Log.i("Click", i.name + i.id)
        }
        return "done"
    }
    fun dispGlobalArray(arrList: ArrayList<LostItemSubmission>): String {
        Log.i("Click", "Global List:")
        for (i in arrList){
            Log.i("Click", i.name + " " + i.id + " " + i.pictureURLs[0])
        }
        return "done"
    }

    fun updateLocalList(){
                finish();
                startActivity(getIntent());
//            Log.i("Click", "come to updateLocalList")
//            Log.i("Click", dispGlobalArray(fbref.lostItemsList))
//        var listener = object: OnGetDataListener {
//            override fun onSuccess(snapshot: Object) {
//                var list = snapshot as ArrayList<LostItemSubmission>
//                fbref.lostItemsList = list
//
//                Log.i("Click", "Getting subs")
//                Log.i("Click", list.toString())
//                Log.i("Click", "Current length: "+ list.size)
//            }
//
//            override fun onStart() {
//            }
//
//            override fun onFailure(error: Object) {
//                var err = error as DatabaseError
//                Log.i(TAG, err.message)
//                Toast.makeText(applicationContext,
//                    "NETWORK ERROR - Please check your network connection",
//                    Toast.LENGTH_SHORT).show()
//            }
//
//        }
//        fbref.fetchSubmissionsList(listener)
//        dataArray.clear()
//        for(i:LostItemSubmission in fbref.lostItemsList){
//            dataArray.add(LostItem(i.userid,i.id,i.pictureURLs[0],i.name, i.location, i.description,
//                i.dateFound, i.dateSubmitted, i.status))
//        }
////            Log.i("Click", "Display local list")
////            Log.i("Click", dataArray.toString())
//        Log.i("store", "at scroll: "+ dataArray[0].status)

    }

    companion object {
        fun create(): FirebaseRef = FirebaseRef();
        const val TAG = "Lost&Found";
    }
}