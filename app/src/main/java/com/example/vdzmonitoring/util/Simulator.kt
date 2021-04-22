package com.example.vdzmonitoring.util

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import kotlin.random.Random

class Simulator(
    private val route: Pair<GeoPoint, GeoPoint>,
    private val polyline: Polyline
) {

    val steps = arrayListOf<GeoPoint>()
    val delaytime = 1000L
    val stepPartition = 5 to 15

    init {
        initStep()
    }

    private fun initStep() {
        polyline.points.add(route.second)
        val geoPoints = polyline.points.toList()
        val nPoints = polyline.points.size
        steps.add(route.first)
        for (i in 0 until nPoints - 1 ) {
            val step = Random.nextInt(stepPartition.first, stepPartition.second)
            val start = geoPoints[i]
            val end = geoPoints[i + 1]
            val latStep = (end.latitude - start.latitude) / step
            val longStep = (end.longitude - start.longitude) / step
            for (s in 0..step) {
                val currentPoint =
                    GeoPoint(start.latitude + s * latStep, start.longitude + s * longStep)
                steps.add(currentPoint)
            }
        }
        steps.add(route.second)
    }
}