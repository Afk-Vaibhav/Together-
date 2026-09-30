package com.example.together

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.together.data.RelationshipPreferencesRepository
import com.example.together.domain.CalendarDuration
import com.example.together.domain.ElapsedTime
import com.example.together.domain.RelationshipDurationCalculator
import com.example.together.notifications.AnniversaryScheduler
import com.example.together.widgets.DurationWidget
import com.example.together.widgets.ElapsedTimeWidget
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RelationshipPreferencesRepository(application)
    private val scheduler = AnniversaryScheduler(application)

    val isSetupCompleted: StateFlow<Boolean> = repository.isSetupCompleted
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val relationshipStart: StateFlow<LocalDateTime?> = repository.relationshipStart
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val monthlyNotificationsEnabled: StateFlow<Boolean> = repository.monthlyNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val yearlyNotificationsEnabled: StateFlow<Boolean> = repository.yearlyNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    private val _calendarDuration = MutableStateFlow<CalendarDuration?>(null)
    val calendarDuration: StateFlow<CalendarDuration?> = _calendarDuration

    private val _elapsedTime = MutableStateFlow<ElapsedTime?>(null)
    val elapsedTime: StateFlow<ElapsedTime?> = _elapsedTime

    init {
        viewModelScope.launch {
            repository.relationshipStart.collect { start ->
                if (start != null) {
                    startTimer(start)
                }
            }
        }
    }

    private fun startTimer(start: LocalDateTime) {
        viewModelScope.launch {
            while (true) {
                val now = LocalDateTime.now()
                _calendarDuration.value = RelationshipDurationCalculator.calculateCalendarDuration(start, now)
                _elapsedTime.value = RelationshipDurationCalculator.calculateElapsedTime(start, now)
                delay(1000)
            }
        }
    }

    fun saveRelationshipStart(date: LocalDate, time: LocalTime) {
        viewModelScope.launch {
            val dateTime = LocalDateTime.of(date, time)
            repository.saveRelationshipStart(dateTime)
            scheduler.scheduleAll()
            
            val context = getApplication<Application>()
            try {
                DurationWidget().updateAll(context)
                ElapsedTimeWidget().updateAll(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setMonthlyNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.setMonthlyNotificationsEnabled(enabled)
            scheduler.scheduleAll()
        }
    }

    fun setYearlyNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.setYearlyNotificationsEnabled(enabled)
            scheduler.scheduleAll()
        }
    }
}
