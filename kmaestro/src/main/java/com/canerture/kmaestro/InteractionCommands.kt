package com.canerture.kmaestro

internal class InteractionCommands(private val commands: MutableList<String>) {

    fun tapOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        index: Int? = null,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null,
        longPressOn: Boolean = false
    ) {
        val tapCommand = StringBuilder("- tapOn:")

        when {
            text != null && id == null && point == null -> tapCommand.append(" \"$text\"")
            else -> {
                tapCommand.append("\n")
                text?.let { tapCommand.append("    text: \"$it\"\n") }
                id?.let { tapCommand.append("    id: \"$it\"\n") }
                point?.let { tapCommand.append("    point: $it\n") }
                index?.let { tapCommand.append("    index: $it\n") }
                if (repeat > 1) {
                    tapCommand.append("    repeat: $repeat\n")
                    tapCommand.append("    delay: $delay\n")
                }
                if (retryTapIfNoChange) tapCommand.append("    retryTapIfNoChange: true\n")
                waitToSettleTimeoutMs?.let { tapCommand.append("    waitToSettleTimeoutMs: $it\n") }
                if (longPressOn) tapCommand.append("    longPressOn: true\n")
            }
        }

        commands.add(tapCommand.toString().trimEnd())
    }

    fun doubleTapOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) {
        val tapCommand = StringBuilder("- doubleTapOn:")

        when {
            text != null && id == null && point == null -> tapCommand.append(" \"$text\"")
            else -> {
                tapCommand.append("\n")
                text?.let { tapCommand.append("    text: \"$it\"\n") }
                id?.let { tapCommand.append("    id: \"$it\"\n") }
                point?.let { tapCommand.append("    point: $it\n") }
                tapCommand.append("    delay: $delay\n")
                if (retryTapIfNoChange) tapCommand.append("    retryTapIfNoChange: true\n")
                waitToSettleTimeoutMs?.let { tapCommand.append("    waitToSettleTimeoutMs: $it\n") }
            }
        }

        commands.add(tapCommand.toString().trimEnd())
    }

    fun longPressOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) {
        val tapCommand = StringBuilder("- longPressOn:")

        when {
            text != null && id == null && point == null -> tapCommand.append(" \"$text\"")
            else -> {
                tapCommand.append("\n")
                text?.let { tapCommand.append("    text: \"$it\"\n") }
                id?.let { tapCommand.append("    id: \"$it\"\n") }
                point?.let { tapCommand.append("    point: $it\n") }
                if (repeat > 1) {
                    tapCommand.append("    repeat: $repeat\n")
                    tapCommand.append("    delay: $delay\n")
                }
                if (retryTapIfNoChange) tapCommand.append("    retryTapIfNoChange: true\n")
                waitToSettleTimeoutMs?.let { tapCommand.append("    waitToSettleTimeoutMs: $it\n") }
            }
        }

        commands.add(tapCommand.toString().trimEnd())
    }

    fun swipe(
        startX: String? = null,
        startY: String? = null,
        endX: String? = null,
        endY: String? = null,
        direction: Direction? = null,
        from: Map<String, String>? = null,
        duration: Int = 400
    ) {
        commands.add("- swipe:")

        if (direction != null) {
            commands.add("    direction: ${direction.displayName}")
        } else if (startX != null && startY != null && endX != null && endY != null) {
            commands.add("    start: $startX, $startY")
            commands.add("    end: $endX, $endY")
        }

        from?.let {
            commands.add("    from:")
            it.forEach { (key, value) ->
                commands.add("      $key: \"$value\"")
            }
        }

        commands.add("    duration: $duration")
    }

    fun scroll(direction: Direction = Direction.DOWN) {
        commands.add("- scroll:")
        commands.add("    direction: ${direction.displayName}")
    }

    fun scrollUntilVisible(
        text: String? = null,
        id: String? = null,
        direction: Direction = Direction.DOWN,
        timeout: Int = 20000,
        speed: Int = 40,
        visibilityPercentage: Int = 100,
        centerElement: Boolean = false,
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }

        commands.add("- scrollUntilVisible:")
        commands.add("    element:")
        text?.let { commands.add("      text: \"$it\"") }
        id?.let { commands.add("      id: \"$it\"") }
        commands.add("    direction: ${direction.displayName}")
        commands.add("    timeout: $timeout")
        commands.add("    speed: $speed")
        commands.add("    visibilityPercentage: $visibilityPercentage")
        commands.add("    centerElement: $centerElement")
    }

    fun back() = commands.add("- back")
}