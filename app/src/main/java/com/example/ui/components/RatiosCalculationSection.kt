package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationEngineResult
import com.example.model.FinancialInput
import com.example.model.MetricStatus
import com.example.model.PiotroskiCriterion
import com.example.model.RatioMetric
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.AmberTranslucentBg
import com.example.ui.theme.AmberTranslucentBorder
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.EmeraldTranslucentBg
import com.example.ui.theme.EmeraldTranslucentBorder
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose500
import com.example.ui.theme.RoseTranslucentBg
import com.example.ui.theme.RoseTranslucentBorder
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

@Composable
fun RatiosCalculationSection(
    result: CalculationEngineResult,
    input: FinancialInput,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Professional Polish Hero Live Ratio Cards (3-column grid)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "LIVE RATIO ANALYSIS",
                color = Slate400,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TerminalRatioTile(
                    title = "ROE",
                    value = "${String.format(java.util.Locale.US, "%.1f", result.roe)}%",
                    subtitle = if (result.roe >= 15) "High Return" else if (result.roe >= 8) "Moderate" else "Low Margin",
                    progress = (result.roe.toFloat() / 25f).coerceIn(0f, 1f),
                    status = if (result.roe >= 15) MetricStatus.SAFE else if (result.roe >= 8) MetricStatus.MODERATE else MetricStatus.RISK,
                    modifier = Modifier.weight(1f)
                )
                TerminalRatioTile(
                    title = "DER",
                    value = "${String.format(java.util.Locale.US, "%.2f", result.der)}x",
                    subtitle = if (result.der <= 1.0) "Safe Margin" else if (result.der <= 1.5) "Moderate" else "High Debt",
                    progress = (1f - (result.der.toFloat() / 3f)).coerceIn(0.1f, 1f),
                    status = if (result.der <= 1.0) MetricStatus.SAFE else if (result.der <= 1.5) MetricStatus.MODERATE else MetricStatus.RISK,
                    modifier = Modifier.weight(1f)
                )
                TerminalRatioTile(
                    title = "CFO/NI",
                    value = "${String.format(java.util.Locale.US, "%.2f", result.cfoToNetIncome)}x",
                    subtitle = if (result.cfoToNetIncome >= 1.0) "High Quality" else if (result.cfoToNetIncome >= 0.8) "Fair Quality" else "Accrual Risk",
                    progress = (result.cfoToNetIncome.toFloat() / 2f).coerceIn(0f, 1f),
                    status = if (result.cfoToNetIncome >= 1.0) MetricStatus.SAFE else if (result.cfoToNetIncome >= 0.8) MetricStatus.MODERATE else MetricStatus.RISK,
                    modifier = Modifier.weight(1f)
                )
            }

            // 2-column secondary quick stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Slate800, RoundedCornerShape(16.dp)),
                    color = Slate900.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Current Ratio", color = Slate400, fontSize = 11.sp)
                        Text(
                            text = "${String.format(java.util.Locale.US, "%.2f", result.currentRatio)}x",
                            color = if (result.currentRatio >= 1.5) Emerald400 else if (result.currentRatio >= 1.0) Amber400 else Rose400,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Slate800, RoundedCornerShape(16.dp)),
                    color = Slate900.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Piotroski F", color = Slate400, fontSize = 11.sp)
                        Text(
                            text = "${result.piotroskiScore}/9",
                            color = if (result.piotroskiScore >= 7) Emerald400 else if (result.piotroskiScore >= 4) Amber400 else Rose400,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Category 1: Profitabilitas
        SectionCard(
            title = "Profitabilitas & Margin",
            subtitle = "Kapasitas emiten mencetak imbal hasil dari modal dan omset",
            accentColor = Emerald400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.metricsList.filter { it.category == "Profitabilitas" }.forEach { metric ->
                    RatioRowCard(metric = metric)
                }
            }
        }

        // Category 2: Solvabilitas & Likuiditas
        SectionCard(
            title = "Solvabilitas & Likuiditas",
            subtitle = "Kemampuan melunasi hutang berbunga dan kewajiban jatuh tempo",
            accentColor = Amber400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.metricsList.filter { it.category.contains("Solvabilitas") || it.category.contains("Likuiditas") }.forEach { metric ->
                    RatioRowCard(metric = metric)
                }
            }
        }

        // Category 3: Kualitas Kas & FCF
        SectionCard(
            title = "Kualitas Arus Kas & Free Cash Flow",
            subtitle = "Deteksi kualitas laba akuntansi vs uang kas nyata & imbal hasil FCF",
            accentColor = Cyan400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.metricsList.filter { it.category.contains("Kas") || it.category.contains("FCF") || it.category.contains("Bebas") }.forEach { metric ->
                    RatioRowCard(metric = metric)
                }
            }
        }

        // Category 4: Piotroski F-Score Breakdown (9 Criteria)
        SectionCard(
            title = "Piotroski F-Score (9 Poin)",
            subtitle = "Skor komposit kesehatan fundamental emiten",
            accentColor = Indigo400,
            action = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            if (result.piotroskiScore >= 7) EmeraldTranslucentBg
                            else if (result.piotroskiScore >= 4) AmberTranslucentBg
                            else RoseTranslucentBg
                        )
                        .border(
                            1.dp,
                            if (result.piotroskiScore >= 7) EmeraldTranslucentBorder
                            else if (result.piotroskiScore >= 4) AmberTranslucentBorder
                            else RoseTranslucentBorder,
                            RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SKOR ${result.piotroskiScore} / 9",
                        color = if (result.piotroskiScore >= 7) Emerald400 else if (result.piotroskiScore >= 4) Amber400 else Rose400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = { (result.piotroskiScore / 9f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (result.piotroskiScore >= 7) Emerald400 else if (result.piotroskiScore >= 4) Amber400 else Rose400,
                    trackColor = Slate800,
                )
                Spacer(modifier = Modifier.height(4.dp))

                result.piotroskiCriteria.forEach { crit ->
                    PiotroskiRow(criterion = crit)
                }
            }
        }
    }
}

