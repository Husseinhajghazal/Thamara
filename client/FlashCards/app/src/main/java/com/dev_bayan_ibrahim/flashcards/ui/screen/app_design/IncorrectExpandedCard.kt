package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio

@Composable
fun IncorrectExpandedCard(
    modifier: Modifier = Modifier,
    card: Card,
    accent: Color,
    bgPattern: String,
    incorrectAnswer: String,
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
                incorrectAnswer = incorrectAnswer
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
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 3,
    )
}
@Composable
private fun CardAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer,
    incorrectAnswer: String,
) {
    when (answer) {
        is CardAnswer.Info -> {}

        is CardAnswer.MultiChoice -> CardMultiChoiceAnswer(
            modifier = modifier,
            answer = answer,
            incorrectAnswer = incorrectAnswer,
        )

        is CardAnswer.TrueFalse -> CardTrueFalseAnswer(
            modifier = modifier,
            answer = answer,
        )

        is CardAnswer.Write -> CardWriteAnswer(
            modifier = modifier,
            answer = answer,
            incorrectAnswer = incorrectAnswer
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardMultiChoiceAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.MultiChoice,
    incorrectAnswer: String,
) {
    require(answer.choices.count() in 2..5)
    val errorColor: Color = MaterialTheme.colorScheme.error
    val correctColor: Color = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        answer.choices.mapNotNull {
            if (it == answer.correctChoice || it == incorrectAnswer) {
                it to (it == answer.correctChoice)
            } else null
        }.forEach { (choice, correct) ->
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = if (correct) correctColor else errorColor,
                            style = if (correct) Fill else Stroke(1.dp.toPx()),
                            cornerRadius = CornerRadius(32.dp.toPx())
                        )
                    }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately
                    ),
                    text = choice,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        if (correct) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
@Composable
private fun CardTrueFalseAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.TrueFalse,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        FilterChip(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            enabled = false,
            label = {
                Text(text = "False")
            },
            onClick = {},
            selected = !answer.answer
        )
        FilterChip(
            modifier = Modifier
                .widthIn(max = 150.dp)
                .weight(1f),
            enabled = false,
            label = {
                Text(text = "True")
            },
            onClick = {},
            selected = !answer.answer,
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardWriteAnswer(
    modifier: Modifier = Modifier,
    answer: CardAnswer.Write,
    incorrectAnswer: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.Immediately
            ),
            text = incorrectAnswer.ifBlank { "\"Blank Text\"" },
            color = MaterialTheme.colorScheme.error,
            maxLines = 1,
            textDecoration = TextDecoration.LineThrough
        )
        Text(
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.WhileFocused
            ),
            text = answer.answer,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
        )
    }
}
