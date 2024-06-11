package com.arvind.assistant.createCourse

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.utils.timeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleClassListItem(
    item: ClassScheduleDetails,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    popupContent: (@Composable () -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(modifier)
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.dayOfWeek.name)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(
                    id = R.string.time_range,
                    item.startTime.format(timeFormatter),
                    item.endTime.format(timeFormatter)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
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