package com.arvind.assistant.screens.createAssignment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.arvind.assistant.db.AssignmentDetails
import com.arvind.assistant.db.DBOps
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateAssignmentViewModel @Inject constructor(
    private val dbOps: DBOps
):ViewModel() {



    var assignmentName = mutableStateOf("")
        private set
    var assignmentResourceLink = mutableStateOf("")
        private set
    var dueDate = mutableStateOf(LocalDateTime.now())
        private set
    var courseId = 0L
        private set

    fun createAssignment(){
        dbOps.createAssignment(
            AssignmentDetails(
                assignmentName = assignmentName.value,
                assignmentResourceLink = assignmentResourceLink.value,
                dueDate = dueDate.value,
                courseId = courseId,
                assignmentId = 1
            )
        )
    }

    fun updateAssignmentName(assignmentName: String){
        this.assignmentName.value = assignmentName
    }

    fun updateAssignmentResourceLink(assignmentResourceLink: String){
        this.assignmentResourceLink.value = assignmentResourceLink
    }

    fun updateDueDate(dueDate: LocalDateTime){
        this.dueDate.value = dueDate
    }

    fun getAssignmentName(): String {
        return assignmentName.value
    }

    fun getAssignmentResourceLink(): String {
        return assignmentResourceLink.value
    }

    fun getDueDate(): LocalDateTime {
        return dueDate.value
    }

    fun updateCourseId(courseId: Long){
        this.courseId = courseId
    }


}