package com.dev_bayan_ibrahim.flashcards.ui.screen.home.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.BasicTextFieldBox
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashSlider
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlin.math.roundToInt

@Composable
fun HomeUserDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    name: String,
    age: Int,
    onNameChange: (String) -> Unit,
    onAgeChange: (Int) -> Unit,
    onSave: () -> Unit,
) {
    FlashDialog(
        modifier = modifier,
        show = show,
        onDismiss = {}
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Welcome New User",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.alpha(0.75f),
                text = "enter your name and age and start playing",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            UserName(
                name = name,
                onNameChange = onNameChange
            )
            UserAge(
                age = age,
                onAgeChange = onAgeChange,
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = onSave,
                enabled = name.isNotBlank()
            ) {
                Text("Start")
            }
        }
    }
}

@Composable
private fun UserName(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .width(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextFieldBox(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            placeHolder = "Name",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UserAge(
    modifier: Modifier = Modifier,
    age: Int,
    validRange: ClosedFloatingPointRange<Float> = 3f..60f,
    onAgeChange: (Int) -> Unit,
) {
    FlashSlider(
        modifier = modifier,
        value = age.toFloat(),
        steps = validRange.run { endInclusive - start }.roundToInt(),
        onValueChange = { onAgeChange(it.roundToInt()) },
        onValueChangeFinished = { /*TODO*/ },
        valueRange = validRange,
        readonly = false,
    )

}

@Preview(showBackground = true)
@Composable
private fun HomeUserDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val user = User(name = "", age = 0, rank = 0)
            HomeUserDialog(
                show = true,
                name = user.name,
                age = user.age,
                onAgeChange = {},
                onNameChange = {},
                onSave = {}
            )
        }
    }
}
