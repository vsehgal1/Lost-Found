package com.example.lostandfound

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.util.*

class EnterLostItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    lateinit var textView: TextView
    lateinit var button: Button
    lateinit var buttonAddPictures: Button
    lateinit var buttonSubmission: Button

    lateinit var name: EditText
    lateinit var description: EditText
    lateinit var location: EditText

    lateinit var image : ImageView


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

    var filePathsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_lost_item)

        name = findViewById(R.id.NameText);
        description = findViewById(R.id.DescriptionText)
        location = findViewById(R.id.LocationText)
        image = findViewById(R.id.imageAdded)

        textView = findViewById(R.id.textView)

        button = findViewById(R.id.btnPick)
        button.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@EnterLostItemActivity, this@EnterLostItemActivity, year, month,day)
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
                //send back filepaths of images
                //send back name, location, description, and date/time that was chosen
                //
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
        val timePickerDialog = TimePickerDialog(this@EnterLostItemActivity, this@EnterLostItemActivity, hour, minute,
            is24HourFormat(this)
        )
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        textView.text = "Year: " + myYear + "\n" + "Month: " + myMonth + "\n" + "Day: " + myDay + "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
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

            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    val uriPathHelper = URIPathHelper()
                    val filePath =imageUri?.let { uriPathHelper.getPath(this, it) }
                    if (filePath != null) {
                        filePathsList.add(filePath)
                    }
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                    image.setImageURI(imageUri)
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri? = data.data
                val uriPathHelper = URIPathHelper()
                val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
                if (filePath != null) {
                    filePathsList.add(filePath)
                }
                image.setImageURI(imageUri)

            }
        }
    }
}
