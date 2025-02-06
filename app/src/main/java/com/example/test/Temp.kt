package com.example.test

class Temp (
    name: String,
    private val isComplete: Boolean = false,
    private val isReported: Boolean = true,
) : Goal(name) {
}