package com.example.weatherapp.presentation.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.presentation.settings.components.LocationSourceCard
import com.example.weatherapp.presentation.settings.components.SettingsRadioRow
import com.example.weatherapp.presentation.settings.components.SettingsSection
import com.example.weatherapp.presentation.settings.viewmodel.SettingsViewModel
import com.example.weatherapp.ui.theme.AppTheme

@Composable
private fun tempOptions() = listOf(
    Triple("metric", stringResource(R.string.celsius), R.drawable.thermometer),
    Triple("imperial", stringResource(R.string.fahrenheit), R.drawable.thermometer),
    Triple("standard", stringResource(R.string.kelvin), R.drawable.thermometer)
)

@Composable
private fun windOptions() = listOf(
    Triple("ms", stringResource(R.string.meter_per_sec), R.drawable.air),
    Triple("mph", stringResource(R.string.miles_per_hour), R.drawable.air)
)

@Composable
private fun langOptions() = listOf(
    Triple("en", stringResource(R.string.language_english), R.drawable.language),
    Triple("ar", stringResource(R.string.language_arabic), R.drawable.language)
)

@Composable
private fun themeOptions() = listOf(
    Triple("light", stringResource(R.string.theme_light), R.drawable.lightmode),
    Triple("dark", stringResource(R.string.theme_dark), R.drawable.darkmode),
    Triple("system", stringResource(R.string.theme_system), R.drawable.contrast)
)

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val colors = AppTheme.colors
    val context = LocalContext.current
    val activity = context as MainActivity

    val locationMode by viewModel.locationMode.collectAsState()
    val manualLocation by viewModel.manualLocation.collectAsState()
    val units by viewModel.units.collectAsState()
    val windUnit by viewModel.windUnit.collectAsState()
    val language by viewModel.language.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(8.dp))


            Row {
                Text(
                    text = stringResource(R.string.settings_title_normal) + " ",
                    color = colors.textPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = stringResource(R.string.settings_title_bold),
                    color = colors.textPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            SettingsSection(title = stringResource(R.string.section_location)) {
                LocationSourceCard(
                    selectedMode = locationMode,
                    manualLocation = manualLocation,
                    onSelectGps = { viewModel.setLocationMode("gps") },
                    onSelectMap = { viewModel.setLocationMode("map") },
                    onOpenMap = { navController.navigate("map_picker/settings") }
                )
            }


            SettingsSection(title = stringResource(R.string.section_temperature_unit)) {
                OptionGroup(
                    options = tempOptions(),
                    selected = units,
                    onSelect = { viewModel.setUnits(it) }
                )
            }


            SettingsSection(title = stringResource(R.string.section_wind_unit)) {
                OptionGroup(
                    options = windOptions(),
                    selected = windUnit,
                    onSelect = { viewModel.setWindUnit(it) }
                )
            }


            SettingsSection(title = stringResource(R.string.section_language)) {
                OptionGroup(
                    options = langOptions(),
                    selected = language,
                    onSelect = { newLang ->
                        viewModel.setLanguage(newLang)
                        activity.applyLanguageAndRecreate(newLang)
                    }
                )
            }


            SettingsSection(title = stringResource(R.string.section_theme)) {
                OptionGroup(
                    options = themeOptions(),
                    selected = themeMode,
                    onSelect = { viewModel.setThemeMode(it) }
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ColumnScope.OptionGroup(
    options: List<Triple<String, String, Int>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    options.forEachIndexed { index, (value, label, iconRes) ->
        SettingsRadioRow(
            label = label,
            selected = selected == value,
            onClick = { onSelect(value) },
            iconRes = iconRes,
            showDivider = index < options.lastIndex
        )
    }
}


