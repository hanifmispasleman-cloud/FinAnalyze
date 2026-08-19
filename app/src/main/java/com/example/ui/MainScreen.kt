package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CompanyGrade
import com.example.ui.components.ClassificationGradeSection
import com.example.ui.components.ExportReportDialog
import com.example.ui.components.GradeBadge
import com.example.ui.components.InputFormSection
import com.example.ui.components.RatiosCalculationSection
import com.example.ui.components.SavedAnalysesDialog
import com.example.ui.components.TeamWorkspaceSection
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald950
import com.example.ui.theme.EmeraldTranslucentBg
import com.example.ui.theme.EmeraldTranslucentBorder
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Rose400
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate750
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.viewmodel.FinancialViewModel

@Composable
fun MainScreen(
    viewModel: FinancialViewModel,
    modifier: Modifier = Modifier
) {
    val input by viewModel.inputState.collectAsStateWithLifecycle()
    val calculationResult by viewModel.calculationResult.collectAsStateWithLifecycle()
    val savedAnalyses by viewModel.savedAnalyses.collectAsStateWithLifecycle()
    val teamNotes by viewModel.teamNotes.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSavedDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    val tabItems = listOf(
        TabItem("Form Input", Icons.Default.Description, "Laporan Manual"),
        TabItem("Rasio & Skor", Icons.Default.Calculate, "Kalkulasi Real-Time"),
        TabItem("Klasifikasi", Icons.Default.MilitaryTech, "Grade A/B/C"),
        TabItem("Workspace", Icons.Default.Group, "Konsensus & Catatan")
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Slate950),
        containerColor = Slate950,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Professional Polish Terminal Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate950)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "FUNDAMENTAL TERMINAL",
                            color = Slate500,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${input.ticker} : ${input.companyName}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.3).sp,
                            maxLines = 1
                        )
                    }

                    GradeBadge(grade = calculationResult.grade)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Action & Terminal Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Slate900.copy(alpha = 0.8f))
                        .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Emerald400)
                        )
                        Text(
                            text = "${input.period} • F-Score ${calculationResult.piotroskiScore}/9",
                            color = Slate300,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // History Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Slate800)
                                .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                                .clickable { showSavedDialog = true }
                                .testTag("btn_saved_history"),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (savedAnalyses.isNotEmpty()) {
                                        Badge(
                                            containerColor = Emerald400,
                                            contentColor = Slate950
                                        ) {
                                            Text(savedAnalyses.size.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = "Riwayat",
                                    tint = Slate200,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Save Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmeraldTranslucentBg)
                                .border(1.dp, EmeraldTranslucentBorder, RoundedCornerShape(10.dp))
                                .clickable { viewModel.saveCurrentAnalysis() }
                                .testTag("btn_save_analysis"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Simpan",
                                tint = Emerald400,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Export Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Slate800)
                                .border(1.dp, Slate700, RoundedCornerShape(10.dp))
                                .clickable { showExportDialog = true }
                                .testTag("btn_export_report"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Ekspor",
                                tint = Cyan400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Modern M3 Navigation Bar with explicit insets handling
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = Slate900,
                contentColor = Slate100,
                tonalElevation = 0.dp
            ) {
                tabItems.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedTab(index) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Emerald400,
                            selectedTextColor = Emerald400,
                            indicatorColor = EmeraldTranslucentBg,
                            unselectedIconColor = Slate500,
                            unselectedTextColor = Slate500
                        ),
                        modifier = Modifier.testTag("nav_tab_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    InputFormSection(
                        input = input,
                        onInputUpdate = { viewModel.updateInput { _ -> it } },
                        onLoadPreset = { viewModel.loadPreset(it) },
                        onResetBlank = { viewModel.resetToBlank() }
                    )
                }
                1 -> {
                    RatiosCalculationSection(
                        result = calculationResult,
                        input = input
                    )
                }
                2 -> {
                    ClassificationGradeSection(
                        result = calculationResult,
                        input = input
                    )
                }
                3 -> {
                    TeamWorkspaceSection(
                        input = input,
                        notes = teamNotes,
                        onUpdateConsensus = { viewModel.updateConsensus(it) },
                        onUpdateTargetPrice = { viewModel.updateTargetPrice(it) },
                        onAddNote = { author, role, content, cons ->
                            viewModel.addTeamNote(author, role, content, cons)
                        },
                        onDeleteNote = { viewModel.deleteTeamNote(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialogs
    if (showSavedDialog) {
        SavedAnalysesDialog(
            analyses = savedAnalyses,
            onLoad = { viewModel.loadSavedAnalysis(it) },
            onDelete = { viewModel.deleteSavedAnalysis(it) },
            onDismiss = { showSavedDialog = false }
        )
    }

    if (showExportDialog) {
        ExportReportDialog(
            reportText = viewModel.generateShareReport(),
            onDismiss = { showExportDialog = false }
        )
    }
}

private data class TabItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String
)
