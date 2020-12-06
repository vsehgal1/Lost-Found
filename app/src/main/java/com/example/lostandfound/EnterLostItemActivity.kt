package com.example.lostandfound

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat.is24HourFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.util.*
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor

import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.database.DatabaseError


class EnterLostItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    lateinit var textView: TextView
    lateinit var button: Button
    lateinit var buttonAddPictures: Button
    lateinit var buttonSubmission: Button

    lateinit var name: EditText
    lateinit var description: EditText
    lateinit var location: EditText
    lateinit var tags: EditText

    lateinit var image : ImageView

    lateinit var uid: String
    lateinit var date: LocalDateTime




    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    val REQUEST_CODE = 200

    val filePathsList = ArrayList<String>()
    val filePathsList2 = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_lost_item)

        uid = intent.getStringExtra("UID").toString()

        name = findViewById(R.id.NameText);
        description = findViewById(R.id.DescriptionText)
        location = findViewById(R.id.LocationText)
        image = findViewById(R.id.imageAdded)
        tags = findViewById(R.id.tagsText)

        textView = findViewById(R.id.textView)

        button = findViewById(R.id.btnPick)
        button.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(
                    this@EnterLostItemActivity,
                    this@EnterLostItemActivity,
                    year,
                    month,
                    day
                )
            datePickerDialog.show()
        }

        buttonAddPictures = findViewById(R.id.btnAddPictures)
        buttonAddPictures.setOnClickListener{
            openGalleryForImages()
        }
        buttonSubmission = findViewById(R.id.btnSubmission)

        buttonSubmission.setOnClickListener{
            if (name.text.toString() != "Name" && description.text.toString() != "Description" && location.text.toString() != "Location"
                && textView.text != "" ){

                //val intent = Intent().apply {
                   // putExtra("message", "This is a message from Activity3")
                    // Put your data here if you want.
                //}
                //setResult(Activity.RESULT_OK, intent)



                val tempRef = FirebaseRef.create()
                val listener = object: OnGetDataListener {
                    override fun onSuccess(snapshot: Object) {
                        val filePathTemp = snapshot as String

                        filePathsList2.add(filePathTemp)
                        //Log.i("Click", "Getting subs")
                        //Log.i("Click", list.toString())
                       // Log.i("Click", "Current length: "+ list.size)

                    }

                    override fun onStart() {
                    }

                    override fun onFailure(error: Object) {
                        var err = error as DatabaseError
                        Log.i(        "Lost&Found-FirebaseRef",  err.message)
                        Toast.makeText(applicationContext,
                            "image failed to upload",
                            Toast.LENGTH_SHORT).show()
                    }

                }

               tempRef.uploadImage(name.text.toString(), filePathsList[0], uid, listener)

                tempRef.newSubmission(
                    uid,
                    name.text.toString(),
                    description.text.toString(),
                    location.text.toString(),
                    filePathsList2,
                    date,
                    tags.text.toString()
                )


                Toast.makeText(this, "Your submission is processing now", Toast.LENGTH_SHORT)

                super.onBackPressed()
            }

        }

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this@EnterLostItemActivity, this@EnterLostItemActivity, hour, minute,
            is24HourFormat(this)
        )
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        date = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute)
        textView.text = "" + myYear + "-"  + myMonth + "-" + myDay + " " + myHour + ":" + myMinute
    }

    private fun openGalleryForImages() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            if (data != null) {
                if (data.data != null) {
                    // if single image is selected

                    val imageURI = data.data
                   // val uriPathHelper = URIPathHelper()
                    val filePath = imageURI?.let { getRealPathFromURIAPI19(this, it) }
                   // val filePath = uriPathHelper.getPath(this, imageURI)

                    //if (filePath != null) {
                     //   filePathsList.add(filePath)
                    //}
                    if (filePath != null) {
                        filePathsList.add(filePath)
                    }
                    image.setImageURI(imageURI)

                }
            }
        }
    }

    fun getRealPathFromURIAPI19(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                    cursor!!.moveToNext()
                    val fileName = cursor.getString(0)
                    val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    if (!TextUtils.isEmpty(path)) {
                        return path
                    }
                } finally {
                    cursor?.close()
                }
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author Niks
     */
    private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                              selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}



