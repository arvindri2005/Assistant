package com.arvind.assistant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantTextField


@Composable
fun CreateCourseScreen(
    createCourse: (courseName: String, requiredAttendance: Double) -> Unit
){

    val courseName = rememberSaveable{
        mutableStateOf("")
    }

    val requiredAttendance = rememberSaveable{
        mutableDoubleStateOf(75.0)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AssistantTextField(
                inputValue = courseName.value,
                onInputChange = {
                    courseName.value = it
                }
            )

            AssistantTextField(
                inputValue = requiredAttendance.doubleValue.toString(),
                onInputChange = {
                    requiredAttendance.doubleValue= it.toDouble()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            AssistantButton(text = "Save") {
                createCourse(courseName.value, requiredAttendance.doubleValue)
            }
        }

    }


}