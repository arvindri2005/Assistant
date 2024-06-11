package com.arvind.assistant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.AssistantTextField
import com.arvind.assistant.createCourse.ScheduleBottomSheet
import com.arvind.assistant.db.ClassScheduleDetails


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

    val classesForTheCourse = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf<ClassScheduleDetails>()
    }

    var showAddClassBottomSheet = rememberSaveable{
        mutableStateOf(false)
    }

    var classToUpdateIndex: MutableState<Nothing?> = rememberSaveable{
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            AssistantFAB(
                icon = Icons.Filled.Add,
                text = "Save",
                onClick = {
                    createCourse(courseName.value, requiredAttendance.doubleValue)
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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

                AssistantButton(text = "Add Schedule Classes") {
                    showAddClassBottomSheet.value = true
                }
                
                
            }
            
            if(showAddClassBottomSheet.value){
                ScheduleBottomSheet(onDismissRequest = { showAddClassBottomSheet.value = false }) {

                }
            }

        }
    }

}