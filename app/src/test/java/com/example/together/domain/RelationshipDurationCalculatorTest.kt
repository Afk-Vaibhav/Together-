package com.example.together.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class RelationshipDurationCalculatorTest {

    @Test
    fun testLessThanOneDay() {
        val start = LocalDateTime.of(2023, 1, 1, 10, 0)
        val now = LocalDateTime.of(2023, 1, 1, 15, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(0, duration.days)
        assertEquals(0, duration.months)
        assertEquals(0, duration.years)
        
        val elapsed = RelationshipDurationCalculator.calculateElapsedTime(start, now)
        assertEquals(0, elapsed.days)
        assertEquals(5, elapsed.hours)
    }

    @Test
    fun testExactlyOneDay() {
        val start = LocalDateTime.of(2023, 1, 1, 10, 0)
        val now = LocalDateTime.of(2023, 1, 2, 10, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(1, duration.days)
        
        val elapsed = RelationshipDurationCalculator.calculateElapsedTime(start, now)
        assertEquals(1, elapsed.days)
    }

    @Test
    fun testOneMonth() {
        val start = LocalDateTime.of(2023, 1, 1, 10, 0)
        val now = LocalDateTime.of(2023, 2, 1, 10, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(0, duration.days)
        assertEquals(1, duration.months)
    }

    @Test
    fun testOneYear() {
        val start = LocalDateTime.of(2023, 1, 1, 10, 0)
        val now = LocalDateTime.of(2024, 1, 1, 10, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(1, duration.years)
    }

    @Test
    fun testOneYearOneMonth() {
        val start = LocalDateTime.of(2023, 1, 1, 10, 0)
        val now = LocalDateTime.of(2024, 2, 1, 10, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(1, duration.years)
        assertEquals(1, duration.months)
        assertEquals(0, duration.days)
    }

    @Test
    fun testLeapYear() {
        val start = LocalDateTime.of(2024, 2, 28, 10, 0)
        val now = LocalDateTime.of(2024, 3, 1, 10, 0)
        
        val duration = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
        assertEquals(0, duration.years)
        assertEquals(0, duration.months)
        assertEquals(2, duration.days)
    }
}
