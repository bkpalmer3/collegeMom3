package com.example.test

open class Goal (
    private val name: String,
//    private val followUpReport: String,

){
    fun displayInfo(){
        println("> $name")
    }
}


//Scrapped dead Code
//    private val taskList: MutableList<Task> = mutableListOf(), scrapped Idea
//        for (task in taskList){
//            task.displayInfo()
//    fun addTask(task: Task){
//        taskList.add(task)
//    }
