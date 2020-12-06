package com.example.lostandfound

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class ItemAdapter(private val context: Context,
                  private val dataSource: ArrayList<LostItem>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var flag = true
    var tempList : ArrayList<LostItem> = ArrayList()

//    init {
//        this.tempList.addAll(dataSource)
//    }

    override fun getCount(): Int {
//        TODO("Not yet implemented")s
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
        val rowView = inflater.inflate(R.layout.list_row, parent, false)
        val imageView = rowView.findViewById(R.id.image) as ImageView
        val titleView = rowView.findViewById(R.id.txtName) as TextView
        val locationView = rowView.findViewById(R.id.location) as TextView
        val descView = rowView.findViewById(R.id.des) as TextView
        val getRef = getItem(position) as LostItem
//        imageView.setImageResource(getRef.img)

        try{
            Glide.with(context).load(getRef.imgURL).into(imageView)
        } catch (e: Exception){
            e.printStackTrace()
        }
        titleView.text = getRef.name
        locationView.text = getRef.locationFound
        descView.text = getRef.desc
        return rowView
    }

    //filter function
    fun filter(charText : String){
        //create deep copy of
        if (flag){
            tempList.clear()
            for(i in dataSource){
            tempList.add(LostItem(i.uid,i.id,i.imgURL,i.name,i.locationFound,i.desc, i.dateFound, i.datePosted))
            flag = false
        }}
        val charText = charText.toLowerCase(Locale.getDefault())
        dataSource.clear()
        Log.i("Click", "temp_list at call:" + tempList.size.toString())
        Log.i("Click", "datasource at call:" + dataSource.size.toString())
        if(charText.length == 0){
            Log.i("Click", "looking for length == 0")
            dataSource.clear()
            dataSource.addAll(tempList)
        }
        else{
            Log.i("Click", "looking for length != 0")
            for(i in tempList){
                if(i.locationFound.contains(charText) or i.name.toLowerCase().contains(charText) or i.desc.contains(charText))
                    dataSource.add(i)
            }
            Log.i("Click", "temp_list after else:" + tempList.size.toString())
        }
        notifyDataSetChanged()
    }

}

