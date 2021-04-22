package com.example.vdzmonitoring.ui.routing

import com.example.vdzmonitoring.data.entities.VDZ
import com.example.vdzmonitoring.util.VDZ_HAS_PASSED_TOLERANCE_IN_METER
import com.example.vdzmonitoring.util.VDZ_RADIUS_IN_METER
import org.osmdroid.util.GeoPoint

const val VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ = 0
const val VDZ_STATUS_INSIDE_VDZ = 1
const val VDZ_STATUS_OUTSIDE_PASS_VDZ = 2

// VDZ PASS
// For determine the tolerance of passed radius value when User has passed a point in meter


class VDZObserver(
    vdz: VDZ
) {
    val id = vdz.vdzID
    val vdz = GeoPoint(vdz.latitude, vdz.longitude)
    var distance: Double = 0.0
    var status = VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ

    fun updateVDZ(myPoint: GeoPoint) {
        distance = vdz.distanceToAsDouble(myPoint)

        status = when(isInsideCircle()) {
            true -> when(status) {
                VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ -> VDZ_STATUS_INSIDE_VDZ
                VDZ_STATUS_INSIDE_VDZ -> when(isPassedCenterOfCircle()) {
                    true -> VDZ_STATUS_OUTSIDE_PASS_VDZ
                    else -> VDZ_STATUS_INSIDE_VDZ
                }
                else -> VDZ_STATUS_OUTSIDE_PASS_VDZ
            }
            else -> when(status == VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ) {
                true -> VDZ_STATUS_OUTSIDE_NOT_PASS_VDZ
                false -> VDZ_STATUS_OUTSIDE_PASS_VDZ
            }
        }
    }

    private fun isInsideCircle() = distance <= VDZ_RADIUS_IN_METER

    private fun isPassedCenterOfCircle () = distance <= VDZ_HAS_PASSED_TOLERANCE_IN_METER

}