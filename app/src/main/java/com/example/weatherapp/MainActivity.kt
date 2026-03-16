package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.network.WeatherNetwork
import com.example.weatherapp.data.weather.AppRepositoryImpl
import com.example.weatherapp.data.weather.datasource.local.AppDatabase
import com.example.weatherapp.data.weather.datasource.local.LocalDataSourceImpl
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.navigation.AppNavHost
import com.example.weatherapp.presentation.settings.viewmodel.SettingsFactory
import com.example.weatherapp.presentation.settings.viewmodel.SettingsViewModel
import com.example.weatherapp.presentation.shared.BottomNavBar
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.LocaleHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val repository by lazy {
        AppRepositoryImpl(
            remoteDataSource = WeatherRemoteDataSourceImpl(WeatherNetwork.weatherService),
            localDataSource = LocalDataSourceImpl(
                context = applicationContext,
                favoriteDao = AppDatabase.getInstance(applicationContext).favoriteDao(),
                alertDao =  AppDatabase.getInstance(applicationContext).alertDao()
            )
        )
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsFactory(repository)
    }

    override fun attachBaseContext(newBase: Context) {
        val langCode = runBlocking {
            LocalDataSourceImpl(
                context = newBase,
                favoriteDao = AppDatabase.getInstance(newBase).favoriteDao(),
                alertDao = AppDatabase.getInstance(newBase).alertDao()
            ).getLanguage().first()
        }
        super.attachBaseContext(LocaleHelper.applyLocale(newBase, langCode))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val themeMode by settingsViewModel.themeMode.collectAsState()
            val systemIsDark = isSystemInDarkTheme()

            val isDark = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> systemIsDark
            }
            WeatherAppTheme(darkTheme = isDark) {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        repository = repository,
                        settingsViewModel = settingsViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }

    fun applyLanguageAndRecreate(langCode: String) {
        LocaleHelper.applyLocale(this, langCode)
        recreate()
    }
}

