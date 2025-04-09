package com.example.agricultureautomationapp.programs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.ProgramItem
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences

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
    private lateinit var programNameField: EditText
    private lateinit var temperatureText: TextView
    private lateinit var temperatureField: EditText
    private lateinit var humidityText: TextView
    private lateinit var humidityField: EditText
    private lateinit var luminosityText: TextView
    private lateinit var luminosityField: EditText
    private lateinit var environmentName: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        var programTypeId: Int = 0

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
        programNameField = findViewById(R.id.program_name_field)
        temperatureText = findViewById(R.id.temperature_text)
        temperatureField = findViewById(R.id.temperature_field)
        humidityText = findViewById(R.id.humidity_text)
        humidityField = findViewById(R.id.humidity_field)
        luminosityText = findViewById(R.id.luminosity_text)
        luminosityField = findViewById(R.id.luminosity_field)
        environmentName = findViewById(R.id.environment_text)

        environmentName.text = SharedPreferences.getEnvironmentName(this)

        programsManager = ProgramsManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProgramsListAdapter(mutableListOf(), programsManager) { selectedProgram -> environmentOptionsLayout.visibility = View.GONE}

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
                temperatureText.visibility = View.INVISIBLE
                temperatureField.visibility = View.INVISIBLE
                humidityText.visibility = View.INVISIBLE
                humidityField.visibility = View.INVISIBLE
                luminosityText.visibility = View.INVISIBLE
                luminosityField.visibility = View.INVISIBLE

                temperatureField.setText("")
                humidityField.setText("")
                luminosityField.setText("")
                selectProgram.setText("Agronomical")

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
            programTypeId = 1
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE

            temperatureText.visibility = View.INVISIBLE
            temperatureField.visibility = View.INVISIBLE
            humidityText.visibility = View.INVISIBLE
            humidityField.visibility = View.INVISIBLE
            luminosityText.visibility = View.INVISIBLE
            luminosityField.visibility = View.INVISIBLE
        }

        horticultural.setOnClickListener {
            selectProgram.setText("Horticultural")
            programTypeId = 2
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE

            temperatureText.visibility = View.INVISIBLE
            temperatureField.visibility = View.INVISIBLE
            humidityText.visibility = View.INVISIBLE
            humidityField.visibility = View.INVISIBLE
            luminosityText.visibility = View.INVISIBLE
            luminosityField.visibility = View.INVISIBLE
        }

        pomological.setOnClickListener {
            selectProgram.setText("Pomological")
            programTypeId = 3
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE

            temperatureText.visibility = View.INVISIBLE
            temperatureField.visibility = View.INVISIBLE
            humidityText.visibility = View.INVISIBLE
            humidityField.visibility = View.INVISIBLE
            luminosityText.visibility = View.INVISIBLE
            luminosityField.visibility = View.INVISIBLE
        }

        viticultural.setOnClickListener {
            selectProgram.setText("Viticultural")
            programTypeId = 4
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE

            temperatureText.visibility = View.INVISIBLE
            temperatureField.visibility = View.INVISIBLE
            humidityText.visibility = View.INVISIBLE
            humidityField.visibility = View.INVISIBLE
            luminosityText.visibility = View.INVISIBLE
            luminosityField.visibility = View.INVISIBLE
        }

        custom.setOnClickListener {
            selectProgram.setText("Custom")
            programTypeId = 5
            programsDropDown.visibility = View.INVISIBLE
            triangleDown.visibility = View.INVISIBLE
            triangleRight.visibility = View.VISIBLE

            temperatureText.visibility = View.VISIBLE
            temperatureField.visibility = View.VISIBLE
            humidityText.visibility = View.VISIBLE
            humidityField.visibility = View.VISIBLE
            luminosityText.visibility = View.VISIBLE
            luminosityField.visibility = View.VISIBLE
        }

        addProgramButton.setOnClickListener {

            val programName = programNameField.text.toString().trim()
            if (programName.isNotEmpty()) {
                if(programTypeId != 0){
                    if(programTypeId != 5){
                        val newProgram = ProgramItem(programTypeId, programName)
                        programsManager.addProgram(newProgram) { updatedList ->
                            adapter.updateList(updatedList)
                            Toast.makeText(this, "Program added!", Toast.LENGTH_SHORT).show()
                            addForm.visibility = View.GONE
                            selectProgram.setText("Select Program")
                            programNameField.setText("")
                        }
                    }
                    else{
                        val temperature = (temperatureField.text.toString().trim()).toDouble()
                        val humidity = (humidityField.text.toString().trim()).toDouble()
                        val luminosity = (luminosityField.text.toString().trim()).toDouble()
                        val newProgram = ProgramItem(programTypeId, programName, temperature, humidity, luminosity)
                        programsManager.addProgram(newProgram) { updatedList ->
                            adapter.updateList(updatedList)
                            Toast.makeText(this, "Program added!", Toast.LENGTH_SHORT).show()
                            addForm.visibility = View.GONE
                            selectProgram.setText("Select Program")
                            programNameField.setText("")

                            temperatureText.visibility = View.INVISIBLE
                            temperatureField.visibility = View.INVISIBLE
                            humidityText.visibility = View.INVISIBLE
                            humidityField.visibility = View.INVISIBLE
                            luminosityText.visibility = View.INVISIBLE
                            luminosityField.visibility = View.INVISIBLE

                            temperatureField.setText("")
                            humidityField.setText("")
                            luminosityField.setText("")
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Please choose a program!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill out the fields!", Toast.LENGTH_SHORT).show()
            }
        }

        programsManager.fetchPrograms { programs ->
            adapter.updateList(programs)
        }
    }
}