package com.example.weatherapp.presentation.shared.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.weather.model.LocationResult
import com.example.weatherapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isSearching: Boolean,
    searchError: String,
    searchResults: List<LocationResult>,
    showSuggestions: Boolean,
    onSuggestionClick: (LocationResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { onSearch() },
                expanded = showSuggestions,
                onExpandedChange = {},
                placeholder = {
                    Text(
                        text = stringResource(com.example.weatherapp.R.string.search_for_a_city),
                        color = colors.textMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(com.example.weatherapp.R.string.search),
                        tint = colors.textMuted
                    )
                },
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = colors.accent
                        )
                    }
                }
            )
        },
        expanded = showSuggestions,
        onExpandedChange = {},
        modifier = modifier.fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = colors.cardBg,
            dividerColor = colors.cardBorder
        )
    ) {
        if (searchError.isNotEmpty()) {
            Text(
                text = searchError,
                color = colors.danger,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyColumn(modifier = Modifier.heightIn(max = 260.dp)) {
            items(searchResults) { result ->
                val city = result.address?.resolvedCity
                    ?: result.displayName.split(",").first().trim()

                ListItem(
                    headlineContent = {
                        Text(
                            text = city,
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = result.displayName,
                            color = colors.textMuted,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = colors.cardBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(result) }
                )

                HorizontalDivider(color = colors.cardBorder, thickness = 0.5.dp)
            }
        }
    }
}