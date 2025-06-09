package com.example.agricultureautomationapp.programs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.example.agricultureautomationapp.apiservices.EnvironmentApiService
import com.example.agricultureautomationapp.customviews.SlidingToggleButton
import com.example.agricultureautomationapp.models.ControlStatus
import com.example.agricultureautomationapp.models.ProgramItem
import com.example.agricultureautomationapp.monitor.MonitorActivity
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProgramsActivity : AppCompatActivity() {

    private val BASE_URL = "http://10.0.2.2:8080"
//    private val BASE_URL = "http://192.168.40.113:8080";

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
    private lateinit var monitorButton: Button
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
    private lateinit var controlForm: View
    private lateinit var controlCloseButton: ImageButton
    private lateinit var pump: View
    private lateinit var fan: View
    private lateinit var led: View

    private var controlValue: Int = 0
    private var fanValue:     Int = 0
    private var pumpValue:    Int = 0
    private var ledValue:     Int = 0

    private var environmentId: Int = 0

    private fun postCurrentControlStatus() {
        val status = ControlStatus(
            switchControl = controlValue,
            fan           = fanValue,
            pump          = pumpValue,
            led           = ledValue
        )
        programsManager.controlEnvironment(environmentId, status)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        var programTypeId = 0
        environmentId = SharedPreferences.getEnvironmentId(this)

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
        controlButton = findViewById(R.id.control_option)
        monitorButton = findViewById(R.id.monitor)
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
        controlForm = findViewById(R.id.control_form)
        controlCloseButton = findViewById(R.id.close_button_control)
        pump = findViewById(R.id.pump)
        fan = findViewById(R.id.fan)
        led = findViewById(R.id.led)

        environmentName.text = SharedPreferences.getEnvironmentName(this)

        programsManager = ProgramsManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProgramsListAdapter(mutableListOf(), programsManager) { selectedProgram -> environmentOptionsLayout.visibility = View.GONE}

        recyclerView.adapter = adapter

        val slidingToggleControl = findViewById<SlidingToggleButton>(R.id.sliding_toggle_control)
        val slidingTogglePump = findViewById<SlidingToggleButton>(R.id.sliding_toggle_pump)
        val slidingToggleFan = findViewById<SlidingToggleButton>(R.id.sliding_toggle_fan)
        val slidingToggleLed = findViewById<SlidingToggleButton>(R.id.sliding_toggle_led)

        slidingToggleControl.onToggleChangedListener = { isOn ->
            controlValue = if (isOn) 1 else 0

            if (isOn) {
                pump.visibility = View.VISIBLE
                fan.visibility  = View.VISIBLE
                led.visibility  = View.VISIBLE
            } else {
                pump.visibility = View.GONE
                fan.visibility  = View.GONE
                led.visibility  = View.GONE
            }

            postCurrentControlStatus()
        }

        slidingToggleFan.onToggleChangedListener = { isOn ->
            fanValue = if (isOn) 1 else 0
            postCurrentControlStatus()
        }

        slidingTogglePump.onToggleChangedListener = { isOn ->
            pumpValue = if (isOn) 1 else 0
            postCurrentControlStatus()
        }

        slidingToggleLed.onToggleChangedListener = { isOn ->
            ledValue = if (isOn) 1 else 0
            postCurrentControlStatus()
        }

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

        controlButton.setOnClickListener {
            if(environmentOptionsLayout.visibility == View.VISIBLE){
                environmentOptionsLayout.visibility = View.GONE
                controlForm.visibility = View.VISIBLE

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val getControlStatusApi = retrofit.create(EnvironmentApiService::class.java)
                val call = getControlStatusApi.getControlStatus(environmentId)

                call.enqueue(object : Callback<ControlStatus> {
                    override fun onResponse(
                        call: Call<ControlStatus>,
                        response: Response<ControlStatus>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                if(response.body()!!.switchControl == 0){
                                    slidingToggleControl.setToggleState(false)
                                    slidingToggleFan.setToggleState(false)
                                    slidingTogglePump.setToggleState(false)
                                    slidingToggleLed.setToggleState(false)

                                    fan.visibility = View.GONE
                                    pump.visibility = View.GONE
                                    led.visibility = View.GONE
                                }
                                else{
                                    slidingToggleControl.setToggleState(true)

                                    fan.visibility = View.VISIBLE
                                    pump.visibility = View.VISIBLE
                                    led.visibility = View.VISIBLE

                                    if(response.body()!!.fan == 0){
                                        slidingToggleFan.setToggleState(false)
                                    }else{
                                        slidingToggleFan.setToggleState(true)
                                    }

                                    if(response.body()!!.pump == 0){
                                        slidingTogglePump.setToggleState(false)
                                    }else{
                                        slidingTogglePump.setToggleState(true)
                                    }

                                    if(response.body()!!.led == 0){
                                        slidingToggleLed.setToggleState(false)
                                    }else{
                                        slidingToggleLed.setToggleState(true)
                                    }
                                }
                            }
                        } else {
                            Log.d("ProgramsManager", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ControlStatus>, t: Throwable) {
                        Log.d("ProgramsManager", "API call failed: ${t.message}")
                    }
                })
            }
        }

        monitorButton.setOnClickListener {
            if(environmentOptionsLayout.visibility == View.VISIBLE){
                environmentOptionsLayout.visibility = View.GONE
                startActivity(Intent(this@ProgramsActivity, MonitorActivity::class.java))
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

        controlCloseButton.setOnClickListener{
            if(controlForm.visibility == View.VISIBLE){
                controlForm.visibility = View.GONE
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

                        if(temperatureField.text.isEmpty() || humidityField.text.isEmpty() || luminosityField.text.isEmpty()) {
                            Toast.makeText(this@ProgramsActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val temperature = (temperatureField.text.toString().trim()).toDouble()
                            val humidity = (humidityField.text.toString().trim()).toDouble()
                            val luminosity = (luminosityField.text.toString().trim()).toDouble()

                            if((temperature > -50 && temperature < 50) && (humidity > 0 && humidity < 100) && (luminosity > 0 && luminosity < 5)){
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

                                    recreate()
                                }
                            }
                            else{
                                if(temperature < -50 || temperature > 50){
                                    Toast.makeText(this@ProgramsActivity, "Temperature must be between -50° and 50° Celsius", Toast.LENGTH_SHORT).show()
                                }

                                if(humidity < 0 || humidity > 100){
                                    Toast.makeText(this@ProgramsActivity, "Humidity must be between 0% and 100%", Toast.LENGTH_SHORT).show()
                                }

                                if(luminosity < 0 || luminosity > 5){
                                    Toast.makeText(this@ProgramsActivity, "Luminosity must be between 0 and 5", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    hideKeyboard()
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

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}