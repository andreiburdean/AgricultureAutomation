package com.example.agricultureautomationapp.environments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.models.EnvironmentItem

class EnvironmentsListAdapter(private var environments: MutableList<EnvironmentItem>, private val environmentsManager: EnvironmentsManager, private val onItemClick: (EnvironmentItem) -> Unit) : RecyclerView.Adapter<EnvironmentsListAdapter.EnvironmentViewHolder>() {

    class EnvironmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val environmentName: TextView = itemView.findViewById(R.id.environment_name)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvironmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.environment_item, parent, false)
        return EnvironmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnvironmentViewHolder, position: Int) {
        val environment = environments[position]
        holder.environmentName.text = environment.environmentName

        holder.itemView.setOnClickListener {
            onItemClick(environment)
        }

        holder.deleteButton.setOnClickListener {
            environmentsManager.deleteEnvironment(environment) { success ->
                if (success) {
                    removeItem(environment)
                } else {
                    Log.e("EnvironmentsListAdapter", "Failed to delete environment from API")
                }
            }
        }
    }

    override fun getItemCount(): Int = environments.size

    fun updateList(newEnvironments: List<EnvironmentItem>) {
        Log.d("EnvironmentsListAdapter", "New environments size: ${newEnvironments.size}")
        environments.clear()
        environments.addAll(newEnvironments)
        notifyDataSetChanged()
    }

    fun removeItem(environment: EnvironmentItem) {
        environments.remove(environment)
        notifyDataSetChanged()
    }
}

