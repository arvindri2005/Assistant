package com.arvind.assistant.screens.createCourse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.AssistantTextField
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.screens.createCourse.components.ScheduleBottomSheet
import com.arvind.assistant.screens.createCourse.components.ScheduleClassListItem


@Composable
fun CreateCourseScreen(
    createCourse: (courseName: String, requiredAttendance: Double, scheduleClasses: List<ClassScheduleDetails>) -> Unit,
){
    val viewModel = viewModel<CreateCourseViewModel>()

    Scaffold(
        floatingActionButton = {
            AssistantFAB(
                icon = Icons.Filled.Add,
                text = "Save",
                onClick = {
                    createCourse(viewModel.getCourseName(), viewModel.getRequiredAttendance(), viewModel.getClassForTheCourse())
                }
            )
        }
    ) { it ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AssistantTextField(
                    inputValue = viewModel.getCourseName(),
                    onInputChange = {
                        viewModel.updateCourseName(it)
                    }
                )

                AssistantTextField(
                    inputValue = viewModel.getRequiredAttendance().toString(),
                    onInputChange = {
                        viewModel.updateRequiredAttendance(it.toDouble())
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

                AssistantButton(text = "Add Schedule Classes") {
                    viewModel.updateBottomSheetState(true)
                }

                Column {
                    viewModel.getClassForTheCourse().forEachIndexed { index, classDetail ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ScheduleClassListItem(
                            item = classDetail,
                            onClick = {
                                viewModel.updateClassToUpdateIndex(index)
                                viewModel.updateBottomSheetState(true)
                            },
                            onCloseClick = {
                                viewModel.deleteClassForTheCourse(index)
                            }
                        )
                    }
                }
                
                
            }
            
            if(viewModel.getBottomSheetState()){
                ScheduleBottomSheet(
                    initialState = viewModel.getClassToUpdateIndex()?.let {
                        viewModel.getSingleClassForTheCourse(it)
                    },

                    onCreateClass = { params ->
                        viewModel.getClassToUpdateIndex()?.let {
                                viewModel.updateClassInfo(it, params)
                                viewModel.updateClassToUpdateIndex(null)
                        } ?: viewModel.addClassForTheCourse(params)
                    },
                    onDismissRequest = {
                        viewModel.updateBottomSheetState(false)
                    }
                )
            }
        }
    }

}