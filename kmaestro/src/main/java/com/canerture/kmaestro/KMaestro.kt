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
        packageName: String,
        arguments: Map<String, Any> = emptyMap(),
    ) {
        require(packageName.isNotEmpty()) { "App ID must not be empty." }
        val launchCommand = StringBuilder("- launchApp:\n    appId: \"$packageName\"")
        if (arguments.isNotEmpty()) {
            require(arguments.values.none { it !is String && it !is Boolean && it !is Double && it !is Int }) {
                "Arguments must be of type String, Boolean, Double, or Integer."
            }
            launchCommand.append("\n    arguments:")
            arguments.forEach { (key, value) ->
                launchCommand.append("\n      $key: $value")
            }
        }
        commands.add(launchCommand.toString())
    }

    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

    fun isVisibleText(
        text: String,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        val visibilityCommand = StringBuilder("- assertVisible:\n")
        if (text.isNotEmpty()) visibilityCommand.append("    text: \"$text\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isVisibleTag(
        tag: String = "",
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(tag.isNotEmpty()) { "Tag must not be empty." }
        val visibilityCommand = StringBuilder("- assertVisible:\n")
        if (tag.isNotEmpty()) visibilityCommand.append("    id: \"$tag\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isInvisibleText(
        text: String,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        val visibilityCommand = StringBuilder("- assertNotVisible:\n")
        if (text.isNotEmpty()) visibilityCommand.append("    text: \"$text\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isInvisibleTag(
        tag: String = "",
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(tag.isNotEmpty()) { "Tag must not be empty." }
        val visibilityCommand = StringBuilder("- assertNotVisible:\n")
        if (tag.isNotEmpty()) visibilityCommand.append("    id: \"$tag\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    fun back() = commands.add("- back")

    fun clearState(appId: String = "") {
        if (appId.isEmpty()) commands.add("- clearState") else commands.add("- clearState: $appId")
    }

    fun copyText(tag: String) = commands.add("- copyTextFrom:\n    id: \"$tag\"")

    fun pasteText() = commands.add("- pasteText")

    fun evalScript(script: String) = commands.add("- evalScript: \"$script\"")

    fun clearText(characters: Int = 50) = commands.add("- eraseText: $characters")

    fun hideKeyboard() = commands.add("- hideKeyboard")

    fun setText(text: String) = commands.add("- inputText: \"$text\"")

    fun setRandomEmail() = commands.add("- inputRandomEmail")

    fun setRandomPersonName() = commands.add("- inputRandomPersonName")

    fun setRandomNumber(length: Int = 8) = commands.add("- inputRandomNumber:\n    length: $length")

    fun setRandomText(length: Int = 8) = commands.add("- inputRandomText:\n    length: $length")

    fun killApp() = commands.add("- killApp")

    fun openUrl(
        url: String,
        autoVerify: Boolean = false,
        forceBrowser: Boolean = false,
    ) {
        require(url.isNotEmpty()) { "URL must not be empty." }
        val openUrlCommand = StringBuilder("- openUrl:\n    link: \"$url\"")
        if (autoVerify) openUrlCommand.append("\n    autoVerify: true")
        if (forceBrowser) openUrlCommand.append("\n    browser: true")
        commands.add(openUrlCommand.toString())
    }

    fun pressKey(key: KeyType) = commands.add("- pressKey: ${key.displayName}")

    fun repeat(
        times: Int = 1,
        commands: (KMaestro) -> Unit,
    ) {
        this.commands.add("- repeat:\n    times: $times")
        commands.invoke(this)
    }

    fun runFlow(flowName: String) {
        require(flowName.isNotEmpty()) { "Flow name must not be empty." }
        commands.add("- runFlow: $flowName")
    }

    fun scroll() = commands.add("- scroll")

    fun scrollUntilVisibleText(
        text: String,
        direction: Direction = Direction.DOWN,
        timeout: Int = 20000,
        speed: Int = 40,
        visibilityPercentage: Int = 100,
        centerElement: Boolean = false,
    ) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        commands.add("- scrollUntilVisible:")
        commands.add("    element:\n      text: \"$text\"")
        commands.add("    direction: ${direction.displayName}")
        commands.add("    timeout: $timeout")
        commands.add("    speed: $speed")
        commands.add("    visibilityPercentage: $visibilityPercentage")
        commands.add("    centerElement: $centerElement")
    }

    fun scrollUntilVisibleTag(
        tag: String,
        direction: Direction = Direction.DOWN,
        timeout: Int = 20000,
        speed: Int = 40,
        visibilityPercentage: Int = 100,
        centerElement: Boolean = false,
    ) {
        require(tag.isNotEmpty()) { "Tag must not be empty." }
        commands.add("- scrollUntilVisible:")
        commands.add("    element:\n      text: \"$tag\"")
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

    fun startRecording() = commands.add("- startRecording: recording")

    fun stopRecording() = commands.add("- stopRecording")

    fun stopApp(appId: String = "") {
        if (appId.isEmpty()) commands.add("- stopApp") else commands.add("- stopApp: $appId")
    }

    fun swipe(startWidth: String, startHeight: String, endWidth: String, endHeight: String, duration: Int = 400) {
        commands.add("- swipe:")
        commands.add("    start: $startWidth, $startHeight")
        commands.add("    end: $endWidth, $endHeight")
        commands.add("    duration: $duration")
    }

    fun swipe(direction: Direction, duration: Int = 400) {
        commands.add("- swipe:")
        commands.add("    direction: ${direction.displayName}")
        commands.add("    duration: $duration")
    }

    fun swipe(fromTag: String, direction: Direction, duration: Int = 400) {
        require(fromTag.isNotEmpty()) { "Tag must not be empty." }
        commands.add("- swipe:")
        commands.add("    from:\n      id: \"$fromTag\"")
        commands.add("    direction: ${direction.displayName}")
        commands.add("    duration: $duration")
    }

    fun swipe(startX: Int, startY: Int, endX: Int, endY: Int, duration: Int = 400) {
        commands.add("- swipe:")
        commands.add("    start: $startX, $startY")
        commands.add("    end: $endX, $endY")
        commands.add("    duration: $duration")
    }

    fun takeScreenshot(fileName: String = "screenshot") = commands.add("- takeScreenshot: $fileName")

    fun toggleAirplaneMode() = commands.add("- toggleAirplaneMode")

    fun clickText(
        text: String,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null,
    ) {
        commands.add("- tapOn: $text")
        if (repeat > 1) {
            commands.add("    repeat: $repeat")
            commands.add("    delay: $delay")
        }
        if (retryTapIfNoChange) commands.add("    retryTapIfNoChange: true")
        waitToSettleTimeoutMs?.let { commands.add("    waitToSettleTimeoutMs: $it") }
    }

    fun clickTag(
        tag: String,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) {
        commands.add("- tapOn:\n    id: $tag")
        if (repeat > 1) {
            commands.add("    repeat: $repeat")
            commands.add("    delay: $delay")
        }
        if (retryTapIfNoChange) commands.add("    retryTapIfNoChange: true")
        waitToSettleTimeoutMs?.let { commands.add("    waitToSettleTimeoutMs: $it") }
    }

    fun clickSpecificPoint(
        percentageX: Int,
        percentageY: Int,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) {
        require(percentageX >= 0 && percentageY >= 0) { "Percentage coordinates must be non-negative." }
        commands.add("- tapOn:\n    point: $percentageX, $percentageY")
        if (repeat > 1) {
            commands.add("    repeat: $repeat")
            commands.add("    delay: $delay")
        }
        if (retryTapIfNoChange) commands.add("    retryTapIfNoChange: true")
        waitToSettleTimeoutMs?.let { commands.add("    waitToSettleTimeoutMs: $it") }
    }

    fun clickCoordinate(
        x: Int,
        y: Int,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) {
        require(x >= 0 && y >= 0) { "Coordinates must be non-negative." }
        commands.add("- tapOn:\n    point: $x, $y")
        if (repeat > 1) {
            commands.add("    repeat: $repeat")
            commands.add("    delay: $delay")
        }
        if (retryTapIfNoChange) commands.add("    retryTapIfNoChange: true")
        waitToSettleTimeoutMs?.let { commands.add("    waitToSettleTimeoutMs: $it") }
    }

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