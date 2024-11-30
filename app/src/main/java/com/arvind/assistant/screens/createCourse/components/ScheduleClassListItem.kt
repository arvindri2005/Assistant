package com.arvind.assistant.screens.createCourse.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.utils.timeFormatter
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleClassListItem(
    item: ClassScheduleDetails,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    popupContent: (@Composable () -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(modifier),
        shape = RoundedCornerShape(25),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = item.dayOfWeek.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(
                        id = R.string.time_range,
                        item.startTime.format(timeFormatter),
                        item.endTime.format(timeFormatter)
                    )
                )

                if (onCloseClick != null) {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription =null
                        )
                    }
                } else {
                    Box(Modifier.height(48.dp)) {}
                }
                if (popupContent != null) {
                    popupContent()
                }
            }

        }

        if(!item.includedInSchedule){
            Surface(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(25),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Exclude From Schedule",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun ScheduleClassListItemPreview() {
    ScheduleClassListItem(
        item = ClassScheduleDetails(
            dayOfWeek = DayOfWeek.MONDAY,
            startTime = LocalTime.now(),
            endTime = LocalTime.now(),
            includedInSchedule = true
        ),
        onClick = {}
    )
}