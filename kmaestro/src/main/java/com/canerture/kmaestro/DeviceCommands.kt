package com.canerture.kmaestro

internal class DeviceCommands(private val commands: MutableList<String>) {

    fun setAirplaneMode(enabled: Boolean) = commands.add("- setAirplaneMode: $enabled")

    fun toggleAirplaneMode() = commands.add("- toggleAirplaneMode")

    fun setLocation(latitude: Double, longitude: Double) {
        commands.add("- setLocation:")
        commands.add("    latitude: $latitude")
        commands.add("    longitude: $longitude")
    }

    fun travel(points: List<Pair<Double, Double>>, speed: Int = 7900) {
        require(points.isNotEmpty()) { "Points must not be empty." }
        commands.add("- travel:")
        commands.add("    points:")
        points.forEach { (latitude, longitude) ->
            commands.add("      - $latitude,$longitude")
        }
        commands.add("    speed: $speed")
    }

    fun pressKey(key: KeyType) = commands.add("- pressKey: ${key.displayName}")

    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

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

    fun takeScreenshot(fileName: String = "screenshot") =
        commands.add("- takeScreenshot: \"$fileName\"")
}