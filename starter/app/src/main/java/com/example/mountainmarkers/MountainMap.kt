package com.example.mountainmarkers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mountainmarkers.presentation.MountainsScreenEvent
import com.example.mountainmarkers.presentation.MountainsScreenViewState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.flow.Flow

@Composable
fun MountainMap(
    paddingValues: PaddingValues,
    viewState: MountainsScreenViewState.MountainList,
    eventFlow: Flow<MountainsScreenEvent>,
    selectedMarkerType: MarkerType,
) {
    var isMapLoaded by remember { mutableStateOf(false) }

    // Definimos la posición inicial de la cámara en Santiago (zoom medio para toda la ciudad)
    val santiago = LatLng(42.8782, -8.5448)
    val cameraPositionState = remember {
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(santiago, 13f)
        )
    }

    // Coordenadas CERSIA
    val cersia = LatLng(42.885379650416255, -8.516780818558765)

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
            // Aquí pones el marker de CERSIA
            Marker(
                state = MarkerState(position = cersia),
                title = "Edificio CERSIA",
                snippet = "Santiago de Compostela",
                onClick = {

                    cameraPositionState.position = CameraPosition(cersia, 17f, 0f, 0f)
                    true
                }
            )


        }

        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = !isMapLoaded,
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
    }
}
