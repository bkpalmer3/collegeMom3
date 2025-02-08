package com.example.accountabilityapp11

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountabilityapp11.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
2
class MainActivity : AppCompatActivity() {

    // Use the shared repository for plans
    private val plans = PlanRepository.plans
    private lateinit var recyclerView: RecyclerView
    private lateinit var planAdapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.plansRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize PlanAdapter. When a plan is clicked, pass its index in the repository.
        planAdapter = PlanAdapter(plans) { plan ->
            val planIndex = plans.indexOf(plan)
            val intent = Intent(this, PlanDetailActivity::class.java)
            intent.putExtra("planIndex", planIndex)
            startActivity(intent)
        }
        recyclerView.adapter = planAdapter

        // Floating Action Button to add a new plan
        val addPlanButton = findViewById<FloatingActionButton>(R.id.addPlanButton)
        addPlanButton.setOnClickListener {
            showAddPlanDialog()
        }
    }

    private fun showAddPlanDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_plan, null)
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)

        AlertDialog.Builder(this)
            .setTitle("Add Plan")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val title = editTextTitle.text.toString().trim()
                val description = editTextDescription.text.toString().trim()
                if (title.isNotEmpty() || description.isNotEmpty()) {
                    // When a plan is added, it starts with an empty goals list.
                    plans.add(Plan(title, description))
                    planAdapter.notifyItemInserted(plans.size - 1)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}