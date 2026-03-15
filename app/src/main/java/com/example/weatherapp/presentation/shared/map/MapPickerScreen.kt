package com.example.weatherapp.presentation.shared.map

import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.network.LocationNetwork
import com.example.weatherapp.data.weather.model.LocationResult
import com.example.weatherapp.presentation.shared.map.components.MapSearchBar
import com.example.weatherapp.presentation.shared.map.components.NoSelectionButton
import com.example.weatherapp.presentation.shared.map.components.OsmMapView
import com.example.weatherapp.presentation.shared.map.components.SelectedLocationCard
import com.example.weatherapp.presentation.shared.map.components.placeMarker
import com.example.weatherapp.ui.theme.AppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

enum class MapPickerSource { FAVORITE, SETTINGS }

@Composable
fun MapPickerScreen(
    source: MapPickerSource = MapPickerSource.FAVORITE,
    onLocationPicked: (lat: Double, lon: Double, city: String, country: String) -> Unit,
    navController: NavController
) {
    val colors = AppTheme.colors
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<LocationResult>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var searchError by remember { mutableStateOf("") }
    var searchJob by remember { mutableStateOf<Job?>(null) }

    var selectedLat by remember { mutableStateOf(0.0) }
    var selectedLon by remember { mutableStateOf(0.0) }
    var selectedCity by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var hasSelection by remember { mutableStateOf(false) }

    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var currentMarker by remember { mutableStateOf<Marker?>(null) }

    fun updateMarker(geo: GeoPoint, title: String) {
        val mv = mapViewRef ?: return
        currentMarker = mv.placeMarker(geo, title, currentMarker)
        selectedLat = geo.latitude
        selectedLon = geo.longitude
        hasSelection = true
    }

    suspend fun doSearch(query: String) {
        if (query.isBlank()) return
        isSearching = true
        searchError = ""
        try {
            val results = LocationNetwork.service.searchCity(query)
            searchResults = results
            showSuggestions = results.isNotEmpty()
            if (results.isEmpty()) searchError = "No results for \"$query\""
        } catch (e: Exception) {
            searchError = "Search failed. Check your connection."
            searchResults = emptyList()
        } finally {
            isSearching = false
        }
    }

    fun onSuggestionSelected(result: LocationResult) {
        val geo = GeoPoint(result.lat.toDouble(), result.lon.toDouble())
        selectedCity = result.address?.resolvedCity
            ?: result.displayName.split(",").first().trim()
        selectedCountry = result.address?.countryCode?.uppercase() ?: ""
        searchQuery = selectedCity
        showSuggestions = false
        focusManager.clearFocus()
        updateMarker(geo, selectedCity)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {

        OsmMapView(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { mapViewRef = it },
            onLongPress = { geo ->
                updateMarker(geo, "Loading…")
                scope.launch {
                    try {
                        val result = LocationNetwork.service.reverseGeocode(
                            geo.latitude, geo.longitude
                        )
                        selectedCity = result.address?.resolvedCity
                            ?: result.displayName.split(",").first().trim()
                        selectedCountry = result.address?.countryCode?.uppercase() ?: ""
                        currentMarker?.title = selectedCity
                        mapViewRef?.invalidate()
                    } catch (_: Exception) {
                        selectedCity = "Lat ${String.format("%.2f", geo.latitude)}"
                        currentMarker?.title = selectedCity
                        mapViewRef?.invalidate()
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.cardBg.copy(alpha = 0.92f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = colors.textPrimary
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(

                    text = when (source) {
                        MapPickerSource.FAVORITE -> stringResource(R.string.map_title_favorite)
                        MapPickerSource.SETTINGS -> stringResource(R.string.map_title_settings)
                    },
                    color = colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(10.dp))

            MapSearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    searchError = ""
                    showSuggestions = false
                    searchJob?.cancel()
                    if (query.length >= 2) {
                        searchJob = scope.launch { delay(600); doSearch(query) }
                    } else {
                        searchResults = emptyList()
                    }
                },
                onSearch = {
                    focusManager.clearFocus()
                    searchJob?.cancel()
                    scope.launch { doSearch(searchQuery) }
                },
                isSearching = isSearching,
                searchError = searchError,
                searchResults = searchResults,
                showSuggestions = showSuggestions,
                onSuggestionClick = { onSuggestionSelected(it) }
            )
        }


        if (!hasSelection) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.cardBg.copy(alpha = 0.85f))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text =stringResource(R.string.map_no_selection),
                    color = colors.textMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            if (hasSelection) {
                SelectedLocationCard(
                    cityName = selectedCity,
                    lat = selectedLat,
                    lon = selectedLon,
                    onSave = {
                        onLocationPicked(
                            selectedLat,
                            selectedLon,
                            selectedCity.ifEmpty { "Unknown" },
                            selectedCountry
                        )
                        navController.popBackStack()
                    }
                )
            } else {
                NoSelectionButton()
            }
        }
    }
}