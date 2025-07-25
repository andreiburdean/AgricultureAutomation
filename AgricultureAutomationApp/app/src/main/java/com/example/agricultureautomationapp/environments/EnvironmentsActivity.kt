package com.example.agricultureautomationapp.environments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.EnvironmentItem
import com.example.agricultureautomationapp.programs.ProgramsActivity
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences

class EnvironmentsActivity : AppCompatActivity() {

    private lateinit var addButton: ImageButton
    private lateinit var addForm: View
    private lateinit var closeButton: ImageButton
    private lateinit var addEnvironmentButton: Button
    private lateinit var adapter: EnvironmentsListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var raspberryIdInput: EditText
    private lateinit var raspberryIpInput: EditText
    private lateinit var environmentNameInput: EditText
    private lateinit var environmentsManager: EnvironmentsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.environments_activity)

        addButton = findViewById(R.id.add_button)
        addForm = findViewById(R.id.add_form)
        closeButton = findViewById(R.id.close_button)
        addEnvironmentButton = findViewById(R.id.add_environment_button)
        recyclerView = findViewById(R.id.environments_recycler)
        raspberryIdInput = findViewById(R.id.raspberry_id_field)
        raspberryIpInput = findViewById(R.id.raspberry_ip_field)
        environmentNameInput = findViewById(R.id.environment_name_field)

        environmentsManager = EnvironmentsManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EnvironmentsListAdapter(mutableListOf(), environmentsManager) { selectedEnvironment ->
            val intent = Intent(this@EnvironmentsActivity, ProgramsActivity::class.java)
            selectedEnvironment.environmentId?.let { environmentId ->
                SharedPreferences.saveEnvironmentId(this, environmentId)
            }
            selectedEnvironment.environmentName.let { environmentName ->
                SharedPreferences.saveEnvironmentName(this, environmentName)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            addForm.visibility = View.VISIBLE
        }

        closeButton.setOnClickListener {
            addForm.visibility = View.GONE
            raspberryIdInput.setText("")
            raspberryIpInput.setText("")
            environmentNameInput.setText("")
        }

        addEnvironmentButton.setOnClickListener {
            if(raspberryIdInput.text.isEmpty() || raspberryIpInput.text.isEmpty()
                || environmentNameInput.text.isEmpty()) {
                Toast.makeText(this@EnvironmentsActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                hideKeyboard()
            }
            else if(raspberryIdInput.text.toString().trim().toIntOrNull() == null){
                Toast.makeText(this@EnvironmentsActivity, "Wrong ID format", Toast.LENGTH_SHORT).show()
                hideKeyboard()
            }
            else if(!ipv4Regex.matches(raspberryIpInput.text.toString().trim())){
                Toast.makeText(this@EnvironmentsActivity, "Wrong IP format", Toast.LENGTH_SHORT).show()
                hideKeyboard()
            }
            else{
                val raspberryId = raspberryIdInput.text.toString().toInt()
                val raspberryIp = raspberryIpInput.text.toString().trim()
                val envName = environmentNameInput.text.toString().trim()

                val newEnvironment = EnvironmentItem(raspberryId = raspberryId,
                    raspberryIp =  raspberryIp, environmentName = envName)
                environmentsManager.addEnvironment(newEnvironment) { updatedList ->
                    adapter.updateList(updatedList)
                    Toast.makeText(this, "Environment added!", Toast.LENGTH_SHORT).show()
                    raspberryIdInput.setText("")
                    raspberryIpInput.setText("")
                    environmentNameInput.setText("")
                    addForm.visibility = View.GONE
                }
            }

            hideKeyboard()
        }

        environmentsManager.fetchEnvironments { environments ->
            adapter.updateList(environments)
        }
    }

    private val ipv4Regex = Regex("""^((25[0-5]|2[0-4]\d|1?\d?\d)\.){3}(25[0-5]|2[0-4]\d|1?\d?\d)$""")


    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
