package com.example.vdzmonitoring.util

import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.ui.routing.VDZObserver
import com.example.vdzmonitoring.ui.routing.VDZ_STATUS_INSIDE_VDZ
import com.example.vdzmonitoring.ui.routing.VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline

fun generateVDZList(vdzs: List<VDZObserver>) =
    vdzs.map{
        generateVDZ(it)
    }

fun generateVDZ(vdz: VDZObserver) =
    generatePoint(
        vdz.vdz,
        VDZ_RADIUS_IN_METER,
        VDZ_FILL_COLOR,
        when(vdz.status) {
            VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ -> VDZ_STROKE_COLOR_OUTSIDE_NOT_PASS
            VDZ_STATUS_INSIDE_VDZ -> VDZ_STROKE_COLOR_INSIDE_VDZ
            else -> VDZ_STROKE_COLOR_OUTSIDE_PASS_VDZ
        },
        VDZ_STROKE_WIDTH,
        "VDZ${vdz.id}"
    )


private fun generatePoint(
    point: GeoPoint,
    radius: Double,
    fillColor: Int,
    strokeColor: Int,
    strokeWidth: Float,
    pointTitle: String = ""
) = Polygon().apply {
        points = Polygon.pointsAsCircle(point, radius)
        fillPaint.color = fillColor
        outlinePaint.color = strokeColor
        outlinePaint.strokeWidth = strokeWidth
        title = pointTitle
    }


fun MapView.generateMyPositionMarker(point: GeoPoint) =
    Marker(this).apply {
        position = point
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        icon = resources.getDrawableForDensity(R.drawable.ic_directions_car_black_48dp, 0, null)
        title =  TAG_MY_LOCATION
    }

fun generateRoad(road: Polyline) = road.apply {
    title = TAG_MY_ROUTE
    outlinePaint.strokeWidth = ROAD_STROKE_WIDTH
}