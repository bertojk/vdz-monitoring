package com.example.vdzmonitoring.util

import android.graphics.Color

// This class contains every constant values for Application


// VDZ PASS
// For determine the tolerance of passed radius value when User has passed a point in meter
const val VDZ_HAS_PASSED_TOLERANCE_IN_METER = 5.0
// =============================================================================================

// VDZ APPEARANCE SETTING
// For VDZ appearance on Map

// radius of VDZ in meter
const val VDZ_RADIUS_IN_METER = 50.0

// fill color of VDZ
const val VDZ_FILL_COLOR = Color.TRANSPARENT

// stroke color of VDZ when User has not passed the VDZ
const val VDZ_STROKE_COLOR_OUTSIDE_NOT_PASS = Color.GREEN

// stroke color of VDZ when User is inside the VDZ
const val VDZ_STROKE_COLOR_INSIDE_VDZ = Color.YELLOW

// stroke color of VDZ when User has passed the VDZ
const val VDZ_STROKE_COLOR_OUTSIDE_PASS_VDZ = Color.RED

// stroke width of VDZ
const val VDZ_STROKE_WIDTH = 5F

const val TAG_VDZ = "VDZ"
// =============================================================================================

// MY POINT APPEARANCE SETTING
// For User appearance on Map

// radius of User in meter
const val MY_POINT_RADIUS_IN_METER = 5.0

// fill color of User
const val MY_POINT_FILL_COLOR = Color.GREEN

// stroke color of User
const val MY_POINT_STROKE_COLOR = Color.GREEN

// stroke width of User
const val MY_POINT_STROKE_WIDTH = 5F

// string tag of User
const val TAG_MY_LOCATION = "MyLocation"
// =============================================================================================

// TERMINAL APPEARANCE SETTING
// For source and destination appearance on Map

// radius of terminal in meter
const val TERMINAL_RADIUS_IN_METER = 10.0

// fill color of terminal
const val TERMINAL_FILL_COLOR = Color.TRANSPARENT

// stroke color of terminal
const val TERMINAL_STROKE_COLOR = Color.RED

// stroke width of terminal
const val TERMINAL_STROKE_WIDTH = 10F
// =============================================================================================


const val ROAD_STROKE_WIDTH = 5F

const val TAG_MY_ROUTE = "MyRoute"

const val TAG_ROUTE_ID_SELECTED = "RouteIdSelected"

const val TAG_CURRENT_ROUTE_LOG_ID = "CurrentRouteLogId"


