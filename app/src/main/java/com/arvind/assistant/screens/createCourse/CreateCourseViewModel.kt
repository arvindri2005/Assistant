package com.arvind.assistant.screens.createCourse

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arvind.assistant.db.ClassScheduleDetails

class CreateCourseViewModel: ViewModel() {
    private val name = MutableLiveData<String>()

    var courseName = mutableStateOf("")
        private set
    var requiredAttendance = mutableDoubleStateOf(75.0)
        private set

    private var classesForTheCourse = mutableStateOf(listOf<ClassScheduleDetails>())


    private val showAddClassBottomSheet = mutableStateOf(false)
    private val classToUpdateIndex = mutableStateOf<Int?>(null)

    fun getClassToUpdateIndex(): Int? {
        return classToUpdateIndex.value
    }

    fun updateClassToUpdateIndex(index: Int?) {
        classToUpdateIndex.value = index
    }
    fun updateClassInfo(index: Int, classDetail: ClassScheduleDetails) {
        val updatedClasses = classesForTheCourse.value.toMutableList()
        updatedClasses[index] = classDetail
        classesForTheCourse.value = updatedClasses
    }

    fun addClassForTheCourse(classDetail: ClassScheduleDetails) {
        val updatedClasses = classesForTheCourse.value.toMutableList()
        updatedClasses.add(classDetail)
        classesForTheCourse.value = updatedClasses
    }

    fun deleteClassForTheCourse(index: Int) {
        val updatedClasses = classesForTheCourse.value.toMutableList()
        if (index >= 0 && index < updatedClasses.size) {
            updatedClasses.removeAt(index)
            classesForTheCourse.value = updatedClasses
        }
    }

    fun getClassForTheCourse(): List<ClassScheduleDetails> {
        return classesForTheCourse.value
    }

    fun getSingleClassForTheCourse(index: Int): ClassScheduleDetails {
        return classesForTheCourse.value[index]
    }

    fun getCourseName(): String {
        return courseName.value
    }

    fun getRequiredAttendance(): Double {
        return requiredAttendance.doubleValue
    }

    fun getBottomSheetState(): Boolean {
        return showAddClassBottomSheet.value
    }

    fun updateBottomSheetState(showBottomSheet: Boolean) {
        this.showAddClassBottomSheet.value = showBottomSheet
    }

    fun updateCourseName(courseName: String) {
        this.courseName.value = courseName
    }

    fun updateRequiredAttendance(requiredAttendance: Double) {
        this.requiredAttendance.doubleValue = requiredAttendance
    }







}