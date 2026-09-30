package com.example.together.domain

data class CalendarDuration(
    val years: Int,
    val months: Int,
    val days: Int
)

data class ElapsedTime(
    val days: Long,
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)
