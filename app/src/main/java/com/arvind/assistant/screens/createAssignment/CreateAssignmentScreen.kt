package com.arvind.assistant.screens.createAssignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.AssistantTextField

@Composable
fun CreateAssignmentScreen(
    viewModel: CreateAssignmentViewModel = hiltViewModel(),
    courseId: Long
) {

    viewModel.updateCourseId(courseId)

    Scaffold(
        modifier =  Modifier.fillMaxSize(),
        floatingActionButton = {
            AssistantFAB(
                onClick = {
                    viewModel.createAssignment()
                },
                icon = Icons.Rounded.Add,
                text = "Save"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Assignment Name")

            AssistantTextField(
                inputValue = viewModel.getAssignmentName(),
                onInputChange = { value ->
                    viewModel.updateAssignmentName(value)
                }
            )

            Text(text = "Resource Link")

            AssistantTextField(
                inputValue = viewModel.getAssignmentResourceLink() ,
                onInputChange = { value ->
                    viewModel.updateAssignmentResourceLink(value)
                }
            )

            Text(text = "Due Date")

        }
    }
}

@Preview
@Composable
fun CreateAssignmentScreenPreview() {
    CreateAssignmentScreen(courseId = 1)
}