package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FinancialInput
import com.example.model.TeamConsensus
import com.example.model.TeamNote
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber950
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald950
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose950
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate750
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeamWorkspaceSection(
    input: FinancialInput,
    notes: List<TeamNote>,
    onUpdateConsensus: (TeamConsensus) -> Unit,
    onUpdateTargetPrice: (Double) -> Unit,
    onAddNote: (String, String, String, TeamConsensus) -> Unit,
    onDeleteNote: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var authorInput by remember { mutableStateOf("Analis Riset") }
    var roleInput by remember { mutableStateOf("Equity Analyst") }
    var contentInput by remember { mutableStateOf("") }
    var noteConsensus by remember { mutableStateOf(input.consensus) }

    val upsideDownsidePercent = if (input.marketPrice > 0) {
        ((input.targetPrice - input.marketPrice) / input.marketPrice) * 100.0
    } else 0.0

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Consensus & Target Price Master Card
        SectionCard(
            title = "1. Konsensus Tim & Target Harga",
            subtitle = "Sinergi keputusan tim riset internal untuk emiten ${input.ticker}",
            accentColor = Emerald400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Consensus 3-way toggle
                Text(
                    text = "Konsensus Rekomendasi Tim:",
                    color = Slate300,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TeamConsensus.values().forEach { c ->
                        ConsensusButton(
                            consensus = c,
                            selected = input.consensus == c,
                            onClick = { onUpdateConsensus(c) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("consensus_btn_${c.name.lowercase()}")
                        )
                    }
                }

                // Target price & Upside/Downside Calculator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinancialInputField(
                        label = "Harga Target (Fair Value)",
                        value = input.targetPrice,
                        onValueChange = onUpdateTargetPrice,
                        modifier = Modifier.weight(0.6f),
                        prefix = "Rp",
                        testTag = "input_target_price"
                    )

                    // Upside/Downside Badge
                    Column(
                        modifier = Modifier
                            .weight(0.4f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (upsideDownsidePercent >= 0) Emerald950 else Rose950)
                            .border(
                                1.dp,
                                if (upsideDownsidePercent >= 0) Emerald400 else Rose400,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (upsideDownsidePercent >= 0) "Potensi Upside" else "Potensi Downside",
                            color = Slate400,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${if (upsideDownsidePercent >= 0) "+" else ""}${String.format(Locale.US, "%.1f", upsideDownsidePercent)}%",
                            color = if (upsideDownsidePercent >= 0) Emerald400 else Rose400,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Add New Note Form Card
        SectionCard(
            title = "2. Tambah Catatan Analisis Kolaboratif",
            subtitle = "Dokumentasikan tesis investasi, katalis, risiko, atau catatan audit",
            accentColor = Indigo400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = authorInput,
                        onValueChange = { authorInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_note_author"),
                        label = { Text("Nama Analis", color = Slate400, fontSize = 12.sp) },
                        placeholder = { Text("Contoh: Budi Santoso", color = Slate600, fontSize = 12.sp) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Slate100),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Slate850,
                            unfocusedContainerColor = Slate900,
                            focusedBorderColor = Indigo400,
                            unfocusedBorderColor = Slate700
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = roleInput,
                        onValueChange = { roleInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_note_role"),
                        label = { Text("Jabatan / Peran", color = Slate400, fontSize = 12.sp) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Slate100),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Slate850,
                            unfocusedContainerColor = Slate900,
                            focusedBorderColor = Indigo400,
                            unfocusedBorderColor = Slate700
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = contentInput,
                    onValueChange = { contentInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp)
                        .testTag("input_note_content"),
                    label = { Text("Catatan Analisis / Tesis Investasi", color = Slate400, fontSize = 12.sp) },
                    placeholder = {
                        Text(
                            "Tuliskan hasil telaah kualitatif, moat bisnis, katalis jangka pendek, atau risiko hutang...",
                            color = Slate600,
                            fontSize = 12.sp
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Slate100),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate900,
                        focusedBorderColor = Indigo400,
                        unfocusedBorderColor = Slate700
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // Recommendation toggle for the note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rekomendasi Analis:",
                        color = Slate400,
                        fontSize = 11.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        TeamConsensus.values().forEach { c ->
                            val isSel = noteConsensus == c
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Indigo400 else Slate800)
                                    .clickable { noteConsensus = c }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = c.name,
                                    color = if (isSel) Color.White else Slate400,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                    Button(
                    onClick = {
                        val author = authorInput.ifBlank { "Analis Riset" }
                        val role = roleInput.ifBlank { "Equity Analyst" }
                        val noteText = contentInput.ifBlank { "Analisis fundamental emiten ${input.ticker}: valuasi & neraca keuangan." }
                        onAddNote(author, role, noteText, noteConsensus)
                        contentInput = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_note"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Emerald400,
                        contentColor = Slate950
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PostAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Publikasikan Catatan ke Tim",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Team Notes Timeline Feed
        SectionCard(
            title = "3. Timeline Catatan Tim (${notes.size})",
            subtitle = "Diskusi & riwayat telaah internal emiten ${input.ticker}",
            accentColor = Cyan400
        ) {
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate850)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            tint = Slate500,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Belum ada catatan tim untuk emiten ini.",
                            color = Slate400,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Tambahkan catatan riset pertama di atas.",
                            color = Slate500,
                            fontSize = 11.sp
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    notes.forEach { note ->
                        TeamNoteItem(
                            note = note,
                            onDelete = { onDeleteNote(note.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamNoteItem(
    note: TeamNote,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    val dateStr = sdf.format(Date(note.timestamp))

    val (tagBg, tagText) = when (note.consensus) {
        TeamConsensus.BUY -> Pair(Emerald950, Emerald400)
        TeamConsensus.WATCHLIST -> Pair(Amber950, Amber400)
        TeamConsensus.AVOID -> Pair(Rose950, Rose400)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Slate750, RoundedCornerShape(12.dp)),
        color = Slate850
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Indigo400.copy(alpha = 0.25f))
                            .border(1.dp, Indigo400, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = note.authorName.take(1).uppercase(),
                            color = Indigo400,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = note.authorName,
                            color = Slate100,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${note.authorRole} • $dateStr",
                            color = Slate400,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(tagBg)
                            .border(1.dp, tagText.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = note.consensus.name,
                            color = tagText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Hapus catatan",
                            tint = Slate500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                color = Slate200,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}
