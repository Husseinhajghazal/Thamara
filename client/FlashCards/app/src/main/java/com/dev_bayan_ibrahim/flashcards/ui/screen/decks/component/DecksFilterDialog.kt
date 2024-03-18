package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.data.util.FlashSelectableItem
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDropDown
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashRangeSlider
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toFloatRange
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.collections.immutable.PersistentSet

@Stable
interface DecksFilterUiState {
    val show: Boolean
    val groupType: DecksGroupType?
    val orderType: DecksOrderType?
    val ascOrder: Boolean
    val filterType: DecksFilter
    val allTags: List<String>

    val levelsRange: IntRange
    val selectedLevelsRange: IntRange

    val selectedRateRange: ClosedFloatingPointRange<Float>
}

class DecksFilterMutableUiState : DecksFilterUiState {
    fun onApply(appliedFilters: DecksFilterMutableUiState) {
        groupType = appliedFilters.groupType
        orderType = appliedFilters.orderType
        filterType = appliedFilters.filterType
        allTags.clear(); allTags.addAll(appliedFilters.allTags)
        levelsRange = appliedFilters.levelsRange
        selectedLevelsRange = appliedFilters.selectedLevelsRange
        selectedRateRange = appliedFilters.selectedRateRange
    }

    override var show: Boolean by mutableStateOf(false)
    override var groupType: DecksGroupType? by mutableStateOf(null)
    override var orderType: DecksOrderType? by mutableStateOf(null)
    override var ascOrder: Boolean by mutableStateOf(true)
    override var filterType: DecksFilter by mutableStateOf(DecksFilter())
    override val allTags: SnapshotStateList<String> = mutableStateListOf()

    override var levelsRange: IntRange by mutableStateOf(1..10)
    override var selectedLevelsRange: IntRange by mutableStateOf(levelsRange)

    override var selectedRateRange: ClosedFloatingPointRange<Float> by mutableStateOf(0f..5f)
}

@Immutable
interface DecksFilterDialogUiActions {
    fun onSelectGroupType(groupType: DecksGroupType)
    fun onSelectOrderType(orderType: DecksOrderType)
    fun onSelectTag(tag: String)
    fun onDeselectAll()
    fun onApply()
    fun onCancel()
    fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>)
    fun onRateValueChange(rates: ClosedFloatingPointRange<Float>)
    fun onShowDialog()
    fun changeOrderType(asc: Boolean)
}

@Composable
fun DecksFilterDialog(
    modifier: Modifier = Modifier,
    state: DecksFilterUiState,
    actions: DecksFilterDialogUiActions

) {
    FlashDialog(
        modifier = modifier,
        show = state.show,
        onDismiss = actions::onCancel
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterSection(title = "Group By") {
                FilterDialogItem(
                    items = DecksGroupType.entries,
                    selectedItem = state.groupType ?: DecksGroupType.NONE,
                    onSelectItem = actions::onSelectGroupType,
                )
            }

            FilterSection(title = "Order By") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterDialogItem(
                        items = DecksOrderType.entries,
                        selectedItem = state.orderType ?: DecksOrderType.NAME,
                        onSelectItem = actions::onSelectOrderType,
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable {
                                actions.changeOrderType(!state.ascOrder)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = state.ascOrder,
                            onCheckedChange = null
                        )
                        Text("asc order")
                    }
                }
            }

            FilterSection(title = "Filter By") {
                FilterTags(
                    allTags = state.allTags,
                    selectedTags = state.filterType.tags,
                    onSelectTag = actions::onSelectTag,
                    onDeselectAll = actions::onDeselectAll,
                )
                FilterRangeSlider(
                    label = "Levels",
                    validRange = state.levelsRange.toFloatRange(),
                    selectedRange = state.selectedLevelsRange.toFloatRange(),
                    onValueChange = actions::onLevelsValueChange
                )

                FilterRangeSlider(
                    label = "Rate",
                    validRange = 0f..5f,
                    selectedRange = state.selectedRateRange,
                    onValueChange = actions::onRateValueChange
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = actions::onCancel) {
                    Text("Cancel")
                }
                TextButton(onClick = actions::onApply) {
                    Text("Apply")
                }
            }
        }
    }
}


@Composable
private fun FilterSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun <T : FlashSelectableItem> FilterDialogItem(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onSelectItem: (T) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 0f else -90f,
        label = ""
    )
    Box(
        modifier = modifier,
    ) {
        AssistChip(
            onClick = { expanded = true },
            label = {
                Text(text = stringResource(id = selectedItem.label))
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotation
                    },
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        FlashDropDown(
            expanded = expanded,
            items = items,
            selectedItem = selectedItem,
            onSelectItem = {
                onSelectItem(it)
                expanded = false
            },
            onDismiss = { expanded = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterTags(
    modifier: Modifier = Modifier,
    allTags: List<String>,
    selectedTags: PersistentSet<String>,
    onSelectTag: (String) -> Unit,
    onDeselectAll: () -> Unit,
) {
    if (allTags.isNotEmpty()) {
        Column(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tags")
                TextButton(onClick = onDeselectAll) {
                    Text(text = "Deselect")
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allTags.forEach { tag ->
                    FilterChip(
                        selected = tag in selectedTags,
                        onClick = { onSelectTag(tag) },
                        label = {
                            Text(text = tag)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRangeSlider(
    modifier: Modifier = Modifier,
    label: String,
    validRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    selectedRange: ClosedFloatingPointRange<Float>,
) {
    Column(
        modifier = Modifier,
    ) {
        Text(label)
        FlashRangeSlider(
            modifier = modifier,
            steps = validRange.run { endInclusive - start - 1 }.toInt(),
            onValueChange = onValueChange,
            selectedRange = selectedRange,
            valueRange = validRange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DecksFilterDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            DecksFilterDialog(
                state = DecksFilterMutableUiState(),
                actions = object : DecksFilterDialogUiActions {
                    override fun onSelectGroupType(groupType: DecksGroupType) {}

                    override fun onSelectOrderType(orderType: DecksOrderType) {}

                    override fun onSelectTag(tag: String) {}

                    override fun onDeselectAll() {}

                    override fun onApply() {}

                    override fun onCancel() {}

                    override fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>) {}

                    override fun onRateValueChange(rates: ClosedFloatingPointRange<Float>) {}

                    override fun onShowDialog() {}

                    override fun changeOrderType(asc: Boolean) {}

                }
            )
        }
    }
}
