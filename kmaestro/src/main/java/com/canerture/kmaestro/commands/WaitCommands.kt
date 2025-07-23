package com.canerture.kmaestro.commands

internal class WaitCommands(private val commands: MutableList<String>) {

    /**
     * Waits for all animations to complete before proceeding.
     * Essential for stable tests when dealing with animated UI elements.
     *
     * @param timeout Maximum time to wait for animations to end in milliseconds (default: 5000)
     */
    fun waitForAnimationToEnd(timeout: Int = 5000) =
        commands.add("- waitForAnimationToEnd:\n    timeout: $timeout")

    /**
     * Waits until specified conditions are met with advanced options.
     * This is more flexible than simple assertions as it allows multiple conditions.
     *
     * @param visible Map of element properties that should be visible
     * @param notVisible Map of element properties that should not be visible
     * @param timeout Maximum time to wait in milliseconds (default: 10000)
     */
    fun extendedWaitUntil(
        visible: Map<String, Any>? = null,
        notVisible: Map<String, Any>? = null,
        timeout: Int = 10000
    ) {
        val waitCommand = StringBuilder("- extendedWaitUntil:\n")
        waitCommand.append("    timeout: $timeout\n")

        visible?.let {
            waitCommand.append("    visible:\n")
            it.forEach { (key, value) ->
                val formattedValue = if (value is String) "\"$value\"" else value.toString()
                waitCommand.append("      $key: $formattedValue\n")
            }
        }

        notVisible?.let {
            waitCommand.append("    notVisible:\n")
            it.forEach { (key, value) ->
                val formattedValue = if (value is String) "\"$value\"" else value.toString()
                waitCommand.append("      $key: $formattedValue\n")
            }
        }

        commands.add(waitCommand.toString().trimEnd())
    }
}