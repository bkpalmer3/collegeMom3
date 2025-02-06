package com.example.test

class User (
    private val userName: String,
    private val userId: String,
    private val planList: MutableList<Plan> = mutableListOf(),
//    private val email: String,
//    private val password: String,
//    private val profilePicture: String,
) {
    fun displayInfo(){
        println("\nUsername: $userName")
        println("User ID: $userId")
        println("\nPlans")
        for (plan in planList){
            plan.displayInfo()
        }
    }
    fun addPlan(plan: Plan){
        planList.add(plan)
    }
}