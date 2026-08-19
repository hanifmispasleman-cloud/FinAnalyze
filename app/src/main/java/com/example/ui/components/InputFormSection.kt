package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FinancialEngine
import com.example.model.FinancialInput
import com.example.model.FinancialUnit
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald950
import com.example.ui.theme.EmeraldTranslucentBg
import com.example.ui.theme.EmeraldTranslucentBorder
import com.example.ui.theme.Indigo400
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

@Composable
fun InputFormSection(
    input: FinancialInput,
    onInputUpdate: (FinancialInput) -> Unit,
    onLoadPreset: (FinancialInput) -> Unit,
    onResetBlank: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unitSuffix = when (input.unit) {
        FinancialUnit.BILLION -> "M"
        FinancialUnit.MILLION -> "Jt"
        FinancialUnit.RAW -> "Rp"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Preset Chips Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate900.copy(alpha = 0.5f))
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = Emerald400,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "PRESET EMITEN TERUJI",
                    color = Slate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FinancialEngine.PRESET_COMPANIES.forEach { preset ->
                    val isSelected = input.ticker == preset.ticker
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isSelected) Emerald400 else Slate850)
                            .border(
                                1.dp,
                                if (isSelected) Emerald400 else Slate800,
                                RoundedCornerShape(999.dp)
                            )
                            .clickable { onLoadPreset(preset) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("preset_${preset.ticker.lowercase()}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = preset.ticker,
                                color = if (isSelected) Slate950 else Slate200,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• ${preset.companyName.split(" ")[0]}",
                                color = if (isSelected) Slate900 else Slate500,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Reset button chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Slate850)
                        .border(1.dp, Slate800, RoundedCornerShape(999.dp))
                        .clickable { onResetBlank() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("preset_reset_blank")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Form Kosong",
                            color = Slate400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Section 1: Profil Emiten & Satuan Laporan
        SectionCard(
            title = "1. Profil Emiten & Parameter",
            subtitle = "Identitas saham, periode laporan, dan skala nominal",
            accentColor = Indigo400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(0.45f)) {
                        Text("Kode Saham (Ticker)", color = Slate300, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = input.ticker,
                            onValueChange = { onInputUpdate(input.copy(ticker = it.uppercase())) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_ticker"),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = Slate100,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Slate850,
                                unfocusedContainerColor = Slate900,
                                focusedBorderColor = Indigo400,
                                unfocusedBorderColor = Slate700
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(0.55f)) {
                        Text("Periode Buku", color = Slate300, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = input.period,
                            onValueChange = { onInputUpdate(input.copy(period = it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_period"),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Slate100),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Slate850,
                                unfocusedContainerColor = Slate900,
                                focusedBorderColor = Indigo400,
                                unfocusedBorderColor = Slate700
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Column {
                    Text("Nama Lengkap Perusahaan", color = Slate300, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = input.companyName,
                        onValueChange = { onInputUpdate(input.copy(companyName = it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_company_name"),
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

                // Financial Unit Selector
                Column {
                    Text("Satuan Nominal Input Angka", color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FinancialUnit.values().forEach { unit ->
                            val isSel = input.unit == unit
                            Box(
                                 modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) EmeraldTranslucentBg else Slate900.copy(alpha = 0.5f))
                                    .border(1.dp, if (isSel) EmeraldTranslucentBorder else Slate800, RoundedCornerShape(12.dp))
                                    .clickable { onInputUpdate(input.copy(unit = unit)) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unit.label,
                                    color = if (isSel) Emerald400 else Slate400,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Market price & shares
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FinancialInputField(
                        label = "Harga Saham Terkini",
                        value = input.marketPrice,
                        onValueChange = { onInputUpdate(input.copy(marketPrice = it)) },
                        modifier = Modifier.weight(1f),
                        prefix = "Rp",
                        testTag = "input_market_price"
                    )
                    FinancialInputField(
                        label = "Saham Beredar",
                        value = input.sharesOutstanding,
                        onValueChange = { onInputUpdate(input.copy(sharesOutstanding = it)) },
                        modifier = Modifier.weight(1f),
                        prefix = "",
                        suffix = unitSuffix,
                        testTag = "input_shares_outstanding"
                    )
                }
            }
        }

        // Section 2: Laporan Laba Rugi (Income Statement)
        SectionCard(
            title = "2. Laporan Laba Rugi (Income Statement)",
            subtitle = "Omset, efisiensi produksi, laba operasional & laba bersih",
            accentColor = Emerald400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FinancialInputField(
                    label = "Pendapatan (Revenue / Omset)",
                    value = input.revenue,
                    onValueChange = { onInputUpdate(input.copy(revenue = it)) },
                    helperText = "Total omset penjualan bersih",
                    suffix = unitSuffix,
                    testTag = "input_revenue"
                )

                FinancialInputField(
                    label = "Laba Kotor (Gross Profit)",
                    value = input.grossProfit,
                    onValueChange = { onInputUpdate(input.copy(grossProfit = it)) },
                    helperText = "Revenue - Beban Pokok Pendapatan (COGS)",
                    suffix = unitSuffix,
                    testTag = "input_gross_profit"
                )

                FinancialInputField(
                    label = "Laba Usaha / Operasi (EBIT)",
                    value = input.operatingIncome,
                    onValueChange = { onInputUpdate(input.copy(operatingIncome = it)) },
                    helperText = "Laba sebelum beban bunga & pajak",
                    suffix = unitSuffix,
                    testTag = "input_operating_income"
                )

                FinancialInputField(
                    label = "Laba Bersih Tahun Berjalan (Net Income)",
                    value = input.netIncome,
                    onValueChange = { onInputUpdate(input.copy(netIncome = it)) },
                    helperText = "Bottom-line laba yang dapat diatribusikan ke pemilik",
                    suffix = unitSuffix,
                    testTag = "input_net_income"
                )
            }
        }

        // Section 3: Arus Kas & Neraca Keuangan (Cash Flow & Balance Sheet)
        SectionCard(
            title = "3. Arus Kas & Neraca (Cash Flow & Balance Sheet)",
            subtitle = "Likuiditas kas riil, struktur hutang, dan total ekuitas pemegang saham",
            accentColor = Cyan400
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Pos Arus Kas (Cash Flow):",
                    color = Cyan400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                FinancialInputField(
                    label = "Arus Kas Operasi (CFO - Operating Cash Flow)",
                    value = input.operatingCashFlow,
                    onValueChange = { onInputUpdate(input.copy(operatingCashFlow = it)) },
                    helperText = "Kas riil hasil operasional inti",
                    suffix = unitSuffix,
                    testTag = "input_cfo"
                )

                FinancialInputField(
                    label = "Belanja Modal (CAPEX / Capital Expenditure)",
                    value = input.capex,
                    onValueChange = { onInputUpdate(input.copy(capex = it)) },
                    helperText = "Investasi aset tetap & ekspansi",
                    suffix = unitSuffix,
                    testTag = "input_capex"
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pos Neraca Keuangan (Balance Sheet):",
                    color = Amber400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                FinancialInputField(
                    label = "Kas & Setara Kas",
                    value = input.cashAndEquivalents,
                    onValueChange = { onInputUpdate(input.copy(cashAndEquivalents = it)) },
                    helperText = "Simpanan tunai & deposito likuid",
                    suffix = unitSuffix,
                    testTag = "input_cash"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FinancialInputField(
                        label = "Aset Lancar (Current Assets)",
                        value = input.currentAssets,
                        onValueChange = { onInputUpdate(input.copy(currentAssets = it)) },
                        modifier = Modifier.weight(1f),
                        suffix = unitSuffix,
                        testTag = "input_current_assets"
                    )
                    FinancialInputField(
                        label = "Total Aset",
                        value = input.totalAssets,
                        onValueChange = { onInputUpdate(input.copy(totalAssets = it)) },
                        modifier = Modifier.weight(1f),
                        suffix = unitSuffix,
                        testTag = "input_total_assets"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FinancialInputField(
                        label = "Liabilitas Lancar (Hutang Pendek)",
                        value = input.currentLiabilities,
                        onValueChange = { onInputUpdate(input.copy(currentLiabilities = it)) },
                        modifier = Modifier.weight(1f),
                        suffix = unitSuffix,
                        testTag = "input_current_liabilities"
                    )
                    FinancialInputField(
                        label = "Total Utang Berbunga (Interest Debt)",
                        value = input.totalDebt,
                        onValueChange = { onInputUpdate(input.copy(totalDebt = it)) },
                        modifier = Modifier.weight(1f),
                        suffix = unitSuffix,
                        testTag = "input_total_debt"
                    )
                }

                FinancialInputField(
                    label = "Total Ekuitas (Nilai Buku Bersih)",
                    value = input.totalEquity,
                    onValueChange = { onInputUpdate(input.copy(totalEquity = it)) },
                    helperText = "Modal disetor + Saldo laba ditahan",
                    suffix = unitSuffix,
                    testTag = "input_total_equity"
                )
            }
        }
    }
}
