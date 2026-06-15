package pls.dev.sushilog.ui.screens

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
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushilog.data.AppSettingsManager
import pls.dev.sushilog.data.AppStrings
import pls.dev.sushilog.data.CustomPiece
import pls.dev.sushilog.data.DRAWABLE_NAME_TO_ID
import pls.dev.sushilog.data.resolveDrawableName
import pls.dev.sushilog.ui.theme.SushiColors
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
    var newIconName by remember { mutableStateOf("nigiri") }
    var newKcal by remember { mutableIntStateOf(0) }
    var newSalmonCount by remember { mutableIntStateOf(0) }
    var newRiceGrams by remember { mutableIntStateOf(0) }
    var showNameError by remember { mutableStateOf(false) }
    var showDuplicateError by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<CustomPiece?>(null) }

    val imageOptions = remember { DRAWABLE_NAME_TO_ID.toList() }

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
                    painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.back),
                    contentDescription = strings.back,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
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
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                if (!isAddingNew) {
                    Button(
                        onClick = {
                            isAddingNew = true
                            editingPiece = null
                            newName = ""
                            newIconName = "nigiri"
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
                    }
                }

                if (isAddingNew) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = if (isAddingNew) 0.dp else 16.dp).shadow(4.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        CustomPieceForm(
                            colors = colors,
                            strings = strings,
                            imageOptions = imageOptions,
                            newName = newName,
                            newIconName = newIconName,
                            newKcal = newKcal,
                            newSalmonCount = newSalmonCount,
                            newRiceGrams = newRiceGrams,
                            showNameError = showNameError,
                            showDuplicateError = showDuplicateError,
                            isEditing = false,
                            onNameChange = {
                                val capitalized = it
                                    .take(AppSettingsManager.MAX_CUSTOM_PIECE_NAME_LENGTH)
                                    .replaceFirstChar { c -> c.uppercase() }
                                newName = capitalized
                                if (capitalized.isNotBlank()) showNameError = false
                                showDuplicateError = false
                            },
                            onIconNameChange = { newIconName = it },
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
                                    val trimmedName = newName.trim()
                                    settingsManager.addCustomPiece(CustomPiece(
                                        id = "custom_${UUID.randomUUID()}",
                                        name = trimmedName,
                                        iconName = newIconName,
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
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
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
                                        newIconName = piece.iconName
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
                            Icon(painter = androidx.compose.ui.res.painterResource(id = piece.iconId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(40.dp))

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
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    CustomPieceDataChip(icon = pls.dev.sushilog.R.drawable.kcal, text = strings.historyKcalLabel.format(piece.kcal), colors = colors)
                                    if (piece.riceGrams > 0) {
                                        CustomPieceDataChip(icon = pls.dev.sushilog.R.drawable.rice, text = strings.historyRiceLabel.format(piece.riceGrams), colors = colors)
                                    }
                                    if (piece.salmonCount > 0) {
                                        CustomPieceDataChip(icon = pls.dev.sushilog.R.drawable.salmon, text = if (piece.salmonCount == 1) strings.historySalmonLabelSingular.format(piece.salmonCount) else strings.historySalmonLabel.format(piece.salmonCount), colors = colors)
                                    }
                                }
                            }
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(id = if (editingPiece?.id == piece.id) pls.dev.sushilog.R.drawable.up else pls.dev.sushilog.R.drawable.down),
                                contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.padding(end = 4.dp).size(24.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = editingPiece?.id == piece.id,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = colors.border)
                                Box(modifier = Modifier.padding(16.dp)) {
                                    CustomPieceForm(
                                        colors = colors,
                                        strings = strings,
                                        imageOptions = imageOptions,
                                        newName = newName,
                                        newIconName = newIconName,
                                        newKcal = newKcal,
                                        newSalmonCount = newSalmonCount,
                                        newRiceGrams = newRiceGrams,
                                        showNameError = showNameError,
                                        showDuplicateError = showDuplicateError,
                                        isEditing = true,
                                        onNameChange = {
                                            val capitalized = it
                                                .take(AppSettingsManager.MAX_CUSTOM_PIECE_NAME_LENGTH)
                                                .replaceFirstChar { c -> c.uppercase() }
                                            newName = capitalized
                                            if (capitalized.isNotBlank()) showNameError = false
                                            showDuplicateError = false
                                        },
                                        onIconNameChange = { newIconName = it },
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
                                                val trimmedName = newName.trim()
                                                settingsManager.updateCustomPiece(editingPiece!!.copy(
                                                    name = trimmedName,
                                                    iconName = newIconName,
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
    }

    if (deleteTarget != null) {
        val piece = deleteTarget!!
        AlertDialog(
            modifier = Modifier.shadow(8.dp, RoundedCornerShape(24.dp)),
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
private fun CustomPieceDataChip(icon: Int, text: String, colors: SushiColors) {
    Row(
        modifier = Modifier.padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(painter = androidx.compose.ui.res.painterResource(id = icon), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))
        Text(text, color = colors.mutedForeground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
    imageOptions: List<Pair<String, Int>>,
    newName: String,
    newIconName: String,
    newKcal: Int,
    newSalmonCount: Int,
    newRiceGrams: Int,
    showNameError: Boolean,
    showDuplicateError: Boolean,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onIconNameChange: (String) -> Unit,
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
                        imageOptions.forEach { (drawableName, drawableId) ->
                            val isSelected = newIconName == drawableName
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) colors.primary.copy(alpha = 0.2f) else colors.surface)
                                    .border(
                                        if (isSelected) 2.dp else 0.dp,
                                        if (isSelected) colors.primary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { onIconNameChange(drawableName) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(painter = androidx.compose.ui.res.painterResource(id = drawableId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = if (isEditing && onDelete != null) { { onDelete() } } else onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEditing) MaterialTheme.colorScheme.error else colors.secondary,
                            contentColor = if (isEditing) MaterialTheme.colorScheme.onError else colors.onSecondary
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
