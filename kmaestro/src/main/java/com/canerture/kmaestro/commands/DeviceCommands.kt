package com.canerture.kmaestro.commands

import com.canerture.kmaestro.KeyType

internal class DeviceCommands(private val commands: MutableList<String>) {

    /**
     * Enables or disables airplane mode on the device.
     * This affects network connectivity for testing offline scenarios.
     *
     * @param enabled Whether to enable (true) or disable (false) airplane mode
     */
    fun setAirplaneMode(enabled: Boolean) = commands.add("- setAirplaneMode: $enabled")

    /**
     * Toggles airplane mode (enables if disabled, disables if enabled).
     * Useful when you need to switch airplane mode state without knowing current state.
     */
    fun toggleAirplaneMode() = commands.add("- toggleAirplaneMode")

    /**
     * Sets the device's GPS location coordinates.
     * Useful for testing location-based features.
     *
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     */
    fun setLocation(latitude: Double, longitude: Double) {
        commands.add("- setLocation:")
        commands.add("    latitude: $latitude")
        commands.add("    longitude: $longitude")
    }

    /**
     * Simulates traveling between GPS coordinates at a specified speed.
     * Useful for testing location-based features with movement simulation.
     *
     * @param points List of latitude/longitude coordinate pairs representing the travel route
     * @param speed Travel speed in meters per second (default: 7900)
     * @throws IllegalArgumentException if points list is empty
     */
    fun travel(points: List<Pair<Double, Double>>, speed: Int = 7900) {
        require(points.isNotEmpty()) { "Points must not be empty." }
        commands.add("- travel:")
        commands.add("    points:")
        points.forEach { (latitude, longitude) ->
            commands.add("      - $latitude,$longitude")
        }
        commands.add("    speed: $speed")
    }

    /**
     * Simulates pressing a physical or virtual key on the device.
     * Supports system keys like volume, home, back, etc.
     *
     * @param key The key to press (from KeyType enum)
     */
    fun pressKey(key: KeyType) = commands.add("- pressKey: ${key.displayName}")

    /**
     * Adds media files (images, videos) to the device's media library.
     * Useful for testing features that require access to photos or videos.
     *
     * @param path Variable number of file paths to add to the media library
     */
    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

    /**
     * Opens a URL link in the default browser or associated app.
     * Can be configured to force browser usage or auto-verify links.
     *
     * @param url The URL to open
     * @param autoVerify Whether to automatically verify the URL (default: null)
     * @param browser Whether to force opening in browser instead of associated app (default: null)
     * @throws IllegalArgumentException if url is empty
     */
    fun openLink(url: String, autoVerify: Boolean? = null, browser: Boolean? = null) {
        require(url.isNotEmpty()) { "URL must not be empty." }
        val openCommand = StringBuilder("- openLink:")

        if (autoVerify == null && browser == null) {
            openCommand.append(" \"$url\"")
        } else {
            openCommand.append("\n    link: \"$url\"")
            autoVerify?.let { openCommand.append("\n    autoVerify: $it") }
            browser?.let { openCommand.append("\n    browser: $it") }
        }

        commands.add(openCommand.toString())
    }

    /**
     * Takes a screenshot of the current screen.
     * Useful for debugging or creating visual documentation of test runs.
     *
     * @param fileName The name of the screenshot file (default: "screenshot")
     */
    fun takeScreenshot(fileName: String = "screenshot") =
        commands.add("- takeScreenshot: \"$fileName\"")
}