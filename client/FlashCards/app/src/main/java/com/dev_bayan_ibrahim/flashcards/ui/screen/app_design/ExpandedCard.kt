package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExpandedCard(
    modifier: Modifier = Modifier,
    card: Card,
    accent: Color,
    bgPattern: String,
    onSelectAnswer: (String) -> Unit,
) {
    DeckCard(
        modifier = modifier
            .aspectRatio(cardRatio)
            .fillMaxSize()
            .padding(32.dp),
        accent = accent,
        enableClick = false
    ) {
        DynamicAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.25f),
            imageUrl = bgPattern
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardQuestion(question = card.question)
            CardImage(
                modifier = Modifier.weight(1f),
                url = card.image,
            )
            CardAnswer(
                answer = card.answer,
                onSelectAnswer = onSelectAnswer
            )
        }
    }

}

@Composable
private fun CardQuestion(
    modifier: Modifier = Modifier,
    question: String,
) {
    DynamicText(
        modifier = modifier,
        text = question,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 3,
    )
}

@Composable
private fun CardImage(
    modifier: Modifier = Modifier,
    url: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        DynamicAsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .then(modifier),
            imageUrl = url,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun CardAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer,
    onSelectAnswer: (String) -> Unit
) {
    when (answer) {
        is CardAnswer.Info -> CardInfoAnswer(
            modifier = modifier,
            answer = answer,
            onClick = { onSelectAnswer("") }
        )

        is CardAnswer.MultiChoice -> CardMultiChoiceAnswer(
            modifier = modifier,
            answer = answer,
            onSelectAnswer = onSelectAnswer,
        )

        is CardAnswer.TrueFalse -> CardTrueFalseAnswer(
            modifier = modifier,
            answer = answer,
            onSelectAnswer = onSelectAnswer
        )

        is CardAnswer.Write -> CardWriteAnswer(
            modifier = modifier,
            answer = answer,
            onSelectAnswer = onSelectAnswer
        )
    }
}

@Composable
private fun CardInfoAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.Info,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Enable reminder")
        OutlinedButton(onClick = onClick) {
            Text(answer.repeatRate.toIsoString())
        }
    }
}

@Composable
private fun CardMultiChoiceAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.MultiChoice,
    chipBorder: Color = MaterialTheme.colorScheme.onSurface,
    onSelectAnswer: (String) -> Unit,
) {
    require(answer.choices.count() in 2..5)
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        items(answer.choices) { choice ->
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable { onSelectAnswer(choice) }
                    .drawBehind {
                        drawRoundRect(
                            color = chipBorder,
                            style = Stroke(1.dp.toPx()),
                            cornerRadius = CornerRadius(32.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                DynamicText(
                    text = choice,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun CardTrueFalseAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.TrueFalse,
    onSelectAnswer: (String) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            onClick = { onSelectAnswer(false.toString()) },
        ) {
            Text(text = "False")
        }
        OutlinedButton(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            onClick = { onSelectAnswer(true.toString()) },
        ) {
            Text(text = "True")
        }
    }
}

@Composable
private fun CardWriteAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.Write,
    onSelectAnswer: (String) -> Unit
) {
    var answerValue by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(16.dp),
            value = answerValue,
            onValueChange = {
                answerValue = it
            },
            label = {
                Text(text = "answer")
            },
            placeholder = {
                Text(text = "enter your answer")
            },
            maxLines = 2,
        )
        OutlinedButton(
            onClick = { onSelectAnswer(answerValue) }
        ) {
            Text(text = "Answer")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedCardPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {

            ExpandedCard(
                card = Card(
                    question = "this is a cat",
                    image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                    answer = CardAnswer.Write(
                        "correct answer"
                    )
                ),
                accent = Color.Magenta,
                bgPattern = "https://drive.google.com/uc?export=download&id=1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
                onSelectAnswer = {

                }
            )
        }
    }
}
