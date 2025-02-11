package com.example.accountabilityapp11

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore

class MainActivity : AppCompatActivity() {
    private val plans = mutableListOf<DocumentSnapshot>() // Change to store DocumentSnapshot
    private lateinit var recyclerView: RecyclerView
    private lateinit var planAdapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.plansRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize PlanAdapter, now passing DocumentSnapshot
        planAdapter = PlanAdapter(plans) { documentSnapshot ->
            val planId = documentSnapshot.id // Retrieve Firestore document ID
            val intent = Intent(this, PlanDetailActivity::class.java)
            intent.putExtra("planId", planId) // Pass planId as string to next activity
            startActivity(intent)
        }
        recyclerView.adapter = planAdapter

        val addPlanButton = findViewById<FloatingActionButton>(R.id.addPlanButton)
        addPlanButton.setOnClickListener {
            showAddPlanDialog()
        }

        // Load plans from Firestore
        loadPlansFromFirestore()
    }

    private fun loadPlansFromFirestore() {
        db.collection("plans")
            .get()
            .addOnSuccessListener { result ->
                plans.clear()
                // Store DocumentSnapshot objects in the plans list
                plans.addAll(result.documents)
                planAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                println("Error loading plans: $e")
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
                    val newPlan = Plan(title, description)
                    db.collection("plans").add(newPlan)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Plan added!", Toast.LENGTH_SHORT).show()
                            loadPlansFromFirestore() // Refresh the plan list after adding
                        }
                        .addOnFailureListener { e ->
                            println("Error adding plan: $e")
                        }
                } else {
                    Toast.makeText(this, "Please provide a title or description", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun listenForRealTimeUpdates() {
        db.collection("plans")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Error listening for plan updates: $e")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    // Update plans with DocumentSnapshot objects
                    plans.clear()
                    plans.addAll(it.documents)
                    planAdapter.notifyDataSetChanged()
                }
            }
    }
}


