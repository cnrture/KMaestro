package com.canerture.kmaestro.commands

import com.canerture.kmaestro.Permission
import com.canerture.kmaestro.PermissionState

internal class AppCommands(private val commands: MutableList<String>) {

    /**
     * Launches an app with optional configuration parameters.
     * This is typically the first command in a Maestro flow.
     *
     * @param appId The bundle ID (iOS) or package name (Android) of the app to launch. If null, launches the default app
     * @param clearState Whether to clear the app's state before launching (default: false)
     * @param clearKeychain Whether to clear the iOS keychain before launching (default: false)
     * @param stopApp Whether to stop the app before launching it (default: true)
     * @param permissions Map of permissions to set for the app (e.g., "notifications" to "deny")
     * @param arguments Launch arguments to pass to the app as key-value pairs
     */
    fun launchApp(
        appId: String? = null,
        clearState: Boolean = false,
        clearKeychain: Boolean = false,
        stopApp: Boolean = true,
        permissions: Map<Permission, PermissionState>? = null,
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
                    launchCommand.append("\n      ${key.value}: \"${value.value}\"")
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

    /**
     * Terminates/kills an app completely.
     * This forcefully stops the app and removes it from memory.
     *
     * @param appId The app ID to kill. If null, kills the current app
     */
    fun killApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- killApp")
        } else {
            commands.add("- killApp: \"$appId\"")
        }
    }

    /**
     * Stops/closes an app (but doesn't kill it completely).
     * The app remains in memory and can be resumed.
     *
     * @param appId The app ID to stop. If null, stops the current app
     */
    fun stopApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- stopApp")
        } else {
            commands.add("- stopApp: \"$appId\"")
        }
    }

    /**
     * Clears the app's state, including preferences, databases, and cached data.
     * This resets the app to its initial state as if it was just installed.
     *
     * @param appId The app ID to clear state for. If null, clears state for the current app
     */
    fun clearState(appId: String? = null) {
        if (appId == null) {
            commands.add("- clearState")
        } else {
            commands.add("- clearState: \"$appId\"")
        }
    }

    /**
     * Clears the iOS keychain. This is iOS-specific and has no effect on Android.
     * Removes all stored credentials and security tokens.
     */
    fun clearKeychain() = commands.add("- clearKeychain")
}