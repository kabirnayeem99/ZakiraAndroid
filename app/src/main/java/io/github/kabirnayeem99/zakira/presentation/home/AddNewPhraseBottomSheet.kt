package io.github.kabirnayeem99.zakira.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPhraseBottomSheet(
    sheetState: SheetState,
    visible: Boolean = false,
    onAddClick: (arabic: String, meaning: String, category: String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (visible) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = RoundedCornerShape(8.dp),
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = true),
            onDismissRequest = onDismiss,
            content = {

                val focusManager = LocalFocusManager.current
                val arabic = remember { mutableStateOf("") }
                val meaning = remember { mutableStateOf("") }
                val category = remember { mutableStateOf("") }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {

                    item {
                        OutlinedTextField(
                            value = arabic.value,
                            onValueChange = { ar -> arabic.value = ar },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.labelLarge.copy(
                                textDirection = TextDirection.Rtl,
                                fontSize = 18.sp,
                            ),
                            keyboardOptions = KeyboardOptions(
                                hintLocales = LocaleList("ar"),
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                            minLines = 4,
                            maxLines = 6,
                            isError = arabic.value.isBlank(),
                            label = {
                                Text(
                                    "عبارة عربية",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 18.sp,
                                    ),
                                )
                            },
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = meaning.value,
                            onValueChange = { ar -> meaning.value = ar },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.labelLarge.copy(
                                textDirection = TextDirection.Ltr,
                                fontSize = 14.sp,
                            ),
                            keyboardOptions = KeyboardOptions(
                                hintLocales = LocaleList("en-US"),
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                            minLines = 4,
                            maxLines = 6,
                            isError = meaning.value.isBlank(),
                            label = {
                                Text(
                                    "المعنى",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 14.sp,
                                    ),
                                )
                            },
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = category.value,
                            onValueChange = { ar -> category.value = ar },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.labelLarge.copy(
                                textDirection = TextDirection.Ltr,
                                fontSize = 14.sp,
                            ),
                            keyboardActions = KeyboardActions {
                                if (arabic.value.isNotBlank() && meaning.value.isNotBlank() && category.value.isNotBlank()) {
                                    onAddClick(arabic.value, meaning.value, category.value)
                                } else if (arabic.value.isNotBlank()) {
                                    focusManager.clearFocus()
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                hintLocales = LocaleList("en-US, ar"),
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                            ),
                            maxLines = 1,
                            isError = category.value.isBlank(),
                            label = {
                                Text(
                                    "الفئة",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 14.sp,
                                    ),
                                )
                            },
                        )
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                        AnimatedVisibility(
                            arabic.value.isNotBlank() && meaning.value.isNotBlank() && category.value.isNotBlank(),
                            enter = fadeIn() + scaleIn(),
                            exit = scaleOut() + fadeOut(),
                        ) {
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    onAddClick(arabic.value, meaning.value, category.value)
                                },
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Text(
                                    "حفظ العبارة",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                )
                            }
                        }
                        Spacer(Modifier.height(40.dp))
                    }
                }
            },
        )
    }
}

