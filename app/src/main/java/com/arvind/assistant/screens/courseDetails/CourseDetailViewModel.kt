package com.arvind.assistant.screens.courseDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arvind.assistant.db.DBOps
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val dbOps: DBOps
): ViewModel() {
    fun getAssignments(courseId: Long) = dbOps.getAssignmentsForCourse(courseId)
}