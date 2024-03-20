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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
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
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        minLines = 1,
        maxLines = 2,
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
        listOf(
            answer.correctChoice,
            incorrectAnswer
        ).forEachIndexed { i, choice ->
            val correct = i == 0
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
    val errorColor: Color = MaterialTheme.colorScheme.error
    val correctColor: Color = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .drawBehind {
                    drawRoundRect(
                        color = if (!answer.answer) correctColor else errorColor,
                        style = if (!answer.answer) Fill else Stroke(1.dp.toPx()),
                        cornerRadius = CornerRadius(32.dp.toPx())
                    )
                },
            text = stringResource(id = R.string._false),
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .drawBehind {
                    drawRoundRect(
                        color = if (answer.answer) correctColor else errorColor,
                        style = if (answer.answer) Fill else Stroke(1.dp.toPx()),
                        cornerRadius = CornerRadius(32.dp.toPx())
                    )
                },
            text = stringResource(id = R.string._true),
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
            text = incorrectAnswer.ifBlank { stringResource(R.string.blank_text) },
            color = MaterialTheme.colorScheme.error,
            maxLines = 1,
            textDecoration = TextDecoration.LineThrough,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.Immediately
            ),
            text = answer.answer,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
