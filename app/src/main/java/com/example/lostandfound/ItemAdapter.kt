// VIKRAM CODE STARTS HERE

package com.example.lostandfound

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ItemAdapter(private val context: Context,
                  private val dataSource: ArrayList<LostItem>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
//        TODO("Not yet implemented")
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
//        TODO("Not yet implemented")
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
//        TODO("Not yet implemented")
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        TODO("Not yet implemented")
//        var layoutInflator : LayoutInflater = LayoutInflater.from(context)
//        convertView = layoutInflator.inflate()
        val rowView = inflater.inflate(R.layout.list_row, parent, false)
        val imageView = rowView.findViewById(R.id.image) as ImageView
        val titleView = rowView.findViewById(R.id.txtName) as TextView
        val locationView = rowView.findViewById(R.id.location) as TextView
        val descView = rowView.findViewById(R.id.des) as TextView

        val getRef = getItem(position) as LostItem
        imageView.setImageResource(getRef.img)
        titleView.text = getRef.name
        locationView.text = getRef.locationFound
        descView.text = getRef.desc
        return rowView
    }

}

// VIKRAM CODE ENDS HERE