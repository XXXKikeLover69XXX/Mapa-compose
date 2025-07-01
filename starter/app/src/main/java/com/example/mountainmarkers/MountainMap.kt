package com.example.mountainmarkers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import com.example.mountainmarkers.presentation.MountainsScreenViewState


@Composable
fun MountainMap(
    paddingValues: PaddingValues,
    viewState: MountainsScreenViewState.MountainList,
    onToggleShowMarkers: () -> Unit,
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val santiagoCentro = LatLng(42.8782, -8.5448)
    val cersia = LatLng(42.885379650416255, -8.516780818558765)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiagoCentro, 12.5f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Mostrar marker CERSIA")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = viewState.showingAllPeaks,
                onCheckedChange = { onToggleShowMarkers() }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true }
            ) {
                if (viewState.showingAllPeaks) {
                    Marker(
                        state = MarkerState(position = cersia),
                        title = "Edificio CERSIA",
                        snippet = "Santiago de Compostela",
                        onClick = {
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(cersia, 17f)
                                )
                            }
                            true
                        }
                    )
                }
            }
        }
    }

    // AnimatedVisibility fuera del Column, sobre el contenido completo
    AnimatedVisibility(
        visible = !isMapLoaded,
        modifier = Modifier.fillMaxSize(),
        enter = EnterTransition.None,
        exit = fadeOut()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
        )
    }
}
