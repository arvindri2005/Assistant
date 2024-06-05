package com.arvind.assistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onCreateCourse: () -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                onCreateCourse()
            }
        ) {
            Text(text = "Create Course")
        }
    }
}