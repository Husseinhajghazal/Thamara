package com.dev_bayan_ibrahim.flashcards.ui.app.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog


@Composable
fun UserRankChangeDialog(
    modifier: Modifier = Modifier,
    rank: UserRank
) {
    var lastVisibleRank by rememberSaveable {
        mutableIntStateOf(rank.rank)
    }
    var show by remember {
        mutableStateOf(false)
    }
    var rise by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = rank) {
        if (rank.rank != lastVisibleRank) {
            rise = rank.rank - lastVisibleRank > 0
            lastVisibleRank = rank.rank
            show = true
        }
    }
    FlashDialog(
        modifier = Modifier
            .height(300.dp)
            .aspectRatio(cardRatio),
        show = show,
        accent = if (rise) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        },
        onDismiss = { show = false }
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if (rise) "New Rank!" else "Rank Drop",
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(
                    id = if (rise) {
                        R.drawable.celebration
                    } else {
                        R.drawable.sad
                    }
                ),
                contentDescription = null
            )
            Text(
                text = "rank ${rank.rank} (${rank.expPercent}%)",
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (rise) "Congrats you reached a higher level!" else "No problem, try again when you are ready!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            OutlinedButton(
                onClick = { show = false }
            ) {
                Text(text = "Close")
            }
        }
    }
}