@Composable
fun TerminalRatioTile(
    title: String,
    value: String,
    subtitle: String,
    progress: Float,
    status: MetricStatus,
    modifier: Modifier = Modifier
) {
    val (statusCol, bgCol, borderCol) = when (status) {
        MetricStatus.SAFE -> Triple(Emerald400, EmeraldTranslucentBg, EmeraldTranslucentBorder)
        MetricStatus.MODERATE -> Triple(Amber400, AmberTranslucentBg, AmberTranslucentBorder)
        MetricStatus.RISK -> Triple(Rose400, RoseTranslucentBg, RoseTranslucentBorder)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bgCol)
            .border(1.dp, borderCol, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = title,
            color = statusCol,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        // Micro progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(borderCol)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(statusCol)
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = subtitle,
            color = statusCol,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RatioRowCard(metric: RatioMetric, modifier: Modifier = Modifier) {
    val (valColor, borderColor) = when (metric.status) {
        MetricStatus.SAFE -> Pair(Emerald400, EmeraldTranslucentBorder)
        MetricStatus.MODERATE -> Pair(Amber400, AmberTranslucentBorder)
        MetricStatus.RISK -> Pair(Rose400, RoseTranslucentBorder)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Slate800, RoundedCornerShape(16.dp)),
        color = Slate850.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = metric.name,
                        color = Slate200,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Rumus: ${metric.formulaDisplay}",
                        color = Slate500,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = metric.formattedValue,
                        color = valColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    StatusBadge(status = metric.status)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Target: ${metric.benchmarkSafe}",
                    color = Slate500,
                    fontSize = 11.sp
                )
                Text(
                    text = metric.statusNote,
                    color = valColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PiotroskiRow(criterion: PiotroskiCriterion, modifier: Modifier = Modifier) {
    val isPassed = criterion.isPassed
    val statusColor = if (isPassed) Emerald400 else Slate500
    val icon = if (isPassed) Icons.Default.Check else Icons.Default.Close

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPassed) EmeraldTranslucentBg else Slate900.copy(alpha = 0.4f))
            .border(
                1.dp,
                if (isPassed) EmeraldTranslucentBorder else Slate800,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isPassed) Emerald400 else Slate700),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPassed) Slate950 else Slate400,
                modifier = Modifier.size(12.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${criterion.number}. ${criterion.title}",
                    color = if (isPassed) Slate200 else Slate400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = criterion.detailValue,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = criterion.description,
                color = Slate500,
                fontSize = 10.sp
            )
        }
    }
}

