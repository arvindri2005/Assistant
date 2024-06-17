package com.arvind.assistant.screens.myCourses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.CourseDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesListItem(
    modifier: Modifier = Modifier,
    course: CourseDetails,
    onClick: () -> Unit
) {
    OutlinedCard(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Current: ${course.currentAttendancePercentage}% Goal: ${course.requiredAttendance.toInt()}%",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                var commonMaxWidth by remember {
                    mutableIntStateOf(0)
                }
                val density = LocalDensity.current
                LaunchedEffect(density) {
                    commonMaxWidth =
                        maxOf(commonMaxWidth, with(density) { 60.dp.toPx().toInt() })
                }
                val sizeModifier = Modifier
                    .onSizeChanged {
                        commonMaxWidth = maxOf(commonMaxWidth, it.width)
                    }
                    .widthIn(min = with(density) { commonMaxWidth.toDp() })

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = sizeModifier
                        .background(
                            shape = RoundedCornerShape(25),
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(text = "P", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(text = course.presents.toString())
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = sizeModifier
                        .background(
                            shape = RoundedCornerShape(25),
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(text = "A", color = MaterialTheme.colorScheme.onErrorContainer)
                    Text(text = course.absents.toString())
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = sizeModifier
                        .background(
                            shape = RoundedCornerShape(25),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(text = "C", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = course.cancels.toString())
                }
            }
            Spacer(modifier = Modifier.padding(top = 4.dp))

        }
    }
}