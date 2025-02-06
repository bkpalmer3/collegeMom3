package com.example.test

fun main() {
    val testUser = User("Mom", "131513")
    val testPlan = Plan(
        testUser,
        "Mom Goals",
        "I want to be a better mom and therefore do things that will make me a better mom"
    )
    val testGoal1 = Eternal("Power walk with the Moms every tuesday")
    val testGoal2 = Temp("Go to the PTA meeting on saturday")
    val testGoal3 = Temp("Make cookies for the Smith's across the street")

    testUser.addPlan(testPlan)
    testPlan.addGoal(testGoal1)
    testPlan.addGoal(testGoal2)
    testPlan.addGoal(testGoal3)

    testUser.displayInfo()
}
