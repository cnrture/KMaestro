package com.canerture.kmaestro

internal class AppCommands(private val commands: MutableList<String>) {

    fun launchApp(
        appId: String? = null,
        clearState: Boolean = false,
        clearKeychain: Boolean = false,
        stopApp: Boolean = true,
        permissions: Map<String, String>? = null,
        arguments: Map<String, Any> = emptyMap(),
    ) {
        val launchCommand = StringBuilder("- launchApp")

        if (appId != null || clearState || clearKeychain || !stopApp || permissions != null || arguments.isNotEmpty()) {
            launchCommand.append(":")
            appId?.let { launchCommand.append("\n    appId: \"$it\"") }
            if (clearState) launchCommand.append("\n    clearState: true")
            if (clearKeychain) launchCommand.append("\n    clearKeychain: true")
            if (!stopApp) launchCommand.append("\n    stopApp: false")

            permissions?.let { perms ->
                launchCommand.append("\n    permissions:")
                perms.forEach { (key, value) ->
                    launchCommand.append("\n      $key: \"$value\"")
                }
            }

            if (arguments.isNotEmpty()) {
                require(arguments.values.none { it !is String && it !is Boolean && it !is Double && it !is Int }) {
                    "Arguments must be of type String, Boolean, Double, or Integer."
                }
                launchCommand.append("\n    arguments:")
                arguments.forEach { (key, value) ->
                    val formattedValue = when (value) {
                        is String -> "\"$value\""
                        else -> value.toString()
                    }
                    launchCommand.append("\n      $key: $formattedValue")
                }
            }
        }

        commands.add(launchCommand.toString())
    }

    fun killApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- killApp")
        } else {
            commands.add("- killApp: \"$appId\"")
        }
    }

    fun stopApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- stopApp")
        } else {
            commands.add("- stopApp: \"$appId\"")
        }
    }

    fun clearState(appId: String? = null) {
        if (appId == null) {
            commands.add("- clearState")
        } else {
            commands.add("- clearState: \"$appId\"")
        }
    }

    fun clearKeychain() = commands.add("- clearKeychain")
}