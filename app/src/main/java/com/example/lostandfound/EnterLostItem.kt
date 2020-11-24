package com.example.lostandfound

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_enter_lost_item.view.*

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.text.Editable
import android.widget.DatePicker

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import android.widget.TimePicker
import java.util.*



/**
 * A simple [Fragment] subclass.
 * Use the [EnterLostItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterLostItem : Fragment(), OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private lateinit var addTime : EditText
    private lateinit var addDate : EditText
    private lateinit var addDesc : EditText
    private lateinit var addName : EditText
    private lateinit var addLoc : EditText

    companion object {

        private const val KEY_CURRENT_TIME = "CURRENT_TIME_KEY"
        private const val KEY_CURRENT_DATE = "CURRENT_DATE_KEY"

        @Suppress("unused")
        private const val TAG = "DateTimePickerFragments"

            private fun pad(c: Int): String {
            return if (c >= 10)
                c.toString()
            else
                "0$c"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_enter_lost_item, container, false)
        addDesc = view.findViewById(R.id.description);
        addName = view.findViewById(R.id.itemName);
        addLoc = view.findViewById(R.id.locationFound);
        addDate = view.findViewById(R.id.editTextDate);
        addTime = view.findViewById(R.id.editTextTime);

        setupUI(savedInstanceState)
        return view
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        if (null == savedInstanceState) {
            addTime.setText(R.string.no_time_selected_string)
            addDate.setText(R.string.no_date_selected_string)
        } else {
            addTime.setText(savedInstanceState.getCharSequence(KEY_CURRENT_TIME))
            addDate.setText(savedInstanceState.getCharSequence(KEY_CURRENT_DATE))

        }
    }



    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        addTime.setText(StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)))
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        addDate.setText(StringBuilder()
            .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-")
            .append(year).append(" ")
        )
    }

    public override fun onSaveInstanceState(bundle: Bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putCharSequence(KEY_CURRENT_DATE, addDate.text)
        bundle.putCharSequence(KEY_CURRENT_TIME, addTime.text)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }


    class TimePickerFragment : DialogFragment(), OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


            val c = Calendar.getInstance()
            val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hourOfDay, minute,false)
        }

        // Callback called by TimePickerDialog when user sets the time
        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            (activity as OnTimeSetListener).onTimeSet(view, hourOfDay, minute)
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            // Set the current date in the DatePickerFragment
            val cal = Calendar.getInstance()

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(
                activity as Context, this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        // Callback called by DatePickerDialog when user sets the time
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

            (activity as DatePickerDialog.OnDateSetListener).onDateSet(view, year, monthOfYear, dayOfMonth)
        }
    }




}