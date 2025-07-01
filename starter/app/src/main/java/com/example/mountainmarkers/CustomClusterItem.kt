package com.example.mountainmarkers

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class CustomClusterItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String
) : ClusterItem {

    override fun getPosition(): LatLng = position
    override fun getTitle(): String = title
    override fun getSnippet(): String = snippet

    // Requerido por versiones recientes de la librería
    override fun getZIndex(): Float? = null
}
