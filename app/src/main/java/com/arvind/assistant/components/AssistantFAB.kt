package com.arvind.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AssistantFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String

) {

    FloatingActionButton(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ){
            Icon(imageVector = icon, contentDescription = null )
            Text(text = text)
        }

    }

}

@Preview
@Composable
fun AssistantFABPreview() {
    AssistantFAB(
        onClick = {},
        icon = Icons.Filled.Add,
        text = "Add"
    )
}