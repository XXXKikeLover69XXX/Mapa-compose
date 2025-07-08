package com.example.mountainmarkers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.mountainmarkers.data.utils.ImperialUnitsConverter
import com.example.mountainmarkers.data.utils.LocalUnitsConverter
import com.example.mountainmarkers.data.utils.MetricUnitsConverter
import com.example.mountainmarkers.presentation.BigSpinner
import com.example.mountainmarkers.presentation.MountainsScreenViewState.Loading
import com.example.mountainmarkers.presentation.MountainsScreenViewState.MountainList
import com.example.mountainmarkers.presentation.MountainsViewModel
import com.example.mountainmarkers.presentation.MountainsViewModelEvent
import com.example.mountainmarkers.ui.theme.MountainMarkersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val unitsConverter = if (LocalConfiguration.current.locales.get(0).country == "US") {
                ImperialUnitsConverter
            } else {
                MetricUnitsConverter
            }

            CompositionLocalProvider(
                LocalUnitsConverter provides unitsConverter
            ) {
                val viewModel: MountainsViewModel by viewModels()
                val screenViewState = viewModel.mountainsScreenViewState.collectAsState()
                val viewState = screenViewState.value

                val showAllChecked =
                    when (viewState) {
                        Loading -> false
                        is MountainList -> viewState.showingAllPeaks
                    }

                MountainMarkersTheme(
                    dynamicColor = false
                ) {
                    // Remember the type of marker we want to show
                    var selectedMarkerType by rememberSaveable {
                        mutableStateOf(MarkerType.Basic)
                    }

                    Scaffold(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                        topBar = {
                            MountainMapTopBar(
                                topBarTitleStringRes = selectedMarkerType.title,
                                showAllChecked = showAllChecked,
                                onZoomAllClick = {
                                    viewModel.onEvent(MountainsViewModelEvent.OnZoomAll)
                                },
                                onToggleShowAllClick = {
                                    viewModel.onEvent(MountainsViewModelEvent.OnToggleAllPeaks)
                                }
                            )
                        },
                        bottomBar = { BottomNav(selectedMarkerType) { selectedMarkerType = it } }
                    ) { paddingValues ->
                        when (viewState) {
                            Loading -> BigSpinner(paddingValues)

                            is MountainList -> {
                                MountainMap(
                                    paddingValues = paddingValues,
                                    viewState = viewState,

                                    onToggleShowMarkers = {
                                        viewModel.onEvent(MountainsViewModelEvent.OnToggleAllPeaks)
                                    }
                                )
                            }
                            }
                        }
                    }
                }
            }
        }
    }
