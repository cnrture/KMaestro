package com.canerture.kmaestro

/**
 * Enumeration of supported directional movements for scrolling and swiping.
 */
enum class Direction(val displayName: String) {
    /** Upward direction */
    UP("UP"),

    /** Downward direction */
    DOWN("DOWN"),

    /** Leftward direction */
    LEFT("LEFT"),

    /** Rightward direction */
    RIGHT("RIGHT"),
}