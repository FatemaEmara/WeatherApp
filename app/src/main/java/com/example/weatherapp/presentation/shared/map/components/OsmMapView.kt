package com.example.weatherapp.presentation.shared.map.components


import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    onMapReady: (MapView) -> Unit,
    onLongPress: (GeoPoint) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(5.0)
                controller.setCenter(GeoPoint(30.0, 31.0))

                overlays.add(object : Overlay() {
                    override fun onLongPress(
                        e: MotionEvent?,
                        mapView: MapView?
                    ): Boolean {
                        e ?: return false
                        mapView ?: return false
                        val geo = mapView.projection
                            .fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                        onLongPress(geo)
                        return true
                    }
                })

                onMapReady(this)
            }
        }
    )
}

fun MapView.placeMarker(
    geoPoint: GeoPoint,
    title: String,
    currentMarker: Marker?
): Marker {
    currentMarker?.let { overlays.remove(it) }
    val marker = Marker(this).apply {
        position = geoPoint
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        this.title = title
    }
    overlays.add(marker)
    controller.animateTo(geoPoint)
    controller.setZoom(10.0)
    invalidate()
    return marker
}