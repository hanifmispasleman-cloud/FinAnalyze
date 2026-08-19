package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CompanyGrade
import com.example.model.MetricStatus
import com.example.model.TeamConsensus
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber600
import com.example.ui.theme.AmberTranslucentBg
import com.example.ui.theme.AmberTranslucentBorder
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald600
import com.example.ui.theme.EmeraldTranslucentBg
import com.example.ui.theme.EmeraldTranslucentBorder
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose600
import com.example.ui.theme.RoseTranslucentBg
import com.example.ui.theme.RoseTranslucentBorder
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import java.util.Locale

@Composable
fun StatusBadge(
    status: MetricStatus,
    modifier: Modifier = Modifier,
    customText: String? = null
) {
    val (bgColor, borderColor, textColor) = when (status) {
        MetricStatus.SAFE -> Triple(EmeraldTranslucentBg, EmeraldTranslucentBorder, Emerald400)
        MetricStatus.MODERATE -> Triple(AmberTranslucentBg, AmberTranslucentBorder, Amber400)
        MetricStatus.RISK -> Triple(RoseTranslucentBg, RoseTranslucentBorder, Rose400)
    }
    val label = customText ?: when (status) {
        MetricStatus.SAFE -> "AMAN"
        MetricStatus.MODERATE -> "MODERAT"
        MetricStatus.RISK -> "RISIKO"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun GradeBadge(
    grade: CompanyGrade,
    modifier: Modifier = Modifier
) {
    val (bgCol, borderCol, textCol) = when (grade) {
        CompanyGrade.GRADE_A -> Triple(
            EmeraldTranslucentBg,
            EmeraldTranslucentBorder,
            Emerald400
        )
        CompanyGrade.GRADE_B -> Triple(
            AmberTranslucentBg,
            AmberTranslucentBorder,
            Amber400
        )
        CompanyGrade.GRADE_C -> Triple(
            RoseTranslucentBg,
            RoseTranslucentBorder,
            Rose400
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgCol)
            .border(1.dp, borderCol, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = grade.code,
            color = textCol,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun FinancialInputField(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    prefix: String = "Rp",
    suffix: String? = null,
    testTag: String = ""
) {
    var rawText by remember(value) {
        mutableStateOf(if (value == 0.0) "" else String.format(Locale.US, "%.1f", value).removeSuffix(".0"))
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Slate400,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            if (helperText != null) {
                Text(
                    text = helperText,
                    color = Slate500,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = rawText,
            onValueChange = { newStr ->
                val filtered = newStr.filter { it.isDigit() || it == '.' || it == '-' }
                rawText = filtered
                val parsed = filtered.toDoubleOrNull() ?: 0.0
                onValueChange(parsed)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag.ifBlank { "input_${label.lowercase().replace(" ", "_")}" }),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Slate100,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            ),
            prefix = {
                Text(
                    text = "$prefix ",
                    color = Slate500,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            },
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        color = Indigo400,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
            },
            trailingIcon = {
                if (rawText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            rawText = ""
                            onValueChange(0.0)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Hapus",
                            tint = Slate500,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Slate850.copy(alpha = 0.6f),
                unfocusedContainerColor = Slate900.copy(alpha = 0.5f),
                focusedBorderColor = Emerald400,
                unfocusedBorderColor = Slate800,
                cursorColor = Emerald400
            ),
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    accentColor: Color = Indigo400,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Slate800, RoundedCornerShape(24.dp)),
        color = Slate900.copy(alpha = 0.6f),
        tonalElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title.uppercase(),
                        color = Slate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                    if (subtitle != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            color = Slate500,
                            fontSize = 11.sp
                        )
                    }
                }
                action?.invoke()
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun ConsensusButton(
    consensus: TeamConsensus,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeBg = when (consensus) {
        TeamConsensus.BUY -> Emerald600
        TeamConsensus.WATCHLIST -> Amber600
        TeamConsensus.AVOID -> Rose600
    }

    val animatedBg by animateColorAsState(if (selected) activeBg else Slate800, label = "bg")
    val animatedText by animateColorAsState(if (selected) Color.White else Slate300, label = "text")

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(animatedBg)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (consensus) {
                TeamConsensus.BUY -> "BUY"
                TeamConsensus.WATCHLIST -> "WATCH"
                TeamConsensus.AVOID -> "AVOID"
            },
            color = animatedText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

