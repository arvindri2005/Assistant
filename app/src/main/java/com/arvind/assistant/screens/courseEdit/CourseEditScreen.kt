package com.arvind.assistant.screens.courseEdit

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseEditScreen(
    course: CourseDetails,
    onEditCourse: (courseName: String, requiredAttendance:Double ) -> Unit,
    onGoBack: () -> Unit
){
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    var newCourseName by rememberSaveable {
        mutableStateOf(course.courseName)
    }
    var newRequiredAttendancePercentage by rememberSaveable {
        mutableIntStateOf(course.requiredAttendance.toInt())
    }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = "Edit details for ${course.courseName}",
                )
            }, navigationIcon = {
                IconButton(onClick = onGoBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription =null
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onClickSave@{
                    if (newCourseName.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Course name cannot be empty",
                                withDismissAction = true
                            )
                        }
                        return@onClickSave
                    }
                    onEditCourse(newCourseName, newRequiredAttendancePercentage.toDouble())
                    Toast.makeText(context, "Course Updated", Toast.LENGTH_SHORT).show()
                    onGoBack()
                },
                modifier = Modifier.imePadding()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = null
                )
                Text(text = "Save")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = newCourseName,
                onValueChange = { newCourseName = it },
                maxLines = 1,
                trailingIcon = {
                    if (newCourseName.isNotBlank()) {
                        IconButton(onClick = { newCourseName = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Course Name")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Required Attendance Percentage: $newRequiredAttendancePercentage",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = newRequiredAttendancePercentage.toFloat(),
                onValueChange = { newRequiredAttendancePercentage = it.toInt() },
                steps = 100,
                valueRange = 1f..100f
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}