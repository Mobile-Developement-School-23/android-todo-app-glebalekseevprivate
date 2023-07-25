package com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.design.R
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.typography
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDatePickerField(
    date: Date?,
    onInitDeadline: () -> Unit,
    onDisableDeadline: () -> Unit,
    onSetDeadline: (Date) -> Unit,
    dateFormatter: SimpleDateFormat,
    timeFormatter: SimpleDateFormat,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var openDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var openTimePickerDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    if (openDatePickerDialog && date != null) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
        DatePickerDialog(
            onDismissRequest = { openDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newDate = datePickerState.selectedDateMillis
                        scope.launch {
                            newDate ?: return@launch
                            onSetDeadline.invoke(Date(newDate))
                        }
                        openDatePickerDialog = false
                    }
                ) {
                    Text("ГОТОВО")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDatePickerDialog = false
                    }
                ) {
                    Text("ОТМЕНА")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (openTimePickerDialog && date != null){
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val timePickerState = rememberTimePickerState(
            initialHour = hours,
            initialMinute = minutes,
        )
        DatePickerDialog(
            onDismissRequest = { openTimePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val newHour = timePickerState.hour
                            val newMinute = timePickerState.minute
                            val date = Calendar.getInstance().apply {
                                set(Calendar.DAY_OF_MONTH, day)
                                set(Calendar.MONTH, month)
                                set(Calendar.YEAR, year)
                                set(Calendar.HOUR_OF_DAY, newHour)
                                set(Calendar.MINUTE, newMinute)
                            }.time
                            onSetDeadline.invoke(date)
                            openTimePickerDialog = false
                        }
                    }
                ) {
                    Text("ГОТОВО")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openTimePickerDialog = false
                    }
                ) {
                    Text("ОТМЕНА")
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }

    }


    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = stringResource(id = R.string.deadline),
                style = typography.body.copy(color = AppTheme.colors.labelPrimaryColor),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            date?.let {
                Row(modifier = Modifier) {
                    Button(
                        onClick = { openDatePickerDialog = true },
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.colorBlue,
                            contentColor = AppTheme.colors.colorWhite,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = dateFormatter.format(it),
                            style = typography.subhead,
                            modifier = Modifier,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { openTimePickerDialog = true },
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.colorBlue,
                            contentColor = AppTheme.colors.colorWhite,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = timeFormatter.format(it),
                            style = typography.subhead,
                            modifier = Modifier,
                        )
                    }
                }
            }
        }

        TodoItemSwitch(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        if (date == null) {
                            onInitDeadline.invoke()
                        } else {
                            onDisableDeadline.invoke()
                        }
                    }
                ),
            checked = date != null,
            checkedTrackColor = AppTheme.colors.colorBlue,
            uncheckedTrackColor = AppTheme.colors.colorBlue30,
            scale = 1.2f
        )
    }
}