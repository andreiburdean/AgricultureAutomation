package com.example.agricultureautomationapp.programs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.ProgramItem

class ProgramsListAdapter(private var programs: MutableList<ProgramItem>, private val programsManager: ProgramsManager, private val onItemClick: (ProgramItem) -> Unit) : RecyclerView.Adapter<ProgramsListAdapter.ProgramViewHolder>() {

    inner class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val programName: TextView = itemView.findViewById(R.id.program_name)
        val statusActive: ImageView = itemView.findViewById(R.id.status_active)
        val statusInactive: ImageView = itemView.findViewById(R.id.status_inactive)
        val optionsButton: ImageButton = itemView.findViewById(R.id.options_button)
        val optionsDropdown: LinearLayout = itemView.findViewById(R.id.program_options_dropdown)
        val start: ImageButton = itemView.findViewById(R.id.start)
        val stop: ImageButton = itemView.findViewById(R.id.stop)
        val edit: ImageButton = itemView.findViewById(R.id.edit)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_item, parent, false)
        return ProgramViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val program = programs[position]
        holder.programName.text = program.programName
        holder.optionsDropdown.visibility = View.GONE
        var programStatus = program.status
        var programName = program.programName

        if (program.status == 1) {
            holder.statusActive.visibility = View.VISIBLE
            holder.statusInactive.visibility = View.GONE
        } else {
            holder.statusActive.visibility = View.GONE
            holder.statusInactive.visibility = View.VISIBLE
        }

        holder.optionsButton.setOnClickListener {
            if (holder.optionsDropdown.visibility == View.VISIBLE) {
                holder.optionsDropdown.visibility = View.INVISIBLE
            } else {
                holder.optionsDropdown.visibility = View.VISIBLE

                if(programStatus == 1){
                    holder.start.visibility = View.GONE
                    holder.stop.visibility = View.VISIBLE
                }else{
                    holder.start.visibility = View.VISIBLE
                    holder.stop.visibility = View.GONE
                }

                if(programName == "Custom"){
                    holder.edit.visibility = View.VISIBLE
                }else{
                    holder.edit.visibility = View.GONE
                }
            }
        }

        holder.start.setOnClickListener {
            holder.optionsDropdown.visibility = View.GONE
            holder.optionsDropdown.requestLayout()
        }

        holder.stop.setOnClickListener {
            holder.optionsDropdown.visibility = View.GONE
            Log.d("ceva", "1")
            holder.optionsDropdown.requestLayout()
        }

        holder.edit.setOnClickListener {
            holder.optionsDropdown.visibility = View.GONE
            holder.optionsDropdown.requestLayout()
        }

        holder.delete.setOnClickListener {
            holder.optionsDropdown.visibility = View.GONE
            holder.optionsDropdown.requestLayout()
        }

        holder.itemView.setOnClickListener {
            onItemClick(program)
        }
    }

    override fun getItemCount(): Int = programs.size

    fun updateList(newPrograms: List<ProgramItem>) {
        Log.d("ProgramsListAdapter", "New programs size: ${newPrograms.size}")
        programs.clear()
        programs.addAll(newPrograms)
        notifyDataSetChanged()
    }
//
//    fun removeItem(environment: ProgramItem) {
//        programs.remove(environment)
//        notifyDataSetChanged()
//    }
}

