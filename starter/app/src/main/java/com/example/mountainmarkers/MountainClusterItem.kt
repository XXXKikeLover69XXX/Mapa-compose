package com.example.mountainmarkers

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MountainClusterItem(
    private val latLng: LatLng,
    private val titleText: String,
    private val snippetText: String
) : ClusterItem {
    override fun getPosition(): LatLng = latLng
    override fun getTitle(): String = titleText
    override fun getSnippet(): String = snippetText
    override fun getZIndex(): Float? = 0f
}
