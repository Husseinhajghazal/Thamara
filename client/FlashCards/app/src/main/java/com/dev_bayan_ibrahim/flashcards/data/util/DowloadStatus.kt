package com.dev_bayan_ibrahim.flashcards.data.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface DownloadStatus {
    val error: Pair<Int?, String?>?
    val success: Boolean
    val progress: Long?
    val total: Long
    val downloadSpeed: Long?
}

class MutableDownloadStatus : DownloadStatus {
    override var error: Pair<Int?, String?>? by mutableStateOf(null)
    override var success: Boolean by mutableStateOf(false)
    override var progress: Long? by mutableStateOf(null)
    override var total: Long by mutableLongStateOf(0)
    override var downloadSpeed: Long? by mutableStateOf(null)
}