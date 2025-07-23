package com.canerture.kmaestro

internal class InteractionCommands(private val commands: MutableList<String>) {

    /**
     * Performs a tap/click action on a UI element.
     * Elements can be identified by text, ID, or screen coordinates.
     *
     * @param text The text content of the element to tap
     * @param id The accessibility ID or resource ID of the element to tap
     * @param point Screen coordinates to tap (e.g., "50%, 25%" or "100, 200")
     * @param index The index of the element if multiple elements match (0-based)
     * @param repeat Number of times to repeat the tap (default: 1)
     * @param delay Delay between repeated taps in milliseconds (default: 100)
     * @param retryTapIfNoChange Whether to retry if no UI change is detected (default: false)
     * @param waitToSettleTimeoutMs Time to wait for UI to settle after tap (optional)
     * @param longPressOn Whether this should be a long press instead of a tap (default: false)
     */
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

    /**
     * Performs a double-tap action on a UI element.
     * Useful for zooming or triggering special actions that require double-tap.
     *
     * @param text The text content of the element to double-tap
     * @param id The accessibility ID or resource ID of the element to double-tap
     * @param point Screen coordinates to double-tap (e.g., "50%, 25%" or "100, 200")
     * @param delay Delay between the two taps in milliseconds (default: 100)
     * @param retryTapIfNoChange Whether to retry if no UI change is detected (default: false)
     * @param waitToSettleTimeoutMs Time to wait for UI to settle after double-tap (optional)
     */
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

    /**
     * Performs a long press action on a UI element.
     * Typically used to trigger context menus or special actions.
     *
     * @param text The text content of the element to long press
     * @param id The accessibility ID or resource ID of the element to long press
     * @param point Screen coordinates to long press (e.g., "50%, 25%" or "100, 200")
     * @param repeat Number of times to repeat the long press (default: 1)
     * @param delay Delay between repeated long presses in milliseconds (default: 100)
     * @param retryTapIfNoChange Whether to retry if no UI change is detected (default: false)
     * @param waitToSettleTimeoutMs Time to wait for UI to settle after long press (optional)
     */
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

    /**
     * Performs a swipe gesture on the screen with flexible configuration options.
     * Can swipe by direction or specific coordinates.
     *
     * @param startX Starting X coordinate (can be percentage like "10%" or absolute pixel value)
     * @param startY Starting Y coordinate (can be percentage like "50%" or absolute pixel value)
     * @param endX Ending X coordinate (can be percentage like "90%" or absolute pixel value)
     * @param endY Ending Y coordinate (can be percentage like "50%" or absolute pixel value)
     * @param direction Direction to swipe (alternative to coordinate-based swiping)
     * @param from Map specifying the element to swipe from (e.g., {"id": "carousel"})
     * @param duration Duration of the swipe in milliseconds (default: 400)
     */
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

    /**
     * Scrolls the screen in the specified direction.
     * Useful for navigating through long lists or content.
     *
     * @param direction The direction to scroll (default: DOWN)
     */
    fun scroll(direction: Direction = Direction.DOWN) {
        commands.add("- scroll:")
        commands.add("    direction: ${direction.displayName}")
    }

    /**
     * Scrolls until a specified element becomes visible on the screen.
     * Continues scrolling until the target element is found or timeout is reached.
     *
     * @param text The text content to look for
     * @param id The accessibility ID or resource ID to look for
     * @param direction The direction to scroll (default: DOWN)
     * @param timeout Maximum time to wait in milliseconds (default: 20000)
     * @param speed Scrolling speed (default: 40)
     * @param visibilityPercentage Percentage of element that must be visible (default: 100)
     * @param centerElement Whether to center the element on screen (default: false)
     * @throws IllegalArgumentException if neither text nor id is provided
     */
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

    /**
     * Navigates back in the app (equivalent to pressing the back button).
     * Works on both Android (back button) and iOS (back navigation).
     */
    fun back() = commands.add("- back")
}