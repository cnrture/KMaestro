package com.canerture.kmaestro

internal class WaitCommands(private val commands: MutableList<String>) {

    fun waitForAnimationToEnd(timeout: Int = 5000) =
        commands.add("- waitForAnimationToEnd:\n    timeout: $timeout")

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