package com.example.agricultureautomationapp.programs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.gfg.custom_spinner_kotlin.SpinnerAdapter

class ProgramsActivity : AppCompatActivity() {

    private lateinit var addForm: View
    private lateinit var closeButton: ImageButton
    private lateinit var addProgramButton: Button
    private lateinit var adapter: ProgramsListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var programsManager: ProgramsManager
    private lateinit var showUpperBarSpinnerButton: View
    private lateinit var upperBarSpinner: Spinner
    private lateinit var upperBarSpinnerLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.programs_activity)

        showUpperBarSpinnerButton = findViewById(R.id.options_button)
        addForm = findViewById(R.id.add_form)
        closeButton = findViewById(R.id.close_button)
        addProgramButton = findViewById(R.id.add_program_button)
        recyclerView = findViewById(R.id.programs_recycler)
        upperBarSpinner = findViewById(R.id.options_spinner)
        upperBarSpinnerLayout = findViewById(R.id.options_spinner_layout)

        programsManager = ProgramsManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProgramsListAdapter(mutableListOf(), programsManager)
        recyclerView.adapter = adapter

        programsManager.fetchPrograms { programs ->
            adapter.updateList(programs)
        }

        val items = listOf(
            SpinnerItem("Add"),
            SpinnerItem("Control"),
        )

        val adapter = SpinnerAdapter(this, items)
        upperBarSpinner.adapter = adapter

        showUpperBarSpinnerButton.setOnClickListener {
            upperBarSpinnerLayout.visibility = View.VISIBLE
            upperBarSpinner.performClick()
        }

        upperBarSpinner.adapter = adapter
        upperBarSpinner.setSelection(0, false)

        upperBarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as? SpinnerItem
                Log.d("SpinnerDebug", "Selected: ${selectedItem?.itemName}")

                when (selectedItem?.itemName) {
                    "Add" -> {
                        Log.d("SpinnerDebug", "Add selected")
                        Toast.makeText(this@ProgramsActivity, "Add selected", Toast.LENGTH_SHORT).show()
                    }
                    "Control" -> {
                        Log.d("SpinnerDebug", "Control selected")
                        Toast.makeText(this@ProgramsActivity, "Control selected", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.d("SpinnerDebug", "Unknown selection")
                    }
                }

                upperBarSpinnerLayout.visibility = View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("SpinnerDebug", "Nothing selected")
                upperBarSpinnerLayout.visibility = View.GONE
            }
        }

    }
}