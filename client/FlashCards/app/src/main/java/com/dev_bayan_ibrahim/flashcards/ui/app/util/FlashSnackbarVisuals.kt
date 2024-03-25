package com.dev_bayan_ibrahim.flashcards.ui.app.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.exception.CardDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.exception.CardDownloadException
import com.dev_bayan_ibrahim.flashcards.data.exception.CardException
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckException

interface FlashSnackbarVisuals {
    fun asSnackbarVisuals(context: Context): SnackbarVisuals
}

enum class FlashSnackbarMessages(
    @StringRes
    val actionLabel: Int?,
    @StringRes
    val message: Int,
    val duration: SnackbarDuration,
    val withDismissAction: Boolean
) : FlashSnackbarVisuals {

    Success(
        actionLabel = null,
        message = R.string.success,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    FinishDownloadDeck(
        actionLabel = null,
        message = R.string.finish_download_deck,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    FailDownloadDeck(
        actionLabel = null,
        message = R.string.fail_download_deck,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    )
    ;

    override fun asSnackbarVisuals(
        context: Context
    ): SnackbarVisuals = FlashSnackbarVisualsImpl(
        actionLabel = actionLabel?.let { context.getString(it) },
        message = context.getString(message),
        duration = duration,
        withDismissAction = withDismissAction
    )

    companion object Factory {

        operator fun invoke(
            throwable: Throwable,
        ): FlashSnackbarVisuals = FlashGeneralSnackbarVisuals(
            actionLabel = { null },
            message = { context -> getThrowableMessage(context, throwable) },
            duration = SnackbarDuration.Long,
            withDismissAction = false,
        )

        operator fun invoke(
            actionLabel: (Context) -> String?,
            message: (Context) -> String,
            duration: SnackbarDuration = SnackbarDuration.Short,
            withDismissAction: Boolean = false,
        ): FlashSnackbarVisuals = FlashGeneralSnackbarVisuals(
            actionLabel = actionLabel,
            message = message,
            duration = duration,
            withDismissAction = withDismissAction
        )
    }
}

private data class FlashGeneralSnackbarVisuals(
    private val actionLabel: (Context) -> String?,
    private val message: (Context) -> String,
    private val duration: SnackbarDuration,
    private val withDismissAction: Boolean,
) : FlashSnackbarVisuals {
    override fun asSnackbarVisuals(context: Context): SnackbarVisuals = FlashSnackbarVisualsImpl(
        actionLabel = actionLabel(context),
        message = message(context),
        duration = duration,
        withDismissAction = withDismissAction
    )
}

private data class FlashSnackbarVisualsImpl(
    override val actionLabel: String?,
    override val message: String,
    override val duration: SnackbarDuration,
    override val withDismissAction: Boolean
) : SnackbarVisuals



private fun getThrowableMessage(
    context: Context,
    throwable: Throwable
): String = when (throwable) {
    is CardDeserializationException -> {
        "invalid cards data ${throwable.message}"
    }
    is CardDownloadException -> {
        "Download Error ${getCodeMessage(context, throwable.code.value)}"
    }
    is CardException -> {
        "Problem in card data ${throwable.message}"
    }
    is DeckDeserializationException -> {
        "invalid deck ${throwable.message}"
    }
    is DeckException -> {
        "Problem in deck data ${throwable.message}"
    }

//    else -> context.getString(R.string.unknown_error)
    else -> throwable.message ?: context.getString(R.string.unknown_error)
}
private fun getCodeMessage(
    context: Context,
    code: Int
): String = "error code $code"