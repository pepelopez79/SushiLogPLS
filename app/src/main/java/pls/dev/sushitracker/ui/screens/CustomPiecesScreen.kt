package pls.dev.sushitracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushitracker.data.AppSettingsManager
import pls.dev.sushitracker.data.AppStrings
import pls.dev.sushitracker.data.CustomPiece
import pls.dev.sushitracker.ui.theme.SushiColors
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomPiecesScreen(
    colors: SushiColors,
    strings: AppStrings.Strings,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { AppSettingsManager(context) }
    var pieces by remember { mutableStateOf(settingsManager.getCustomPieces()) }

    var editingPiece by remember { mutableStateOf<CustomPiece?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }

    var newName by remember { mutableStateOf("") }
    var newEmoji by remember { mutableStateOf("🍣") }
    var newKcal by remember { mutableIntStateOf(0) }
    var newSalmonCount by remember { mutableIntStateOf(0) }
    var newRiceGrams by remember { mutableIntStateOf(0) }
    var showNameError by remember { mutableStateOf(false) }
    var showDuplicateError by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<CustomPiece?>(null) }

    val emojiOptions = remember {
        listOf("🍣", "🍱", "🐟", "🍙", "🥟", "🍜", "🥗", "🍤", "🍢", "🍘", "🍵",
               "🫔", "🥣", "🥡", "🫛", "🍚", "🥢", "🍲", "🍛", "🍶", "🍡")
            .filter { android.graphics.Paint().hasGlyph(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.secondary)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = strings.back,
                    tint = colors.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = strings.customPiecesManage,
                    color = colors.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${pieces.size}/12 • ${strings.customPiecesSubtitle}",
                    color = colors.mutedForeground,
                    fontSize = 13.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = !isAddingNew,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Button(
                        onClick = {
                            isAddingNew = true
                            editingPiece = null
                            newName = ""
                            newEmoji = "🍣"
                            newKcal = 0
                            newSalmonCount = 0
                            newRiceGrams = 0
                            showNameError = false
                            showDuplicateError = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(strings.addCustomPiece, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }

                AnimatedVisibility(
                    visible = isAddingNew,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = if (isAddingNew) 0.dp else 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        CustomPieceForm(
                            colors = colors,
                            strings = strings,
                            emojiOptions = emojiOptions,
                            newName = newName,
                            newEmoji = newEmoji,
                            newKcal = newKcal,
                            newSalmonCount = newSalmonCount,
                            newRiceGrams = newRiceGrams,
                            showNameError = showNameError,
                            showDuplicateError = showDuplicateError,
                            isEditing = false,
                            onNameChange = {
                                newName = it
                                if (it.isNotBlank()) showNameError = false
                                showDuplicateError = false
                            },
                            onEmojiChange = { newEmoji = it },
                            onKcalChange = { newKcal = it },
                            onSalmonChange = { newSalmonCount = it },
                            onRiceChange = { newRiceGrams = it },
                            onCancel = { isAddingNew = false },
                            onSave = {
                                val isDuplicate = pieces.any { it.name.trim().equals(newName.trim(), ignoreCase = true) }
                                if (newName.isBlank()) {
                                    showNameError = true
                                } else if (isDuplicate) {
                                    showDuplicateError = true
                                } else {
                                    settingsManager.addCustomPiece(CustomPiece(
                                        id = "custom_${UUID.randomUUID()}",
                                        name = newName.trim(),
                                        emoji = newEmoji,
                                        kcal = newKcal,
                                        salmonCount = newSalmonCount,
                                        riceGrams = newRiceGrams
                                    ))
                                    pieces = settingsManager.getCustomPieces()
                                    isAddingNew = false
                                }
                            },
                            limitReached = pieces.size >= 12
                        )
                    }
                }
            }

            if (pieces.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                        Text(strings.customPiecesEmpty, color = colors.mutedForeground, fontSize = 14.sp)
                    }
                }
            }

            items(pieces) { piece ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (editingPiece?.id == piece.id) {
                                        editingPiece = null
                                    } else {
                                        isAddingNew = false
                                        editingPiece = piece
                                        newName = piece.name
                                        newEmoji = piece.emoji
                                        newKcal = piece.kcal
                                        newSalmonCount = piece.salmonCount
                                        newRiceGrams = piece.riceGrams
                                        showNameError = false
                                        showDuplicateError = false
                                    }
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(colors.secondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(piece.emoji, fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = piece.name,
                                    color = colors.onSurface,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    CustomPieceDataChip(icon = "🔥", text = "${piece.kcal}", colors = colors)
                                    CustomPieceDataChip(icon = "🐟", text = "${piece.salmonCount}", colors = colors)
                                    CustomPieceDataChip(icon = "🍚", text = "${piece.riceGrams}g", colors = colors)
                                }
                            }
                        }


                        AnimatedVisibility(
                            visible = editingPiece?.id == piece.id,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Box(modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                                CustomPieceForm(
                                    colors = colors,
                                    strings = strings,
                                    emojiOptions = emojiOptions,
                                    newName = newName,
                                    newEmoji = newEmoji,
                                    newKcal = newKcal,
                                    newSalmonCount = newSalmonCount,
                                    newRiceGrams = newRiceGrams,
                                    showNameError = showNameError,
                                    showDuplicateError = showDuplicateError,
                                    isEditing = true,
                                    onNameChange = {
                                        newName = it
                                        if (it.isNotBlank()) showNameError = false
                                        showDuplicateError = false
                                    },
                                    onEmojiChange = { newEmoji = it },
                                    onKcalChange = { newKcal = it },
                                    onSalmonChange = { newSalmonCount = it },
                                    onRiceChange = { newRiceGrams = it },
                                    onCancel = { editingPiece = null },
                                    onDelete = { deleteTarget = piece },
                                    onSave = {
                                        val isDuplicate = pieces.any { it.name.trim().equals(newName.trim(), ignoreCase = true) && it.id != editingPiece?.id }
                                        if (newName.isBlank()) {
                                            showNameError = true
                                        } else if (isDuplicate) {
                                            showDuplicateError = true
                                        } else {
                                            settingsManager.updateCustomPiece(editingPiece!!.copy(
                                                name = newName.trim(),
                                                emoji = newEmoji,
                                                kcal = newKcal,
                                                salmonCount = newSalmonCount,
                                                riceGrams = newRiceGrams
                                            ))
                                            pieces = settingsManager.getCustomPieces()
                                            editingPiece = null
                                        }
                                    },
                                    limitReached = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (deleteTarget != null) {
        val piece = deleteTarget!!
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor = colors.surface,
            title = { Text(strings.delete, color = colors.onSurface, fontWeight = FontWeight.Bold) },
            text = { Text("${strings.delete} \"${piece.name}\"?", color = colors.mutedForeground) },
            confirmButton = {
                TextButton(onClick = {
                    settingsManager.removeCustomPiece(piece.id)
                    pieces = settingsManager.getCustomPieces()
                    if (editingPiece?.id == piece.id) {
                        editingPiece = null
                    }
                    deleteTarget = null
                }) {
                    Text(strings.delete, color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text(strings.cancel, color = colors.primary)
                }
            }
        )
    }
}

@Composable
private fun CustomPieceDataChip(icon: String, text: String, colors: SushiColors) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.secondary)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(icon, fontSize = 10.sp)
        Text(text, color = colors.mutedForeground, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun NumericStepper(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    step: Int,
    colors: SushiColors,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.mutedForeground, modifier = Modifier.padding(bottom = 6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, colors.secondary, RoundedCornerShape(16.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { if (value - step >= 0) onValueChange(value - step) else onValueChange(0) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.secondary)
            ) {
                Text("-", color = colors.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            Text(text = value.toString(), color = colors.onSurface, fontWeight = FontWeight.Black, fontSize = 16.sp)
            IconButton(
                onClick = { onValueChange(value + step) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.secondary)
            ) {
                Text("+", color = colors.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CustomPieceForm(
    colors: SushiColors,
    strings: AppStrings.Strings,
    emojiOptions: List<String>,
    newName: String,
    newEmoji: String,
    newKcal: Int,
    newSalmonCount: Int,
    newRiceGrams: Int,
    showNameError: Boolean,
    showDuplicateError: Boolean,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onEmojiChange: (String) -> Unit,
    onKcalChange: (Int) -> Unit,
    onSalmonChange: (Int) -> Unit,
    onRiceChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
    limitReached: Boolean
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!isEditing) {
            Text(
                text = strings.addCustomPiece,
                color = colors.onSurface,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
            )
        }

        if (!limitReached || isEditing) {
            Column(modifier = Modifier.padding(if (isEditing) 0.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = onNameChange,
                    placeholder = { Text(strings.customPieceNameHint, color = colors.mutedForeground) },
                    isError = showNameError || showDuplicateError,
                    supportingText = if (showNameError) {
                        { Text(strings.noPieceName, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
                    } else if (showDuplicateError) {
                        { Text(strings.duplicatePieceName, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
                    } else null,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.border,
                        cursorColor = colors.primary,
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorSupportingTextColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumericStepper(
                        label = strings.kcal,
                        value = newKcal,
                        onValueChange = onKcalChange,
                        step = 10,
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                    NumericStepper(
                        label = "${strings.customPieceSalmonLabel} (${strings.customPieceSalmonHint})",
                        value = newSalmonCount,
                        onValueChange = onSalmonChange,
                        step = 1,
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumericStepper(
                        label = "${strings.rice} (g)",
                        value = newRiceGrams,
                        onValueChange = onRiceChange,
                        step = 5,
                        colors = colors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        emojiOptions.forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (newEmoji == emoji) colors.primary.copy(alpha = 0.2f) else colors.surface)
                                    .border(
                                        if (newEmoji == emoji) 2.dp else 0.dp,
                                        if (newEmoji == emoji) colors.primary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { onEmojiChange(emoji) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 18.sp)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = if (isEditing && onDelete != null) { { onDelete() } } else onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEditing) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else colors.secondary,
                            contentColor = if (isEditing) MaterialTheme.colorScheme.error else colors.onSecondary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        if (isEditing) {
                            Text(strings.delete, fontWeight = FontWeight.Bold)
                        } else {
                            Text(strings.cancel, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(if (isEditing) strings.save else strings.addCustomPiece, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.primary.copy(alpha = 0.1f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    strings.customPiecesLimit,
                    color = colors.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
