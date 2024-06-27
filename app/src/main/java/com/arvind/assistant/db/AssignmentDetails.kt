package com.arvind.assistant.db

import java.time.LocalDateTime

data class AssignmentDetails(
    val assignmentId: Long,
    val courseId: Long,
    val assignmentName: String,
    val assignmentResourceLink: String,
    val dueDate: LocalDateTime
)