package com.example.weatherapp.presentation.alert.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.*

private data class WeatherOption(
    val type:    String,
    val iconRes: Int,
    val labelRes: Int
)

private val weatherOptions = listOf(
    WeatherOption("rain",  R.drawable.rainy,  R.string.weather_type_rain),
    WeatherOption("snow",  R.drawable.snow,  R.string.weather_type_snow),
    WeatherOption("storm", R.drawable.storm, R.string.weather_type_storm),
    WeatherOption("fog",   R.drawable.fog,   R.string.weather_type_fog),
    WeatherOption("heat",  R.drawable.heat,  R.string.weather_type_heat),
    WeatherOption("cold",  R.drawable.cold,  R.string.weather_type_cold),
    WeatherOption("wind",  R.drawable.air,  R.string.weather_type_wind),
    WeatherOption("any",   R.drawable.sun,   R.string.weather_type_any)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Alert) -> Unit
) {
    val colors  = AppTheme.colors
    val context = LocalContext.current
    val fmt     = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())

    var startMillis     by remember { mutableStateOf<Long?>(null) }
    var endMillis       by remember { mutableStateOf<Long?>(null) }
    var selectedWeather by remember { mutableStateOf("rain") }
    var errorMessage    by remember { mutableStateOf<String?>(null) }

    val errNoStart    = stringResource(R.string.alerts_error_no_start)
    val errNoEnd      = stringResource(R.string.alerts_error_no_end)
    val errEndBefore  = stringResource(R.string.alerts_error_end_before_start)
    val errPast       = stringResource(R.string.alerts_error_start_in_past)

    fun pickDateTime(onResult: (Long) -> Unit) {
        val now = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val cal = Calendar.getInstance().apply {
                            set(year, month, day, hour, minute, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        onResult(cal.timeInMillis)
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
                ).show()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor   = colors.cardBg,
        shape            = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle       = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(colors.textMuted.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Column {
                Text(
                    text       = stringResource(R.string.alerts_sheet_title),
                    color      = colors.textPrimary,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = stringResource(R.string.alerts_sheet_subtitle),
                    color    = colors.textMuted,
                    fontSize = 12.sp
                )
            }

            HorizontalDivider(color = colors.textMuted.copy(alpha = 0.12f))

            DateTimePickerRow(
                label        = stringResource(R.string.alerts_sheet_start_label),
                icon         = R.drawable.notifications,
                selectedText = startMillis?.let { fmt.format(Date(it)) }
                    ?: stringResource(R.string.alerts_sheet_start_hint),
                isSet        = startMillis != null,
                accentColor  = colors.accent,
                mutedColor   = colors.textMuted,
                primaryColor = colors.textPrimary,
                cardBg       = colors.cardBg,
                onClick      = { pickDateTime { startMillis = it } }
            )

            DateTimePickerRow(
                label        = stringResource(R.string.alerts_sheet_end_label),
                icon         = R.drawable.stop,
                selectedText = endMillis?.let { fmt.format(Date(it)) }
                    ?: stringResource(R.string.alerts_sheet_end_hint),
                isSet        = endMillis != null,
                accentColor  = colors.danger,
                mutedColor   = colors.textMuted,
                primaryColor = colors.textPrimary,
                cardBg       = colors.cardBg,
                onClick      = { pickDateTime { endMillis = it } }
            )

            errorMessage?.let { msg ->
                Text(
                    text     = msg,
                    color    = colors.danger,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            HorizontalDivider(color = colors.textMuted.copy(alpha = 0.12f))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text          = stringResource(R.string.alerts_sheet_condition_title),
                    color         = colors.accent,
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text     = stringResource(R.string.alerts_sheet_condition_subtitle),
                    color    = colors.textMuted,
                    fontSize = 12.sp
                )
                weatherOptions.chunked(2).forEach { row ->
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { option ->
                            WeatherChip(
                                option       = option,
                                isSelected   = selectedWeather == option.type,
                                accentColor  = colors.accent,
                                mutedColor   = colors.textMuted,
                                cardBg       = colors.cardBg,
                                modifier     = Modifier.weight(1f),
                                onClick      = { selectedWeather = option.type }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = {
                    errorMessage = null
                    val s = startMillis
                    val e = endMillis
                    when {
                        s == null             -> errorMessage = errNoStart
                        e == null             -> errorMessage = errNoEnd
                        e <= s                -> errorMessage = errEndBefore
                        s <= System.currentTimeMillis() -> errorMessage = errPast
                        else -> onConfirm(
                            Alert(
                                startTime   = s,
                                endTime     = e,
                                type        = "alarm",
                                weatherType = selectedWeather
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor   = colors.textPrimary
                )
            ) {
                Icon(
                    painter            = painterResource(R.drawable.notifications),
                    contentDescription = null,
                    modifier           = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = stringResource(R.string.alerts_sheet_confirm),
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DateTimePickerRow(
    label:        String,
    icon:         Int,
    selectedText: String,
    isSet:        Boolean,
    accentColor:  androidx.compose.ui.graphics.Color,
    mutedColor:   androidx.compose.ui.graphics.Color,
    primaryColor: androidx.compose.ui.graphics.Color,
    cardBg:       androidx.compose.ui.graphics.Color,
    onClick:      () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isSet) accentColor.copy(alpha = 0.4f)
                else mutedColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(if (isSet) accentColor.copy(alpha = 0.06f) else cardBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter            = painterResource(icon),
                contentDescription = null,
                tint               = accentColor,
                modifier           = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = mutedColor, fontSize = 11.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text       = selectedText,
                color      = if (isSet) primaryColor else mutedColor,
                fontSize   = 14.sp,
                fontWeight = if (isSet) FontWeight.SemiBold else FontWeight.Normal
            )
        }
        Icon(
            painter            = painterResource(R.drawable.add),
            contentDescription = null,
            tint               = mutedColor.copy(alpha = 0.4f),
            modifier           = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun WeatherChip(
    option:      WeatherOption,
    isSelected:  Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    mutedColor:  androidx.compose.ui.graphics.Color,
    cardBg:      androidx.compose.ui.graphics.Color,
    modifier:    Modifier = Modifier,
    onClick:     () -> Unit
) {
    val label = stringResource(option.labelRes)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.5.dp,
                color = if (isSelected) accentColor else mutedColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) accentColor.copy(alpha = 0.1f) else cardBg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter            = painterResource(option.iconRes),
            contentDescription = null,
            tint               = if (isSelected) accentColor else mutedColor,
            modifier           = Modifier.size(20.dp)
        )
        Text(
            text       = label,
            color      = if (isSelected) accentColor else mutedColor,
            fontSize   = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            maxLines   = 1
        )
    }
}