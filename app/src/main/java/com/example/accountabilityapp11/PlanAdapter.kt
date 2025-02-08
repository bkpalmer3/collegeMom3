package com.example.accountabilityapp11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountabilityapp11.R

class PlanAdapter(
    private val plans: List<Plan>,
    private val onPlanClick: (Plan) -> Unit
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.planTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.planDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.titleTextView.text = plan.title
        holder.descriptionTextView.text = plan.description

        // Set the click listener for this item
        holder.itemView.setOnClickListener { onPlanClick(plan) }
    }

    override fun getItemCount(): Int = plans.size
} 