package com.example.test

class Plan (
    private val user: User,
    private val title: String,
    private val description: String,
    private val goalList: MutableList<Goal> = mutableListOf(),
) {
    fun displayInfo(){
        println("Title: $title \nAbout: $description\n\nGoals:")
        for (goal in goalList) {
            goal.displayInfo()
        }
    }
    fun addGoal(goal: Goal){
        goalList.add(goal)
    }
}