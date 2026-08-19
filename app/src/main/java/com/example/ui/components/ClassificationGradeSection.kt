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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationEngineResult
import com.example.model.CompanyGrade
import com.example.model.FinancialInput
import com.example.model.GradeRuleCheck
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

@Composable
fun ClassificationGradeSection(
    result: CalculationEngineResult,
    input: FinancialInput,
    modifier: Modifier = Modifier
) {
    val grade = result.grade
    val (bgGradient, glowBorder, glowBg) = when (grade) {
        CompanyGrade.GRADE_A -> Triple(
            Brush.linearGradient(listOf(Color(0xFF064E3B).copy(alpha = 0.5f), Color(0xFF022C22).copy(alpha = 0.8f))),
            EmeraldTranslucentBorder,
            EmeraldTranslucentBg
        )
        CompanyGrade.GRADE_B -> Triple(
            Brush.linearGradient(listOf(Color(0xFF78350F).copy(alpha = 0.5f), Color(0xFF451A03).copy(alpha = 0.8f))),
            AmberTranslucentBorder,
            AmberTranslucentBg
        )
        CompanyGrade.GRADE_C -> Triple(
            Brush.linearGradient(listOf(Color(0xFF881337).copy(alpha = 0.5f), Color(0xFF4C0519).copy(alpha = 0.8f))),
            RoseTranslucentBorder,
            RoseTranslucentBg
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Grade Classification Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, glowBorder, RoundedCornerShape(20.dp))
                .testTag("grade_hero_card"),
            color = Slate900.copy(alpha = 0.7f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgGradient)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "KLASIFIKASI KUALITAS EMITEN",
                                color = Slate400,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${input.ticker} : ${input.companyName}",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (-0.3).sp
                            )
                        }

                        GradeBadge(grade = grade)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate800, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = grade.description,
                        color = Slate200,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Rule-by-Rule Automated Scoring Checklist
        SectionCard(
            title = "Pemeriksaan Kriteria Algoritma",
            subtitle = "Kepatuhan terhadap benchmark Grade A / B / C",
            accentColor = Indigo400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.gradeRuleChecks.forEach { rule ->
                    GradeRuleRow(rule = rule)
                }
            }
        }

        // Automated System Diagnosis Box
        SectionCard(
            title = "Diagnosis Sintesis Analis",
            subtitle = "Evaluasi otomatis dari gabungan metrik profit, utang, dan kas",
            accentColor = Cyan400
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Slate900.copy(alpha = 0.5f))
                    .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Indigo400,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "Rangkuman Fundamental:",
                            color = Slate100,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.summaryDiagnosis,
                            color = Slate300,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Decision Framework Matrix Guide
        SectionCard(
            title = "Panduan Standar Grade FinAnalyzer",
            subtitle = "Aturan matematis penentuan grade fundamental",
            accentColor = Amber400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GradeGuideItem(
                    code = "GRADE A (Wonderful Company)",
                    criteria = "ROE > 15% • DER < 1.0x • CFO/NI > 1.0x • F-Score >= 7",
                    action = "Cocok untuk akumulasi jangka panjang / Compounder.",
                    color = Emerald400,
                    bg = EmeraldTranslucentBg,
                    border = EmeraldTranslucentBorder
                )
                GradeGuideItem(
                    code = "GRADE B (Fair / Moderate Company)",
                    criteria = "ROE 8-15% • DER 1.0-1.5x • F-Score 4-6",
                    action = "Fundamental wajar, perhatikan valuasi dan momentum katalis.",
                    color = Amber400,
                    bg = AmberTranslucentBg,
                    border = AmberTranslucentBorder
                )
                GradeGuideItem(
                    code = "GRADE C (High Risk / Value Trap)",
                    criteria = "ROE < 8% ATAU DER > 2.0x ATAU CFO/NI < 0.8x",
                    action = "Tinggi risiko beban hutang / manipulasi laba akrual. Hati-hati.",
                    color = Rose400,
                    bg = RoseTranslucentBg,
                    border = RoseTranslucentBorder
                )
            }
        }
    }
}

@Composable
fun GradeRuleRow(rule: GradeRuleCheck, modifier: Modifier = Modifier) {
    val (statusColor, icon, bgCol, borderCol) = if (rule.isPassed) {
        Tuple4(Emerald400, Icons.Default.CheckCircle, EmeraldTranslucentBg, EmeraldTranslucentBorder)
    } else if (rule.isCriticalRisk) {
        Tuple4(Rose400, Icons.Default.Warning, RoseTranslucentBg, RoseTranslucentBorder)
    } else {
        Tuple4(Amber400, Icons.Default.Info, AmberTranslucentBg, AmberTranslucentBorder)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderCol, RoundedCornerShape(16.dp)),
        color = Slate900.copy(alpha = 0.45f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(20.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rule.title,
                    color = Slate200,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Syarat: ${rule.requiredBenchmark}",
                    color = Slate500,
                    fontSize = 10.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = rule.actualValue,
                    color = statusColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = if (rule.isPassed) "LOLOS (A)" else if (rule.isCriticalRisk) "ZONA RISIKO (C)" else "MODERAT (B)",
                    color = statusColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

@Composable
fun GradeGuideItem(
    code: String,
    criteria: String,
    action: String,
    color: Color,
    bg: Color,
    border: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text(
            text = code,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = criteria,
            color = Slate200,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = action,
            color = Slate400,
            fontSize = 10.sp
        )
    }
}

