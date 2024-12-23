package com.andreasmlbngaol.codegenerator.views.code

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreasmlbngaol.codegenerator.model.db.CodeEvent
import com.andreasmlbngaol.codegenerator.model.db.SortType
import com.andreasmlbngaol.codegenerator.model.millisToLocalDateTime

@Composable
fun CodeScreen(
    viewModel: CodeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(CodeEvent.ShowDialog)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Code"
                )
            }
        }
    ) { paddingValues ->
        if(state.isAddingCode) {
            AddCodeDialog(
                state = state,
                onEvent = onEvent
            )
        }
        if(state.qrCode.width > 1) {
            QrCodeDialog(
                onDismiss = {
                    onEvent(CodeEvent.HideQrCode)
                },
                state = state,
            )
        }
        if(state.barCode.width > 1) {
            BarCodeDialog(
                onDismiss = {
                    onEvent(CodeEvent.HideBarCode)
                },
                state = state
            )
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SortType.entries) { sortType ->
                        val selected = state.sortType == sortType
                        FilterChip(
                            label = {
                                Text(text = sortType.name)
                            },
                            selected = selected,
                            onClick = {
                                onEvent(CodeEvent.SortCodes(sortType))
                            },
                            leadingIcon = {
                                AnimatedVisibility(selected) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon"
                                    )
                                }
                            }
                        )
                    }
                }
            }
            items(state.codes) { code ->
                ListItem(
                    headlineContent = {
                        Text(text = code.title)
                    },
                    supportingContent = {
                        Text(
                            text = code.content,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    overlineContent = {
                        Text(text = code.createdAt.millisToLocalDateTime())
                    },
                    leadingContent = {
                        IconButton(
                            onClick = {
                                onEvent(CodeEvent.DeleteCode(code))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete code",
                            )
                        }
                    },
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    onEvent(CodeEvent.ShowQrCode(code))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = "QR code"
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEvent(CodeEvent.ShowBarCode(code))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LineWeight,
                                    contentDescription = "Done code",
                                    modifier = Modifier
                                        .rotate(-90f)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}