package com.canerture.kmaestro

import java.io.File

/**
 * KMaestro - Kotlin DSL for Maestro mobile testing framework
 *
 * A type-safe, fluent API for creating Maestro YAML test flows in Kotlin.
 * Provides comprehensive coverage of all Maestro commands with enhanced developer experience.
 *
 * @param path The directory path where the YAML file will be saved
 * @param yamlName The name of the YAML file (without .yaml extension)
 *
 * @author KMaestro Team
 * @since 1.0.0
 */
class KMaestro(
    private val path: String,
    private val yamlName: String,
) {

    private var commands = mutableListOf<String>()

    init {
        commands.add("# $yamlName\n")
    }

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
     *
     * @sample
     * ```kotlin
     * launchApp(
     *     appId = "com.example.app",
     *     clearState = true,
     *     arguments = mapOf("testMode" to true, "userId" to "123")
     * )
     * ```
     */
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

    /**
     * Adds media files (images, videos) to the device's media library.
     * Useful for testing features that require access to photos or videos.
     *
     * @param path Variable number of file paths to add to the media library
     *
     * @sample
     * ```kotlin
     * addMedia("/path/to/image.jpg", "/path/to/video.mp4")
     * ```
     */
    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

    /**
     * Asserts that a UI element is visible on the screen.
     * This command will wait for the element to appear if it's not immediately visible.
     *
     * @param text The text content to look for in the element
     * @param id The accessibility ID or resource ID of the element
     * @param enabled Whether the element should be enabled (true/false/null for any)
     * @param checked Whether the element should be checked (true/false/null for any)
     * @param focused Whether the element should have keyboard focus (true/false/null for any)
     * @param selected Whether the element should be selected (true/false/null for any)
     * @param index The index of the element if multiple elements match (0-based)
     *
     * @throws IllegalArgumentException if neither text nor id is provided
     *
     * @sample
     * ```kotlin
     * assertVisible(text = "Welcome")
     * assertVisible(id = "login_button", enabled = true)
     * ```
     */
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

    /**
     * Asserts that a UI element is NOT visible on the screen.
     * This command will wait for the element to disappear if it's currently visible.
     *
     * @param text The text content to look for in the element
     * @param id The accessibility ID or resource ID of the element
     * @param enabled Whether the element should be enabled (true/false/null for any)
     * @param checked Whether the element should be checked (true/false/null for any)
     * @param focused Whether the element should have keyboard focus (true/false/null for any)
     * @param selected Whether the element should be selected (true/false/null for any)
     * @param index The index of the element if multiple elements match (0-based)
     *
     * @throws IllegalArgumentException if neither text nor id is provided
     *
     * @sample
     * ```kotlin
     * assertNotVisible(text = "Loading...")
     * assertNotVisible(id = "error_message")
     * ```
     */
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

    /**
     * Asserts that a JavaScript condition evaluates to true.
     * Useful for custom validations that can't be expressed with standard assertions.
     *
     * @param condition A JavaScript expression that should evaluate to true
     *
     * @throws IllegalArgumentException if condition is empty
     *
     * @sample
     * ```kotlin
     * assertTrue("window.userLoggedIn === true")
     * assertTrue("document.querySelectorAll('.error').length === 0")
     * ```
     */
    fun assertTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    /**
     * Uses AI to assert that a certain condition or state is present in the app.
     * This leverages Maestro's AI capabilities for intelligent assertions.
     *
     * @param description A natural language description of what should be asserted
     *
     * @throws IllegalArgumentException if description is empty
     *
     * @sample
     * ```kotlin
     * assertWithAI("The login form is displayed correctly")
     * assertWithAI("User profile information is visible")
     * ```
     */
    fun assertWithAI(description: String) {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- assertWithAI: \"$description\"")
    }

    /**
     * Uses AI to detect any visual defects or issues in the current screen.
     * This is useful for automated UI quality checks.
     *
     * @sample
     * ```kotlin
     * assertNoDefectsWithAI()
     * ```
     */
    fun assertNoDefectsWithAI() {
        commands.add("- assertNoDefectsWithAi")
    }

    /**
     * Navigates back in the app (equivalent to pressing the back button).
     *
     * @sample
     * ```kotlin
     * back()
     * ```
     */
    fun back() = commands.add("- back")

    /**
     * Clears the iOS keychain. This is iOS-specific and has no effect on Android.
     *
     * @sample
     * ```kotlin
     * clearKeychain()
     * ```
     */
    fun clearKeychain() = commands.add("- clearKeychain")

    /**
     * Clears the app's state, including preferences, databases, and cached data.
     *
     * @param appId The app ID to clear state for. If null, clears state for the current app
     *
     * @sample
     * ```kotlin
     * clearState() // Clear current app
     * clearState("com.example.app") // Clear specific app
     * ```
     */
    fun clearState(appId: String? = null) {
        if (appId == null) {
            commands.add("- clearState")
        } else {
            commands.add("- clearState: \"$appId\"")
        }
    }

    /**
     * Copies text from a UI element to the clipboard.
     *
     * @param text The text content of the element to copy from
     * @param id The accessibility ID or resource ID of the element to copy from
     *
     * @throws IllegalArgumentException if neither text nor id is provided
     *
     * @sample
     * ```kotlin
     * copyTextFrom(text = "Copy this text")
     * copyTextFrom(id = "username_field")
     * ```
     */
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

    /**
     * Pastes text from the clipboard into the currently focused text field.
     *
     * @sample
     * ```kotlin
     * copyTextFrom(id = "source_field")
     * tapOn(id = "destination_field")
     * pasteText()
     * ```
     */
    fun pasteText() = commands.add("- pasteText")

    /**
     * Evaluates a JavaScript script in the context of the app.
     * Useful for custom logic, data manipulation, or complex validations.
     *
     * @param script The JavaScript code to execute
     *
     * @throws IllegalArgumentException if script is empty
     *
     * @sample
     * ```kotlin
     * evalScript("console.log('Test started')")
     * evalScript("window.testData = {userId: 123}")
     * ```
     */
    fun evalScript(script: String) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        commands.add("- evalScript: \"$script\"")
    }

    /**
     * Erases text from the currently focused text field.
     *
     * @param charactersToErase The number of characters to erase. If null, erases all text
     *
     * @sample
     * ```kotlin
     * eraseText() // Erase all text
     * eraseText(5) // Erase 5 characters
     * ```
     */
    fun eraseText(charactersToErase: Int? = null) {
        if (charactersToErase == null) {
            commands.add("- eraseText")
        } else {
            commands.add("- eraseText: $charactersToErase")
        }
    }

    /**
     * Waits until specified conditions are met with advanced options.
     * This is more flexible than simple assertions as it allows multiple conditions.
     *
     * @param visible Map of element properties that should be visible
     * @param notVisible Map of element properties that should not be visible
     * @param timeout Maximum time to wait in milliseconds (default: 10000)
     *
     * @sample
     * ```kotlin
     * extendedWaitUntil(
     *     visible = mapOf("text" to "Loading complete", "enabled" to true),
     *     timeout = 15000
     * )
     * ```
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

    /**
     * Uses AI to extract text from the screen based on a natural language description.
     *
     * @param description A description of what text to extract
     * @return A placeholder string that can be used to reference the extracted text
     *
     * @throws IllegalArgumentException if description is empty
     *
     * @sample
     * ```kotlin
     * val username = extractTextWithAI("Extract the username from the profile page")
     * ```
     */
    fun extractTextWithAI(description: String): String {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- extractTextWithAI: \"$description\"")
        return "\${output.extractedText}" // Placeholder for extracted text reference
    }

    /**
     * Hides the on-screen keyboard if it's currently visible.
     *
     * @sample
     * ```kotlin
     * inputText("Hello World")
     * hideKeyboard()
     * ```
     */
    fun hideKeyboard() = commands.add("- hideKeyboard")

    /**
     * Inputs text into the currently focused text field.
     *
     * @param text The text to input
     *
     * @throws IllegalArgumentException if text is empty
     *
     * @sample
     * ```kotlin
     * tapOn(id = "username_field")
     * inputText("john.doe@example.com")
     * ```
     */
    fun inputText(text: String) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        commands.add("- inputText: \"$text\"")
    }

    /**
     * Inputs a randomly generated email address.
     * Useful for testing registration flows or any feature requiring unique emails.
     *
     * @sample
     * ```kotlin
     * tapOn(id = "email_field")
     * inputRandomEmail()
     * ```
     */
    fun inputRandomEmail() = commands.add("- inputRandomEmail")

    /**
     * Inputs a randomly generated person name.
     * Useful for testing forms that require names.
     *
     * @sample
     * ```kotlin
     * tapOn(id = "name_field")
     * inputRandomPersonName()
     * ```
     */
    fun inputRandomPersonName() = commands.add("- inputRandomPersonName")

    /**
     * Inputs a randomly generated number with specified length.
     *
     * @param length The number of digits to generate (default: 8)
     *
     * @sample
     * ```kotlin
     * tapOn(id = "phone_field")
     * inputRandomNumber(10) // Generates 10-digit number
     * ```
     */
    fun inputRandomNumber(length: Int = 8) = commands.add("- inputRandomNumber:\n    length: $length")

    /**
     * Inputs randomly generated text with specified length.
     *
     * @param length The number of characters to generate (default: 8)
     *
     * @sample
     * ```kotlin
     * tapOn(id = "description_field")
     * inputRandomText(20)
     * ```
     */
    fun inputRandomText(length: Int = 8) = commands.add("- inputRandomText:\n    length: $length")

    /**
     * Terminates/kills an app completely.
     *
     * @param appId The app ID to kill. If null, kills the current app
     *
     * @sample
     * ```kotlin
     * killApp() // Kill current app
     * killApp("com.example.app") // Kill specific app
     * ```
     */
    fun killApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- killApp")
        } else {
            commands.add("- killApp: \"$appId\"")
        }
    }

    /**
     * Opens a URL link in the default browser or associated app.
     *
     * @param url The URL to open
     * @param autoVerify Whether to automatically verify the URL (default: null)
     * @param browser Whether to force opening in browser instead of associated app (default: null)
     *
     * @throws IllegalArgumentException if url is empty
     *
     * @sample
     * ```kotlin
     * openLink("https://example.com")
     * openLink("https://test.com", autoVerify = true, browser = true)
     * ```
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
     * Simulates pressing a physical or virtual key on the device.
     *
     * @param key The key to press (from KeyType enum)
     *
     * @sample
     * ```kotlin
     * pressKey(KeyType.ENTER)
     * pressKey(KeyType.BACK)
     * pressKey(KeyType.VOLUME_UP)
     * ```
     */
    fun pressKey(key: KeyType) = commands.add("- pressKey: ${key.displayName}")

    /**
     * Repeats a set of commands a specified number of times.
     *
     * @param times The number of times to repeat the commands
     * @param commands List of command strings to repeat
     *
     * @sample
     * ```kotlin
     * repeat(3, listOf(
     *     "- tapOn: \"Next\"",
     *     "- waitForAnimationToEnd"
     * ))
     * ```
     */
    fun repeat(times: Int, commands: List<String>) {
        this.commands.add("- repeat:")
        this.commands.add("    times: $times")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    /**
     * Retries a set of commands with a maximum number of attempts.
     * Useful for handling flaky UI elements or network-dependent operations.
     *
     * @param maxRetries Maximum number of retry attempts (default: 3)
     * @param commands List of command strings to retry
     *
     * @sample
     * ```kotlin
     * retry(5, listOf(
     *     "- tapOn: \"Retry Button\"",
     *     "- assertVisible: \"Success\""
     * ))
     * ```
     */
    fun retry(maxRetries: Int = 3, commands: List<String>) {
        this.commands.add("- retry:")
        this.commands.add("    maxRetries: $maxRetries")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    /**
     * Runs a predefined flow by name. Flows are reusable command sequences.
     *
     * @param flowName The name of the flow to run
     *
     * @throws IllegalArgumentException if flowName is empty
     *
     * @sample
     * ```kotlin
     * runFlow("loginFlow")
     * runFlow("onboardingFlow")
     * ```
     */
    fun runFlow(flowName: String) {
        require(flowName.isNotEmpty()) { "Flow name must not be empty." }
        commands.add("- runFlow: \"$flowName\"")
    }

    /**
     * Runs a JavaScript script with optional environment variables.
     *
     * @param script The script content or file path to run
     * @param env Optional environment variables to pass to the script
     *
     * @throws IllegalArgumentException if script is empty
     *
     * @sample
     * ```kotlin
     * runScript("console.log('Hello World')")
     * runScript("./scripts/setup.js", mapOf("ENV" to "test"))
     * ```
     */
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

    /**
     * Scrolls the screen in the specified direction.
     *
     * @param direction The direction to scroll (default: DOWN)
     *
     * @sample
     * ```kotlin
     * scroll() // Scroll down
     * scroll(Direction.UP) // Scroll up
     * ```
     */
    fun scroll(direction: Direction = Direction.DOWN) {
        commands.add("- scroll:")
        commands.add("    direction: ${direction.displayName}")
    }

    /**
     * Scrolls until a specified element becomes visible on the screen.
     *
     * @param text The text content to look for
     * @param id The accessibility ID or resource ID to look for
     * @param direction The direction to scroll (default: DOWN)
     * @param timeout Maximum time to wait in milliseconds (default: 20000)
     * @param speed Scrolling speed (default: 40)
     * @param visibilityPercentage Percentage of element that must be visible (default: 100)
     * @param centerElement Whether to center the element on screen (default: false)
     *
     * @throws IllegalArgumentException if neither text nor id is provided
     *
     * @sample
     * ```kotlin
     * scrollUntilVisible(text = "Terms and Conditions")
     * scrollUntilVisible(id = "footer", direction = Direction.DOWN, timeout = 30000)
     * ```
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
     * Enables or disables airplane mode on the device.
     *
     * @param enabled Whether to enable (true) or disable (false) airplane mode
     *
     * @sample
     * ```kotlin
     * setAirplaneMode(true) // Enable airplane mode
     * setAirplaneMode(false) // Disable airplane mode
     * ```
     */
    fun setAirplaneMode(enabled: Boolean) = commands.add("- setAirplaneMode: $enabled")

    /**
     * Sets the device's GPS location coordinates.
     *
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     *
     * @sample
     * ```kotlin
     * setLocation(40.7128, -74.0060) // New York City
     * setLocation(51.5074, -0.1278) // London
     * ```
     */
    fun setLocation(latitude: Double, longitude: Double) {
        commands.add("- setLocation:")
        commands.add("    latitude: $latitude")
        commands.add("    longitude: $longitude")
    }

    /**
     * Starts recording the test session for video capture.
     *
     * @param fileName The name of the recording file (default: "recording")
     *
     * @sample
     * ```kotlin
     * startRecording("test_session_1")
     * // ... test steps ...
     * stopRecording()
     * ```
     */
    fun startRecording(fileName: String = "recording") = commands.add("- startRecording: \"$fileName\"")

    /**
     * Stops the current recording session.
     *
     * @sample
     * ```kotlin
     * startRecording()
     * // ... test steps ...
     * stopRecording()
     * ```
     */
    fun stopRecording() = commands.add("- stopRecording")

    /**
     * Stops/closes an app (but doesn't kill it completely).
     *
     * @param appId The app ID to stop. If null, stops the current app
     *
     * @sample
     * ```kotlin
     * stopApp() // Stop current app
     * stopApp("com.example.app") // Stop specific app
     * ```
     */
    fun stopApp(appId: String? = null) {
        if (appId == null) {
            commands.add("- stopApp")
        } else {
            commands.add("- stopApp: \"$appId\"")
        }
    }

    /**
     * Performs a swipe gesture on the screen with flexible configuration options.
     *
     * @param startX Starting X coordinate (can be percentage like "10%" or absolute pixel value)
     * @param startY Starting Y coordinate (can be percentage like "50%" or absolute pixel value)
     * @param endX Ending X coordinate (can be percentage like "90%" or absolute pixel value)
     * @param endY Ending Y coordinate (can be percentage like "50%" or absolute pixel value)
     * @param direction Direction to swipe (alternative to coordinate-based swiping)
     * @param from Map specifying the element to swipe from (e.g., {"id": "carousel"})
     * @param duration Duration of the swipe in milliseconds (default: 400)
     *
     * @sample
     * ```kotlin
     * swipe(direction = Direction.LEFT) // Simple directional swipe
     * swipe(startX = "10%", startY = "50%", endX = "90%", endY = "50%") // Coordinate swipe
     * swipe(from = mapOf("id" to "carousel"), direction = Direction.RIGHT) // Swipe from element
     * ```
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
     * Takes a screenshot of the current screen.
     *
     * @param fileName The name of the screenshot file (default: "screenshot")
     *
     * @sample
     * ```kotlin
     * takeScreenshot() // Default name
     * takeScreenshot("login_screen") // Custom name
     * ```
     */
    fun takeScreenshot(fileName: String = "screenshot") = commands.add("- takeScreenshot: \"$fileName\"")

    /**
     * Toggles airplane mode (enables if disabled, disables if enabled).
     *
     * @sample
     * ```kotlin
     * toggleAirplaneMode()
     * ```
     */
    fun toggleAirplaneMode() = commands.add("- toggleAirplaneMode")

    /**
     * Performs a tap/click action on a UI element.
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
     *
     * @sample
     * ```kotlin
     * tapOn(text = "Login") // Tap by text
     * tapOn(id = "submit_button") // Tap by ID
     * tapOn(point = "50%, 25%") // Tap at coordinates
     * tapOn(text = "Button", repeat = 3, delay = 500) // Multiple taps
     * ```
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
     *
     * @param text The text content of the element to double-tap
     * @param id The accessibility ID or resource ID of the element to double-tap
     * @param point Screen coordinates to double-tap (e.g., "50%, 25%" or "100, 200")
     * @param delay Delay between the two taps in milliseconds (default: 100)
     * @param retryTapIfNoChange Whether to retry if no UI change is detected (default: false)
     * @param waitToSettleTimeoutMs Time to wait for UI to settle after double-tap (optional)
     *
     * @sample
     * ```kotlin
     * doubleTapOn(text = "Image") // Double-tap by text
     * doubleTapOn(id = "zoom_area") // Double-tap by ID
     * doubleTapOn(point = "50%, 50%", delay = 200) // Double-tap at coordinates with custom delay
     * ```
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
     *
     * @param text The text content of the element to long press
     * @param id The accessibility ID or resource ID of the element to long press
     * @param point Screen coordinates to long press (e.g., "50%, 25%" or "100, 200")
     * @param repeat Number of times to repeat the long press (default: 1)
     * @param delay Delay between repeated long presses in milliseconds (default: 100)
     * @param retryTapIfNoChange Whether to retry if no UI change is detected (default: false)
     * @param waitToSettleTimeoutMs Time to wait for UI to settle after long press (optional)
     *
     * @sample
     * ```kotlin
     * longPressOn(text = "Context Menu") // Long press by text
     * longPressOn(id = "file_item") // Long press by ID
     * longPressOn(point = "75%, 50%", repeat = 2) // Multiple long presses
     * ```
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
     * Simulates traveling between GPS coordinates at a specified speed.
     * Useful for testing location-based features with movement simulation.
     *
     * @param points List of latitude/longitude coordinate pairs representing the travel route
     * @param speed Travel speed in meters per second (default: 7900)
     *
     * @throws IllegalArgumentException if points list is empty
     *
     * @sample
     * ```kotlin
     * travel(listOf(
     *     40.7128 to -74.0060, // New York
     *     34.0522 to -118.2437  // Los Angeles
     * ), speed = 5000)
     * ```
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
     * Waits for all animations to complete before proceeding.
     * Essential for stable tests when dealing with animated UI elements.
     *
     * @param timeout Maximum time to wait for animations to end in milliseconds (default: 5000)
     *
     * @sample
     * ```kotlin
     * tapOn(text = "Animate")
     * waitForAnimationToEnd(timeout = 3000)
     * assertVisible(text = "Animation Complete")
     * ```
     */
    fun waitForAnimationToEnd(timeout: Int = 5000) = commands.add("- waitForAnimationToEnd:\n    timeout: $timeout")

    /**
     * Builds the final YAML content and saves it to a file.
     * This method should be called at the end of your test flow construction.
     *
     * @return The generated YAML content as a string
     *
     * @sample
     * ```kotlin
     * val yaml = kMaestro.apply {
     *     launchApp("com.example.app")
     *     tapOn(text = "Login")
     * }.build()
     * ```
     */
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

    /**
     * Enumeration of supported physical and virtual keys that can be pressed.
     */
    enum class KeyType(val displayName: String) {
        /** Home button */
        HOME("Home"),

        /** Lock/Power button */
        LOCK("Lock"),

        /** Enter key */
        ENTER("Enter"),

        /** Backspace key */
        BACKSPACE("Backspace"),

        /** Volume up button */
        VOLUME_UP("Volume Up"),

        /** Volume down button */
        VOLUME_DOWN("Volume Down"),

        /** Back button */
        BACK("Back"),

        /** Power button */
        POWER("Power"),

        /** Tab key */
        TAB("Tab"),

        /** Remote control D-pad up */
        REMOTE_DPAD_UP("Remote Dpad Up"),

        /** Remote control D-pad down */
        REMOTE_DPAD_DOWN("Remote Dpad Down"),

        /** Remote control D-pad left */
        REMOTE_DPAD_LEFT("Remote Dpad Left"),

        /** Remote control D-pad right */
        REMOTE_DPAD_RIGHT("Remote Dpad Right"),

        /** Remote control D-pad center */
        REMOTE_DPAD_CENTER("Remote Dpad Center"),

        /** Remote control play/pause button */
        REMOTE_MEDIA_PLAY_PAUSE("Remote Media Play Pause"),

        /** Remote control stop button */
        REMOTE_MEDIA_STOP("Remote Media Stop"),

        /** Remote control next button */
        REMOTE_MEDIA_NEXT("Remote Media Next"),

        /** Remote control previous button */
        REMOTE_MEDIA_PREVIOUS("Remote Media Previous"),

        /** Remote control rewind button */
        REMOTE_MEDIA_REWIND("Remote Media Rewind"),

        /** Remote control fast forward button */
        REMOTE_MEDIA_FAST_FORWARD("Remote Media Fast Forward"),

        /** Remote control navigation up */
        REMOTE_SYSTEM_NAVIGATION_UP("Remote System Navigation Up"),

        /** Remote control navigation down */
        REMOTE_SYSTEM_NAVIGATION_DOWN("Remote System Navigation Down"),

        /** Remote control A button */
        REMOTE_BUTTON_A("Remote Button A"),

        /** Remote control B button */
        REMOTE_BUTTON_B("Remote Button B"),

        /** Remote control menu button */
        REMOTE_MENU("Remote Menu"),

        /** TV input button */
        TV_INPUT("TV Input"),

        /** TV HDMI 1 input */
        TV_INPUT_HDMI_1("TV Input HDMI 1"),

        /** TV HDMI 2 input */
        TV_INPUT_HDMI_2("TV Input HDMI 2"),

        /** TV HDMI 3 input */
        TV_INPUT_HDMI_3("TV Input HDMI 3"),
    }

    /**
     * Enumeration of supported directional movements for scrolling and swiping.
     */
    enum class Direction(val displayName: String) {
        /** Upward direction */
        UP("UP"),

        /** Downward direction */
        DOWN("DOWN"),

        /** Leftward direction */
        LEFT("LEFT"),

        /** Rightward direction */
        RIGHT("RIGHT"),
    }
}