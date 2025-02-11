package com.example.accountabilityapp11

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

object PlanRepository {
    private val db = FirebaseFirestore.getInstance()
    private val plansCollection = db.collection("plans")

    val plans = mutableListOf<Plan>()

    fun fetchPlans(onComplete: () -> Unit) {
        plansCollection.get()
            .addOnSuccessListener { result ->
                plans.clear()
                plans.addAll(result.toObjects<Plan>())
                onComplete()
            }
            .addOnFailureListener { e ->
                println("Error fetching plans: $e")
            }
    }

    fun addPlan(plan: Plan, onComplete: () -> Unit) {
        plansCollection.add(plan)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> println("Error adding plan: $e") }
    }
}
