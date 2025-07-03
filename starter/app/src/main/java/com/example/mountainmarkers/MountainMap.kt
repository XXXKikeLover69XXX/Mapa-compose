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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.compose.clustering.Clustering
import kotlinx.coroutines.launch
import com.example.mountainmarkers.presentation.MountainsScreenViewState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MountainMap(
    paddingValues: PaddingValues,
    viewState: MountainsScreenViewState.MountainList,
    selectedMarkerType: MarkerType,
    onToggleShowMarkers: () -> Unit,
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val cersia = LatLng(42.885379650416255, -8.516780818558765)
    val santiago = LatLng(42.8782, -8.5448)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiago, 13f)
    }

    // Lista de puntos para clustering
    val clusterItems = remember {
        listOf(
            MountainClusterItem(cersia, "Edificio CERSIA", "Santiago de Compostela"),
            MountainClusterItem(LatLng(42.885, -8.517), "UAMI", "BRRR"),
            MountainClusterItem(LatLng(42.886, -8.516), "Albergue Dream", "AAAAH"),
            MountainClusterItem(LatLng(42.884, -8.514), "Domus Vi", "BBSITAA")
        )
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
            Text(text = "Mostrar markers")
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
                    // Clustering
                    Clustering(
                        items = clusterItems,
                        onClusterItemClick = { item ->
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(item.position, 17f)
                                )
                            }
                            true
                        }
                    )
                }
            }
        }
    }

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
