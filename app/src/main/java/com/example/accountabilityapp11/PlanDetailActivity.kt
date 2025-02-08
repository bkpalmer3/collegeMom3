package com.example.accountabilityapp11

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
        val planIndex = intent.getIntExtra("planIndex", -1)
        if (planIndex == -1 || planIndex >= PlanRepository.plans.size) {
            finish()
            return
        }
        currentPlan = PlanRepository.plans[planIndex]

        planTitleBanner = findViewById(R.id.planTitleBanner)
        planDescriptionBanner = findViewById(R.id.planDescriptionBanner)
        planTitleBanner.text = currentPlan.title
        planDescriptionBanner.text = currentPlan.description

        goalsRecyclerView = findViewById(R.id.goalsRecyclerView)
        goalsRecyclerView.layoutManager = LinearLayoutManager(this)
        goalsAdapter = GoalsAdapter(currentPlan.goals) { goal, position ->
            // Remove the goal when its checkbox is checked.
            currentPlan.goals.removeAt(position)
            goalsAdapter.notifyItemRemoved(position)
            Toast.makeText(this, "Goal completed and removed", Toast.LENGTH_SHORT).show()
        }
        goalsRecyclerView.adapter = goalsAdapter

        val addGoalButton = findViewById<FloatingActionButton>(R.id.addGoalButton)
        addGoalButton.setOnClickListener {
            showAddGoalDialog()
        }
    }

    private fun showAddGoalDialog() {
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
                // Reminder date/time processing omitted for brevity.
                if (goalTitle.isNotEmpty()) {
                    // Create the goal using the checkbox option selected.
                    currentPlan.goals.add(Goal(goalTitle, checkboxSelected))
                    goalsAdapter.notifyItemInserted(currentPlan.goals.size - 1)
                    Toast.makeText(this, "Goal added: $goalTitle", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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