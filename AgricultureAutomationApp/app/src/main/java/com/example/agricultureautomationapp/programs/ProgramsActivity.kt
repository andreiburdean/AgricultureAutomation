package com.example.agricultureautomationapp.programs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R

class ProgramsActivity : AppCompatActivity() {

    private lateinit var addForm: View
    private lateinit var closeButton: ImageButton
    private lateinit var addProgramButton: Button
    private lateinit var adapter: ProgramsListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var programsManager: ProgramsManager
    private lateinit var upperBarDropDownButton: View
    private lateinit var environmentOptionsLayout: LinearLayout
    private lateinit var addButton: Button
    private lateinit var controlButton: Button
    private lateinit var selectProgram: TextView
    private lateinit var programsDropDown: LinearLayout
    private lateinit var triangleRight: ImageView
    private lateinit var triangleDown: ImageView
    private lateinit var agronomical: Button
    private lateinit var horticultural: Button
    private lateinit var pomological: Button
    private lateinit var viticultural: Button
    private lateinit var custom: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.programs_activity)

        val mainLayout = findViewById<ConstraintLayout>(R.id.programs_activity)
        upperBarDropDownButton = findViewById(R.id.options_button)
        addForm = findViewById(R.id.add_form)
        closeButton = findViewById(R.id.close_button)
        addProgramButton = findViewById(R.id.add_program_button)
        recyclerView = findViewById(R.id.programs_recycler)
        environmentOptionsLayout = findViewById(R.id.environment_options_dropdown)
        addButton = findViewById(R.id.add)
        controlButton = findViewById(R.id.control)
        selectProgram = findViewById(R.id.select_program)
        programsDropDown = findViewById(R.id.programs_dropdown)
        triangleRight = findViewById(R.id.triangle_right)
        triangleDown = findViewById(R.id.triangle_down)
        agronomical = findViewById(R.id.agronomical)
        horticultural = findViewById(R.id.horticultural)
        pomological = findViewById(R.id.pomological)
        viticultural = findViewById(R.id.viticulltural)
        custom = findViewById(R.id.custom)

        programsManager = ProgramsManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProgramsListAdapter(mutableListOf(), programsManager) { selectedProgram -> }

        recyclerView.adapter = adapter

        upperBarDropDownButton.setOnClickListener {
            if(environmentOptionsLayout.visibility == View.VISIBLE){
                environmentOptionsLayout.visibility = View.GONE
            } else{
                environmentOptionsLayout.visibility = View.VISIBLE
            }
        }

        mainLayout.setOnClickListener {
            environmentOptionsLayout.visibility = View.GONE
        }

        addButton.setOnClickListener {
            if(environmentOptionsLayout.visibility == View.VISIBLE){
                environmentOptionsLayout.visibility = View.GONE
                addForm.visibility = View.VISIBLE
            }
        }

        closeButton.setOnClickListener{
            if(addForm.visibility == View.VISIBLE){
                addForm.visibility = View.GONE
            }
        }

        controlButton.setOnClickListener {
            if(environmentOptionsLayout.visibility == View.VISIBLE){
                environmentOptionsLayout.visibility = View.GONE
            }
        }

        selectProgram.setOnClickListener{
            if(programsDropDown.visibility == View.VISIBLE){
                programsDropDown.visibility = View.INVISIBLE
                triangleDown.visibility = View.INVISIBLE
                triangleRight.visibility = View.VISIBLE
            }else{
                programsDropDown.visibility = View.VISIBLE
                triangleDown.visibility = View.VISIBLE
                triangleRight.visibility = View.INVISIBLE
            }
        }

        agronomical.setOnClickListener {
            selectProgram.setText("Agronomical")
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE
        }

        horticultural.setOnClickListener {
            selectProgram.setText("Horticultural")
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE
        }

        pomological.setOnClickListener {
            selectProgram.setText("Pomological")
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE
        }

        viticultural.setOnClickListener {
            selectProgram.setText("Viticultural")
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE
        }

        custom.setOnClickListener {
            selectProgram.setText("Custom")
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE
        }

        programsManager.fetchPrograms { programs ->
            adapter.updateList(programs)
        }
    }
}