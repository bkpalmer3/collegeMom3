package com.example.accountabilityapp11

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.accountabilityapp11.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlanDetailActivity : AppCompatActivity() {

    private lateinit var planTitleBanner: TextView
    private lateinit var planDescriptionBanner: TextView
    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var currentPlan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the plan index from intent extras.
        val planId = intent.getStringExtra("planId")
        if (planId == null) {
            finish() // Close activity if no ID is provided
            return
        }

        // Initialize views
        planTitleBanner = findViewById(R.id.planTitleBanner)
        planDescriptionBanner = findViewById(R.id.planDescriptionBanner)
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)

        // Fetch the plan from Firestore
        fetchPlanFromFirestore(planId)

        // Set up the RecyclerView (this will be done once the data is fetched)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)

        val addGoalButton = findViewById<FloatingActionButton>(R.id.addGoalButton)
        addGoalButton.setOnClickListener {
            // Log to verify if the click is being detected
            Log.d("PlanDetailActivity", "Add goal button clicked")
            showAddGoalDialog(planId)
        }
    }

    private fun fetchPlanFromFirestore(planId: String) {
        val db = FirebaseFirestore.getInstance()
        val planRef = db.collection("plans").document(planId)

        planRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    currentPlan = document.toObject(Plan::class.java)!!

                    // Now that currentPlan is initialized, update the UI
                    planTitleBanner.text = currentPlan.title
                    planDescriptionBanner.text = currentPlan.description

                    // Initialize adapter with fetched goals
                    goalsAdapter = GoalsAdapter(currentPlan.goals) { goal, position ->
                        removeGoalFromFirestore(planId, goal, position)
                    }
                    goalsRecyclerView.adapter = goalsAdapter
                } else {
                    finish() // If plan doesn't exist, close the activity
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching plan: $e")
                finish()
            }
    }

    private fun showAddGoalDialog(planId: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_goal, null)
        val editTextGoalTitle = dialogView.findViewById<EditText>(R.id.editTextGoalTitle)
        val editTextReminderDate = dialogView.findViewById<EditText>(R.id.editTextReminderDate)
        val editTextReminderTime = dialogView.findViewById<EditText>(R.id.editTextReminderTime)
        val btnCheckbox = dialogView.findViewById<Button>(R.id.btnCheckbox)

        var checkboxSelected = false
        btnCheckbox.setOnClickListener {
            checkboxSelected = !checkboxSelected
            btnCheckbox.text = if (checkboxSelected) "Checkbox Selected" else "Checkbox"
        }

        val calendar = Calendar.getInstance()
        editTextReminderDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    editTextReminderDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        editTextReminderTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                    editTextReminderTime.setText(formattedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Add Goal")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val goalTitle = editTextGoalTitle.text.toString().trim()
                if (goalTitle.isNotEmpty()) {
                    val newGoal = Goal(goalTitle, checkboxSelected)
                    addGoalToFirestore(planId, newGoal)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun addGoalToFirestore(planId: String, goal: Goal) {
        val db = FirebaseFirestore.getInstance()
        val planRef = db.collection("plans").document(planId)

        planRef.update("goals", FieldValue.arrayUnion(goal)) // Use Firestore arrayUnion to add a goal
            .addOnSuccessListener {
                currentPlan.goals.add(goal)
                goalsAdapter.notifyItemInserted(currentPlan.goals.size - 1)
                Toast.makeText(this, "Goal added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                println("Error adding goal: $e")
            }
    }

    private fun removeGoalFromFirestore(planId: String, goal: Goal, position: Int) {
        val db = FirebaseFirestore.getInstance()
        val planRef = db.collection("plans").document(planId)

        planRef.update("goals", FieldValue.arrayRemove(goal))
            .addOnSuccessListener {
                currentPlan.goals.removeAt(position)
                goalsAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "Goal completed and removed", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                println("Error removing goal: $e")
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
