package com.example.accountabilityapp11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountabilityapp11.R

// Update the Goal model to include a flag indicating whether a checkbox should be shown.
data class Goal(val name: String, val hasCheckbox: Boolean = false)

class GoalsAdapter(
    private val goals: MutableList<Goal>,
    private val onGoalChecked: (goal: Goal, position: Int) -> Unit
) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalTitle: TextView = itemView.findViewById(R.id.goalTitle)
        val goalCheckbox: CheckBox = itemView.findViewById(R.id.goalCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.goalTitle.text = goal.name

        // Show the checkbox if the goal was created with the checkbox enabled.
        if (goal.hasCheckbox) {
            holder.goalCheckbox.visibility = View.VISIBLE
            // Remove any previous listener since views get recycled.
            holder.goalCheckbox.setOnCheckedChangeListener(null)
            holder.goalCheckbox.isChecked = false
            holder.goalCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onGoalChecked(goal, position)
                }
            }
        } else {
            holder.goalCheckbox.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = goals.size
}