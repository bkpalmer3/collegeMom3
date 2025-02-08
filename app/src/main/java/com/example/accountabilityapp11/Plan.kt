package com.example.accountabilityapp11

data class Plan(
    val title: String,
    val description: String,
    val goals: MutableList<Goal> = mutableListOf()
)