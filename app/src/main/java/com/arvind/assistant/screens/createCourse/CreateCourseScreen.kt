package com.arvind.assistant.screens.createCourse

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.AssistantTextField
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.screens.createCourse.components.ScheduleBottomSheet
import com.arvind.assistant.screens.createCourse.components.ScheduleClassListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseScreen(
    createCourse: (courseName: String, requiredAttendance: Double, scheduleClasses: List<ClassScheduleDetails>) -> Unit,
){
    val viewModel = viewModel<CreateCourseViewModel>()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        floatingActionButton = {
            AssistantFAB(
                icon = Icons.Filled.Add,
                text = "Save",
                onClick = {
                    createCourse(viewModel.getCourseName(), viewModel.getRequiredAttendance(), viewModel.getClassForTheCourse())
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Create New  Course")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                scrollBehavior = scrollBehavior
            )
        }
    ) { it ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                AssistantTextField(
                    inputValue = viewModel.getCourseName(),
                    onInputChange = {
                        viewModel.updateCourseName(it)
                    },
                    label = { Text("Enter Course Name") },
                    modifier = Modifier
                        .padding( vertical = 5.dp)
                )

                Text(
                    text = "Required Attendance: ${viewModel.getRequiredAttendance().toInt()}%",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )

                Slider(
                    value = viewModel.getRequiredAttendance().toFloat(),
                    onValueChange ={
                        viewModel.updateRequiredAttendance(it.toDouble())
                    },
                    valueRange = 0f..100f,
                    steps = 100,
                )

                AssistantButton(text = "Add Schedule Classes") {
                    viewModel.updateBottomSheetState(true)
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
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

@Preview
@Composable
fun PreviewCreateCourseScreen(){
    CreateCourseScreen(
        createCourse = { _, _, _ ->

        }
    )
}