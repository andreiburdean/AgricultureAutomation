package com.example.agricultureautomationapp.programs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.ProgramItem

class ProgramsListAdapter(private var programs: MutableList<ProgramItem>, private val programsManager: ProgramsManager) : RecyclerView.Adapter<ProgramsListAdapter.ProgramViewHolder>() {

    inner class ProgramViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val programName: TextView = view.findViewById(R.id.program_name)
        val statusActive: ImageView = view.findViewById(R.id.status_active)
        val statusInactive: ImageView = view.findViewById(R.id.status_inactive)
        val optionsButton: ImageButton = view.findViewById(R.id.options_button)
        val optionsSpinner: Spinner = view.findViewById(R.id.program_options_spinner)

        fun bind(program: ProgramItem) {
            programName.text = program.programName

            val options = listOf("Edit", "Delete", "Control")
            val spinnerAdapter = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_dropdown_item,
                options
            )
            optionsSpinner.adapter = spinnerAdapter

            optionsSpinner.visibility = View.GONE

            optionsButton.setOnClickListener {
                optionsSpinner.visibility =
                    if (optionsSpinner.visibility == View.GONE) View.VISIBLE else View.GONE
                optionsSpinner.performClick()
            }

            optionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedOption = options[position]
                    when (selectedOption) {
                        "Edit" -> Toast.makeText(
                            itemView.context,
                            "Edit ${program.programName}",
                            Toast.LENGTH_SHORT
                        ).show()

                        "Delete" -> Toast.makeText(
                            itemView.context,
                            "Delete ${programName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    optionsSpinner.visibility = View.GONE
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    optionsSpinner.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_item, parent, false)
        return ProgramViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val program = programs[position]
        holder.programName.text = program.programName

        if (program.status == 1) {
            holder.statusActive.visibility = View.VISIBLE
            holder.statusInactive.visibility = View.GONE
        } else {
            holder.statusActive.visibility = View.GONE
            holder.statusInactive.visibility = View.VISIBLE
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

