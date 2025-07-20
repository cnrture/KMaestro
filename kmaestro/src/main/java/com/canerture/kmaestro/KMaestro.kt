package com.canerture.kmaestro

import java.io.File

class KMaestro(
    private val path: String,
    private val yamlName: String,
) {

    private var commands = mutableListOf<String>()

    init {
        commands.add("# $yamlName\n")
    }

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

    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

    fun assertVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val visibilityCommand = StringBuilder("- assertVisible:")

        if (text != null && id == null) {
            visibilityCommand.append(" \"$text\"")
        } else {
            visibilityCommand.append("\n")
            text?.let { visibilityCommand.append("    text: \"$it\"\n") }
            id?.let { visibilityCommand.append("    id: \"$it\"\n") }
            enabled?.let { visibilityCommand.append("    enabled: $it\n") }
            checked?.let { visibilityCommand.append("    checked: $it\n") }
            focused?.let { visibilityCommand.append("    focused: $it\n") }
            selected?.let { visibilityCommand.append("    selected: $it\n") }
            index?.let { visibilityCommand.append("    index: $it\n") }
        }

        commands.add(visibilityCommand.toString().trimEnd())
    }

    fun assertNotVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val visibilityCommand = StringBuilder("- assertNotVisible:")

        if (text != null && id == null && enabled == null && checked == null && focused == null && selected == null && index == null) {
            visibilityCommand.append(" \"$text\"")
        } else {
            visibilityCommand.append("\n")
            text?.let { visibilityCommand.append("    text: \"$it\"\n") }
            id?.let { visibilityCommand.append("    id: \"$it\"\n") }
            enabled?.let { visibilityCommand.append("    enabled: $it\n") }
            checked?.let { visibilityCommand.append("    checked: $it\n") }
            focused?.let { visibilityCommand.append("    focused: $it\n") }
            selected?.let { visibilityCommand.append("    selected: $it\n") }
            index?.let { visibilityCommand.append("    index: $it\n") }
        }

        commands.add(visibilityCommand.toString().trimEnd())
    }

    fun assertTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    fun assertWithAI(description: String) {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- assertWithAI: \"$description\"")
    }

    fun assertNoDefectsWithAI() {
        commands.add("- assertNoDefectsWithAi")
    }

    fun back() = commands.add("- back")

    fun clearKeychain() = commands.add("- clearKeychain")

    fun clearState(appId: String? = null) {
        if (appId == null) {
            commands.add("- clearState")
        } else {
            commands.add("- clearState: \"$appId\"")
        }
    }

    fun copyTextFrom(
        text: String? = null,
        id: String? = null
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val copyCommand = StringBuilder("- copyTextFrom:")

        if (text != null && id == null) {
            copyCommand.append(" \"$text\"")
        } else {
            copyCommand.append("\n")
            text?.let { copyCommand.append("    text: \"$it\"\n") }
            id?.let { copyCommand.append("    id: \"$it\"\n") }
        }

        commands.add(copyCommand.toString().trimEnd())
    }

    fun pasteText() = commands.add("- pasteText")

    fun evalScript(script: String) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        commands.add("- evalScript: \"$script\"")
    }

    fun eraseText(charactersToErase: Int? = null) {
        if (charactersToErase == null) {
            commands.add("- eraseText")
        } else {
            commands.add("- eraseText: $charactersToErase")
        }
    }

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

    fun extractTextWithAI(description: String): String {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- extractTextWithAI: \"$description\"")
        return "\${output.extractedText}" // Placeholder for extracted text reference
    }

    fun hideKeyboard() = commands.add("- hideKeyboard")

    fun inputText(text: String) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        commands.add("- inputText: \"$text\"")
    }

    fun inputRandomEmail() = commands.add("- inputRandomEmail")

    fun inputRandomPersonName() = commands.add("- inputRandomPersonName")

    fun inputRandomNumber(length: Int = 8) = commands.add("- inputRandomNumber:\n    length: $length")

    fun inputRandomText(length: Int = 8) = commands.add("- inputRandomText:\n    length: $length")

    fun killApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- killApp")
        } else {
            commands.add("- killApp: \"$appId\"")
        }
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

    fun pressKey(key: KeyType) = commands.add("- pressKey: ${key.displayName}")

    fun repeat(times: Int, commands: List<String>) {
        this.commands.add("- repeat:")
        this.commands.add("    times: $times")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    fun retry(maxRetries: Int = 3, commands: List<String>) {
        this.commands.add("- retry:")
        this.commands.add("    maxRetries: $maxRetries")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    fun runFlow(flowName: String) {
        require(flowName.isNotEmpty()) { "Flow name must not be empty." }
        commands.add("- runFlow: \"$flowName\"")
    }

    fun runScript(script: String, env: Map<String, String>? = null) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        val scriptCommand = StringBuilder("- runScript:")

        if (env == null) {
            scriptCommand.append(" \"$script\"")
        } else {
            scriptCommand.append("\n    script: \"$script\"")
            if (env.isNotEmpty()) {
                scriptCommand.append("\n    env:")
                env.forEach { (key, value) ->
                    scriptCommand.append("\n      $key: \"$value\"")
                }
            }
        }

        commands.add(scriptCommand.toString())
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

    fun setAirplaneMode(enabled: Boolean) = commands.add("- setAirplaneMode: $enabled")

    fun setLocation(latitude: Double, longitude: Double) {
        commands.add("- setLocation:")
        commands.add("    latitude: $latitude")
        commands.add("    longitude: $longitude")
    }

    fun startRecording(fileName: String = "recording") = commands.add("- startRecording: \"$fileName\"")

    fun stopRecording() = commands.add("- stopRecording")

    fun stopApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- stopApp")
        } else {
            commands.add("- stopApp: \"$appId\"")
        }
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

    fun takeScreenshot(fileName: String = "screenshot") = commands.add("- takeScreenshot: \"$fileName\"")

    fun toggleAirplaneMode() = commands.add("- toggleAirplaneMode")

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

    fun travel(points: List<Pair<Double, Double>>, speed: Int = 7900) {
        require(points.isNotEmpty()) { "Points must not be empty." }
        commands.add("- travel:")
        commands.add("    points:")
        points.forEach { (latitude, longitude) ->
            commands.add("      - $latitude,$longitude")
        }
        commands.add("    speed: $speed")
    }

    fun waitForAnimationToEnd(timeout: Int = 5000) = commands.add("- waitForAnimationToEnd:\n    timeout: $timeout")

    fun build(): String {
        val yaml = commands.joinToString("\n")
        val directory = File(path)
        if (!directory.exists()) directory.mkdirs()
        val fileName = if (yamlName.endsWith(".yaml")) yamlName else "$yamlName.yaml"
        val file = File(directory, fileName)
        file.writeText(yaml)
        commands.clear()
        return yaml
    }

    enum class KeyType(val displayName: String) {
        HOME("Home"),
        LOCK("Lock"),
        ENTER("Enter"),
        BACKSPACE("Backspace"),
        VOLUME_UP("Volume Up"),
        VOLUME_DOWN("Volume Down"),
        BACK("Back"),
        POWER("Power"),
        TAB("Tab"),
        REMOTE_DPAD_UP("Remote Dpad Up"),
        REMOTE_DPAD_DOWN("Remote Dpad Down"),
        REMOTE_DPAD_LEFT("Remote Dpad Left"),
        REMOTE_DPAD_RIGHT("Remote Dpad Right"),
        REMOTE_DPAD_CENTER("Remote Dpad Center"),
        REMOTE_MEDIA_PLAY_PAUSE("Remote Media Play Pause"),
        REMOTE_MEDIA_STOP("Remote Media Stop"),
        REMOTE_MEDIA_NEXT("Remote Media Next"),
        REMOTE_MEDIA_PREVIOUS("Remote Media Previous"),
        REMOTE_MEDIA_REWIND("Remote Media Rewind"),
        REMOTE_MEDIA_FAST_FORWARD("Remote Media Fast Forward"),
        REMOTE_SYSTEM_NAVIGATION_UP("Remote System Navigation Up"),
        REMOTE_SYSTEM_NAVIGATION_DOWN("Remote System Navigation Down"),
        REMOTE_BUTTON_A("Remote Button A"),
        REMOTE_BUTTON_B("Remote Button B"),
        REMOTE_MENU("Remote Menu"),
        TV_INPUT("TV Input"),
        TV_INPUT_HDMI_1("TV Input HDMI 1"),
        TV_INPUT_HDMI_2("TV Input HDMI 2"),
        TV_INPUT_HDMI_3("TV Input HDMI 3"),
    }

    enum class Direction(val displayName: String) {
        UP("UP"),
        DOWN("DOWN"),
        LEFT("LEFT"),
        RIGHT("RIGHT"),
    }
}