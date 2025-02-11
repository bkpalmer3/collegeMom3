package com.example.accountabilityapp11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class PlanAdapter(
    private var planSnapshots: List<DocumentSnapshot>,
    private val onPlanClick: (DocumentSnapshot) -> Unit // Now passing DocumentSnapshot
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
        val documentSnapshot = planSnapshots[position]
        val plan = documentSnapshot.toObject(Plan::class.java)

        if (plan != null) {
            holder.titleTextView.text = plan.title
            holder.descriptionTextView.text = plan.description

            // Pass Firestore document snapshot to the click handler
            holder.itemView.setOnClickListener { onPlanClick(documentSnapshot) }
        }
    }

    override fun getItemCount(): Int = planSnapshots.size

    fun updatePlans(newSnapshots: QuerySnapshot) {
        planSnapshots = newSnapshots.documents
        notifyDataSetChanged()
    }
}


