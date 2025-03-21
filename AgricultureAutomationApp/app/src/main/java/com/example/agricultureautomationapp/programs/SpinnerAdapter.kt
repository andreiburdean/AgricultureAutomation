package com.gfg.custom_spinner_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.programs.SpinnerItem

class SpinnerAdapter(private val context: Context, private val items: List<SpinnerItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.spinner_item, parent, false)

        val item = getItem(position) as SpinnerItem
        val text = view.findViewById<TextView>(R.id.spinner_text)
        text.text = item.itemName
        return view
    }
}
