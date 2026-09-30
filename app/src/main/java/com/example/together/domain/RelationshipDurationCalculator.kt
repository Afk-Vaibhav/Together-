package com.example.together.domain

import java.time.Duration
import java.time.LocalDateTime
import java.time.Period

object RelationshipDurationCalculator {

    fun calculateCalendarDuration(start: LocalDateTime, now: LocalDateTime): CalendarDuration {
        if (now.isBefore(start)) {
            return CalendarDuration(0, 0, 0)
        }
        
        var tempNow = now
        
        // If the current time is before the start time on the respective day,
        // subtract one day for correct calendar calculation.
        if (now.toLocalTime().isBefore(start.toLocalTime())) {
            tempNow = now.minusDays(1)
        }
        
        val period = Period.between(start.toLocalDate(), tempNow.toLocalDate())
        
        return CalendarDuration(
            years = period.years,
            months = period.months,
            days = period.days
        )
    }

    fun calculateElapsedTime(start: LocalDateTime, now: LocalDateTime): ElapsedTime {
        if (now.isBefore(start)) {
            return ElapsedTime(0, 0, 0, 0)
        }
        
        val duration = Duration.between(start, now)
        val totalSeconds = duration.seconds
        
        val days = totalSeconds / (24 * 3600)
        val remainingAfterDays = totalSeconds % (24 * 3600)
        
        val hours = remainingAfterDays / 3600
        val remainingAfterHours = remainingAfterDays % 3600
        
        val minutes = remainingAfterHours / 60
        val seconds = remainingAfterHours % 60
        
        return ElapsedTime(
            days = days,
            hours = hours.toInt(),
            minutes = minutes.toInt(),
            seconds = seconds.toInt()
        )
    }
}
