package com.example.together.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.together.MainViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SetupScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showStartButton by remember { mutableStateOf(false) }

    var isFutureError by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate, selectedTime) {
        if (selectedDate != null && selectedTime != null) {
            val dateTime = java.time.LocalDateTime.of(selectedDate, selectedTime)
            if (dateTime.isAfter(java.time.LocalDateTime.now())) {
                isFutureError = true
                showStartButton = false
            } else {
                isFutureError = false
                showStartButton = true
            }
        }
    }

    val alpha by animateFloatAsState(targetValue = if (showStartButton) 1f else 0f, animationSpec = tween(500))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧿", fontSize = 48.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(
            text = "Together with!🤍",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
        )
        Text(
            text = "Every second counts.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
            modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
        )

        Button(
            onClick = {
                val now = LocalDate.now()
                DatePickerDialog(context, { _, year, month, day ->
                    selectedDate = LocalDate.of(year, month + 1, day)
                }, selectedDate?.year ?: now.year, (selectedDate?.monthValue ?: now.monthValue) - 1, selectedDate?.dayOfMonth ?: now.dayOfMonth).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(
                text = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "When did it start?",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val now = LocalTime.now()
                TimePickerDialog(context, { _, hour, minute ->
                    selectedTime = LocalTime.of(hour, minute)
                }, selectedTime?.hour ?: now.hour, selectedTime?.minute ?: now.minute, false).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(
                text = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "What time?",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        if (isFutureError) {
            Text(
                text = "That date is in the future.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        if (showStartButton && !isFutureError) {
            Button(
                onClick = {
                    viewModel.saveRelationshipStart(selectedDate!!, selectedTime!!)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .alpha(alpha)
            ) {
                Text(
                    text = "Start Counting 🤎",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
