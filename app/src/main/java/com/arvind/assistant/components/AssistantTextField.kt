package com.arvind.assistant.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    onInputChange: (String)-> Unit,
    label: @Composable (()-> Unit)?= null,
    trailingIcon: @Composable (()-> Unit)?= null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = inputValue,
        onValueChange = onInputChange,
        label = label,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(25),
        colors = OutlinedTextFieldDefaults
            .colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,


            )

    )



}

@Preview
@Composable
fun AssistantTextFieldPreview() {
    val remember = rememberSaveable {
        mutableStateOf("")
    }
    AssistantTextField(
        inputValue = remember.value,
        onInputChange = {
            remember.value = it
        },
    )
}