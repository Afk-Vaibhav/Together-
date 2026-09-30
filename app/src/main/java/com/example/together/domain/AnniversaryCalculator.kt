package com.example.together.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object AnniversaryCalculator {

    fun getNextMonthlyAnniversary(start: LocalDateTime, now: LocalDateTime): LocalDateTime {
        val startDate = start.toLocalDate()
        
        var candidateDate = now.toLocalDate()
        candidateDate = resolveAnniversaryDate(candidateDate.year, candidateDate.monthValue, startDate.dayOfMonth)
        
        var candidateDateTime = LocalDateTime.of(candidateDate, LocalTime.MIDNIGHT)
        
        if (candidateDateTime.isBefore(now) || candidateDateTime.isEqual(now)) {
            val nextMonth = now.toLocalDate().plusMonths(1)
            val nextMonthDate = resolveAnniversaryDate(nextMonth.year, nextMonth.monthValue, startDate.dayOfMonth)
            candidateDateTime = LocalDateTime.of(nextMonthDate, LocalTime.MIDNIGHT)
        }
        
        return candidateDateTime
    }
    
    fun getNextYearlyAnniversary(start: LocalDateTime, now: LocalDateTime): LocalDateTime {
        val startDate = start.toLocalDate()
        
        var candidateYear = now.year
        var candidateDate = resolveAnniversaryDate(candidateYear, startDate.monthValue, startDate.dayOfMonth)
        var candidateDateTime = LocalDateTime.of(candidateDate, LocalTime.MIDNIGHT)
        
        if (candidateDateTime.isBefore(now) || candidateDateTime.isEqual(now)) {
            candidateYear += 1
            candidateDate = resolveAnniversaryDate(candidateYear, startDate.monthValue, startDate.dayOfMonth)
            candidateDateTime = LocalDateTime.of(candidateDate, LocalTime.MIDNIGHT)
        }
        
        return candidateDateTime
    }
    
    private fun resolveAnniversaryDate(year: Int, month: Int, targetDayOfMonth: Int): LocalDate {
        val dateInMonth = LocalDate.of(year, month, 1)
        val lengthOfMonth = dateInMonth.lengthOfMonth()
        
        val actualDay = if (targetDayOfMonth > lengthOfMonth) lengthOfMonth else targetDayOfMonth
        return LocalDate.of(year, month, actualDay)
    }
}
