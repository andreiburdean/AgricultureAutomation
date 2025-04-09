package com.example.agricultureautomationapp.programs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.ProgramItem

class ProgramFormDialog(
    private val programId: Int,
    private val initialTemperature: Double,
    private val initialHumidity: Double,
    private val initialLuminosity: Double,
    private val programsManager: ProgramsManager,
    private val listAdapter: ProgramsListAdapter
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.edit_program_dialog, null)

        val temperatureField = view.findViewById<EditText>(R.id.temperature_field)
        val humidityField = view.findViewById<EditText>(R.id.humidity_field)
        val luminosityField = view.findViewById<EditText>(R.id.luminosity_field)
        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        val editButton = view.findViewById<Button>(R.id.edit_program_button)

        temperatureField.setText(initialTemperature.toString())
        humidityField.setText(initialHumidity.toString())
        luminosityField.setText(initialLuminosity.toString())

        editButton.setOnClickListener {
            val newTemperature = temperatureField.text.toString().trim().toDoubleOrNull() ?: initialTemperature
            val newHumidity = humidityField.text.toString().trim().toDoubleOrNull() ?: initialHumidity
            val newLuminosity = luminosityField.text.toString().trim().toDoubleOrNull() ?: initialLuminosity

            val program = ProgramItem(
                programId = programId,
                temperature = newTemperature,
                humidity = newHumidity,
                luminosity = newLuminosity
            )

            programsManager.updateProgram(program) { updatedProgram ->
                listAdapter.updateCustomEnvItem(updatedProgram)
                Toast.makeText(context, "Program updated!", Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }

        closeButton.setOnClickListener { dismiss() }

        builder.setView(view)
        return builder.create().apply {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }
}

