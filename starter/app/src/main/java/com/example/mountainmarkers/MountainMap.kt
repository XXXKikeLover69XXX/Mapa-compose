package com.example.mountainmarkers

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.compose.clustering.Clustering
import com.example.mountainmarkers.presentation.MountainsScreenViewState
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MountainMap(
    paddingValues: PaddingValues,
    viewState: MountainsScreenViewState.MountainList,
    onToggleShowMarkers: () -> Unit,
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf<MountainClusterItem?>(null) }

    // Marcadores
    val iARCUS = LatLng(42.87434584313793, -8.561456786566236)
    val iMATUS = LatLng(42.87287787590329, -8.558428816370364)
    val iGFAE = LatLng(42.877518466443064, -8.558955945205511)
    val iCE = LatLng(42.876456234085246, -8.552803545205595)
    val iLGA = LatLng(42.87886807391224, -8.542679731711427)
    val cIQUS = LatLng(42.87312610094581, -8.557557674040995)
    val cIMUS = LatLng(42.871199716306094, -8.561833287535125)
    val iDIS = LatLng(42.870423239108426, -8.565678560547143)
    val cRETUS = LatLng(42.87450611079023, -8.560144675888262)
    val iDEGA = LatLng(42.87628834421211, -8.552506345205597)
    val iPSIUS = LatLng(42.875165708857516, -8.561098202876266)
    val iLTIUS = LatLng(42.89152643732697, -8.5452758219116)
    val cESEG = LatLng(42.87475183464985, -8.553600158699684)
    val iNCIFOR = LatLng(42.882508525754034, -8.546514204723271)
    val cITIUS = LatLng(42.87354426452495, -8.557769833558977)
    val santiago = LatLng(42.8782, -8.5448)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiago, 11f)
    }

    val clusterItems = remember {
        listOf(
            MountainClusterItem(iARCUS, "IARCUS", "Santiago de Compostela"),
            MountainClusterItem(iMATUS, "IMATUS", "Santiago de Compostela"),
            MountainClusterItem(iGFAE, "IGFAE", "Santiago de Compostela"),
            MountainClusterItem(iCE, "ICE", "Santiago de Compostela"),
            MountainClusterItem(iLGA, "ILGA", "Santiago de Compostela"),
            MountainClusterItem(cIQUS, "CIQUS", "Santiago de Compostela"),
            MountainClusterItem(cIMUS, "CIMUS", "Santiago de Compostela"),
            MountainClusterItem(iDIS, "IDIS", "Santiago de Compostela"),
            MountainClusterItem(cRETUS, "CRETUS", "Santiago de Compostela"),
            MountainClusterItem(iDEGA, "IDEGA", "Santiago de Compostela"),
            MountainClusterItem(iPSIUS, "IPSIUS", "Santiago de Compostela"),
            MountainClusterItem(iLTIUS, "ILTIUS", "Santiago de Compostela"),
            MountainClusterItem(cESEG, "CESEG", "Santiago de Compostela"),
            MountainClusterItem(iNCIFOR, "INCIFOR", "Santiago de Compostela"),
            MountainClusterItem(cITIUS, "CITIUS", "Santiago de Compostela"),
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
                    Clustering(
                        items = clusterItems,
                        onClusterItemClick = { item ->
                            selectedItem = item
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

            if (selectedItem != null) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .height(56.dp)
                        .fillMaxWidth(0.4f),
                    onClick = {
                        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${selectedItem!!.position.latitude},${selectedItem!!.position.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Cómo llegar")
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

