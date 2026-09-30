package com.example.together.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SettingsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val start by viewModel.relationshipStart.collectAsState()
    val monthlyEnabled by viewModel.monthlyNotificationsEnabled.collectAsState()
    val yearlyEnabled by viewModel.yearlyNotificationsEnabled.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingDate by remember { mutableStateOf<LocalDate?>(null) }
    var pendingTime by remember { mutableStateOf<LocalTime?>(null) }
    
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Change relationship date?") },
            text = { Text("Changing this will recalculate your relationship duration.") },
            confirmButton = {
                TextButton(onClick = {
                    val finalDate = pendingDate ?: start?.toLocalDate() ?: LocalDate.now()
                    val finalTime = pendingTime ?: start?.toLocalTime() ?: LocalTime.now()
                    viewModel.saveRelationshipStart(finalDate, finalTime)
                    showConfirmDialog = false
                    pendingDate = null
                    pendingTime = null
                }) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    pendingDate = null
                    pendingTime = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Relationship", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        SettingsItem("Edit relationship date", start?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))) {
            val now = LocalDate.now()
            DatePickerDialog(context, { _, y, m, d ->
                pendingDate = LocalDate.of(y, m + 1, d)
                pendingTime = pendingTime ?: start?.toLocalTime()
                showConfirmDialog = true
            }, start?.year ?: now.year, (start?.monthValue ?: now.monthValue) - 1, start?.dayOfMonth ?: now.dayOfMonth).show()
        }

        SettingsItem("Edit relationship time", start?.format(DateTimeFormatter.ofPattern("hh:mm a"))) {
            val now = LocalTime.now()
            TimePickerDialog(context, { _, h, m ->
                pendingTime = LocalTime.of(h, m)
                pendingDate = pendingDate ?: start?.toLocalDate()
                showConfirmDialog = true
            }, start?.hour ?: now.hour, start?.minute ?: now.minute, false).show()
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Text("Notifications", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        SettingsToggleItem("Monthly anniversary", "Get a little reminder every month 🤎", monthlyEnabled) {
            viewModel.setMonthlyNotifications(it)
        }
        SettingsToggleItem("Yearly anniversary", "Get a little reminder every year 🤎", yearlyEnabled) {
            viewModel.setYearlyNotifications(it)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Privacy", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Everything stays on this phone.\nNo account. No cloud. No tracking.", color = Color.Gray, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("About", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Together", color = Color.White, fontSize = 16.sp)
        Text("Version 1.0", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String?, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(title, color = Color.White, fontSize = 16.sp)
            if (subtitle != null) {
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SettingsToggleItem(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 16.sp)
            Text(subtitle, color = Color.Gray, fontSize = 14.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
        )
    }
}
