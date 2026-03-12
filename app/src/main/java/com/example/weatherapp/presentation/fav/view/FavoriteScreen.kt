package com.example.weatherapp.presentation.fav.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.presentation.fav.components.FavEmptyState
import com.example.weatherapp.presentation.fav.components.FavoriteCard
import com.example.weatherapp.presentation.fav.components.SwipeToDeleteWrapper
import com.example.weatherapp.presentation.fav.viewmodel.FavoriteViewModel
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    navController: NavController
) {
    val colors = AppTheme.colors
    val favorites by viewModel.favorites.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Row {
                Text(
                    text = "My ",
                    color = colors.textPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Favorites",
                    color = colors.textPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${favorites.size} saved location${if (favorites.size != 1) "s" else ""}",
                color = colors.textMuted,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))

            if (favorites.isEmpty()) {
                FavEmptyState()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(items = favorites, key = { it.id }) { location ->
                        SwipeToDeleteWrapper(
                            onDelete = { viewModel.removeFavorite(location) }
                        ) {
                            FavoriteCard(
                                location = location,
                                onClick = {
                                    navController.navigate("forecast_detail/${location.id}")
                                }
                            )
                        }
                    }
                }
            }
        }


        FloatingActionButton(
            onClick = { navController.navigate("map_picker/favorite") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp),
            containerColor = colors.accent,
            contentColor = colors.textPrimary,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = "Add favorite",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}