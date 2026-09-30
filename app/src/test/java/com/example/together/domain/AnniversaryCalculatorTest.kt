package com.example.together.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class AnniversaryCalculatorTest {

    @Test
    fun testMonthlyAnniversaryJan31() {
        val start = LocalDateTime.of(2024, 1, 31, 10, 0)
        
        // Next anniversary from Feb 1 should be Feb 29 (leap year)
        val now = LocalDateTime.of(2024, 2, 1, 10, 0)
        val next = AnniversaryCalculator.getNextMonthlyAnniversary(start, now)
        assertEquals(29, next.dayOfMonth)
        assertEquals(2, next.monthValue)
        
        // Next anniversary from Mar 1 should be Mar 31
        val now2 = LocalDateTime.of(2024, 3, 1, 10, 0)
        val next2 = AnniversaryCalculator.getNextMonthlyAnniversary(start, now2)
        assertEquals(31, next2.dayOfMonth)
        assertEquals(3, next2.monthValue)
        
        // Next anniversary from Apr 1 should be Apr 30
        val now3 = LocalDateTime.of(2024, 4, 1, 10, 0)
        val next3 = AnniversaryCalculator.getNextMonthlyAnniversary(start, now3)
        assertEquals(30, next3.dayOfMonth)
        assertEquals(4, next3.monthValue)
    }

    @Test
    fun testYearlyAnniversaryFeb29() {
        val start = LocalDateTime.of(2024, 2, 29, 10, 0)
        
        // Next anniversary from Feb 20, 2025 should be Feb 28, 2025 (non-leap year fallback)
        val now = LocalDateTime.of(2025, 2, 20, 10, 0)
        val next = AnniversaryCalculator.getNextYearlyAnniversary(start, now)
        assertEquals(28, next.dayOfMonth)
        assertEquals(2, next.monthValue)
        assertEquals(2025, next.year)
        
        // Next anniversary from Mar 1, 2025 should be Feb 28, 2026
        val now2 = LocalDateTime.of(2025, 3, 1, 10, 0)
        val next2 = AnniversaryCalculator.getNextYearlyAnniversary(start, now2)
        assertEquals(28, next2.dayOfMonth)
        assertEquals(2, next2.monthValue)
        assertEquals(2026, next2.year)
        
        // Next anniversary from Mar 1, 2027 should be Feb 29, 2028 (leap year)
        val now3 = LocalDateTime.of(2027, 3, 1, 10, 0)
        val next3 = AnniversaryCalculator.getNextYearlyAnniversary(start, now3)
        assertEquals(29, next3.dayOfMonth)
        assertEquals(2, next3.monthValue)
        assertEquals(2028, next3.year)
    }
}
