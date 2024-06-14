package com.arvind.assistant.components

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun AssistantBaseDialog(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    onDismissRequest: () -> Unit,
    dialogPadding: PaddingValues = BaseDialogDefaults.dialogMargins,
    contentPadding: PaddingValues = BaseDialogDefaults.contentPadding,
    minWidth: Dp = 280.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.run {
            setDimAmount(BaseDialogDefaults.dimAmount)
            setGravity(Gravity.BOTTOM)
        }
        Box(
            modifier = modifier
                .widthIn(min = minWidth)
                .padding(dialogPadding)
                .semantics { paneTitle = "Dialog" }
        ) {
            Column(
                modifier = Modifier
                    .clip(BaseDialogDefaults.shape)
                    .shadow(elevation = BaseDialogDefaults.elevation)
                    .background(
                        color = backgroundColor,
                        shape = BaseDialogDefaults.shape
                    )
                    .padding(contentPadding),
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    content()
                }
            }
        }
    }
}

object BaseDialogDefaults {

    val contentPadding = PaddingValues(
        all = 24.dp
    )

    val contentPaddingAlternative = PaddingValues(
        vertical = 24.dp, horizontal = 8.dp
    )

    val dialogMargins = PaddingValues(
        bottom = 12.dp,
        start = 12.dp,
        end = 12.dp
    )

    val shape = RoundedCornerShape(
        size = 26.dp
    )

    const val dimAmount = 0.65F

    val elevation = 1.dp

    const val animDuration = 150

}