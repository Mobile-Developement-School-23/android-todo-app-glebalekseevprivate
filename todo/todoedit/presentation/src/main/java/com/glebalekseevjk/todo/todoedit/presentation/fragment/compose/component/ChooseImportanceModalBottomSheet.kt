package com.glebalekseevjk.feature.todoitem.presentation.fragment.compose.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.design.R
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.AppTheme
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.transparent
import com.glebalekseevjk.todo.todoedit.presentation.fragment.compose.theme.typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChooseImportanceModalBottomSheet(
    initValue: Importance,
    onItemSelected: (Importance) -> Unit
) {
    val radioOptions = listOf(
        Importance.LOW.name,
        Importance.BASIC.name,
        Importance.IMPORTANT.name,
    )
    val selectedOption = remember { mutableStateOf(initValue.name) }
    val importanceArray = stringArrayResource(id = R.array.importance_array)
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()

    var isImportanceTarget by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isImportanceTarget)
    val importantColor by transition.animateColor(label = "colorTransition") { state ->
        if (state) AppTheme.colors.colorRed20 else transparent
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backPrimaryColor)
    ) {
        Text(
            text = stringResource(id = R.string.choose_importance),
            style = typography.title.copy(color = AppTheme.colors.labelPrimaryColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
        ) {
            for (option in radioOptions) {
                val isImportant = option == Importance.IMPORTANT.name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            onClick = {
                                selectedOption.value = option
                                onItemSelected(Importance.valueOf(option))
                                scope.launch {
                                    if (isImportant){
                                        isImportanceTarget = true
                                        delay(500)
                                        isImportanceTarget = false
                                    }
                                }
                            },
                            indication = null
                        )
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedOption.value == option,
                        onClick = {
                            selectedOption.value = option
                            onItemSelected(Importance.valueOf(option))
                            scope.launch {
                                if (isImportant){
                                    isImportanceTarget = true
                                    delay(500)
                                    isImportanceTarget = false
                                }
                            }
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colors.colorBlue)
                    )
                    Text(
                        text = when (Importance.valueOf(option)) {
                            Importance.LOW -> importanceArray[1]
                            Importance.BASIC -> importanceArray[0]
                            Importance.IMPORTANT -> importanceArray[2]
                        },
                        style = typography.body.copy(
                            color = if (isImportant) AppTheme.colors.colorRed else AppTheme.colors.labelPrimaryColor
                        ),
                        modifier = Modifier
                            .background(if (isImportant) importantColor else transparent, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}