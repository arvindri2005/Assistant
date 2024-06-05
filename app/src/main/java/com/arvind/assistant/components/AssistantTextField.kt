package com.arvind.assistant.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

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

    TextField(
        modifier = modifier,
        value = inputValue,
        onValueChange = onInputChange,
        label = label,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
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