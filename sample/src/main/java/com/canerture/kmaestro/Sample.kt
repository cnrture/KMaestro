package com.canerture.kmaestro

fun main() {
    testLaunchAppCommands()
    testAssertionCommands()
    testNavigationCommands()
    testStateManagementCommands()
    testInputCommands()
    testTapCommands()
    testScrollCommands()
    testSwipeCommands()
    testWaitCommands()
    testMediaCommands()
    testScriptCommands()
    testFlowControlCommands()
    testKeyPressCommands()
    testLinkCommands()
    testTravelCommand()
    testComplexScenario()
}

fun testLaunchAppCommands() {
    KMaestro(path = "maestro", yamlName = "launch_test") {
        // Basic launch
        launchApp()

        // Launch with app ID
        launchApp(appId = "com.example.app")

        // Launch with all options
        launchApp(
            appId = "com.example.app",
            clearState = true,
            clearKeychain = true,
            stopApp = false,
            permissions = mapOf(
                "notifications" to "deny",
                "location" to "allow"
            ),
            arguments = mapOf(
                "testMode" to true,
                "username" to "test_user",
                "timeout" to 30.0,
                "retries" to 5
            )
        )
    }
}

fun testAssertionCommands() {
    KMaestro(path = "maestro", yamlName = "assertion_test") {
        // Basic assertions
        assertVisible(text = "Welcome")
        assertVisible(id = "login_button")

        // Assertions with properties
        assertVisible(
            text = "Submit",
            enabled = true,
            checked = false,
            focused = true
        )

        assertNotVisible(text = "Error")
        assertNotVisible(
            id = "error_message",
            enabled = false
        )

        // Other assertions
        assertTrue("2 + 2 == 4")
        assertWithAI("The login form is displayed correctly")
        assertNoDefectsWithAI()
    }
}

fun testNavigationCommands() {
    KMaestro(path = "maestro", yamlName = "navigation_test") {
        back()
        killApp()
        killApp(appId = "com.example.app")
        stopApp()
        stopApp(appId = "com.example.app")
    }
}

fun testStateManagementCommands() {
    KMaestro(path = "maestro", yamlName = "state_test") {
        clearState()
        clearState(appId = "com.example.app")
        clearKeychain()

        setAirplaneMode(enabled = true)
        setAirplaneMode(enabled = false)
        toggleAirplaneMode()

        setLocation(latitude = 40.7128, longitude = -74.0060) // New York
    }
}

fun testInputCommands() {
    KMaestro(path = "maestro", yamlName = "input_test") {
        inputText("Hello World")
        inputRandomEmail()
        inputRandomPersonName()
        inputRandomNumber(length = 10)
        inputRandomText(length = 15)

        eraseText()
        eraseText(charactersToErase = 5)

        hideKeyboard()

        copyTextFrom(text = "Copy this")
        copyTextFrom(id = "username_field")
        pasteText()
    }
}

fun testTapCommands() {
    KMaestro(path = "maestro", yamlName = "tap_test") {
        // Basic taps
        tapOn(text = "Login")
        tapOn(id = "submit_button")
        tapOn(point = "50%, 25%")

        // Advanced taps
        tapOn(
            text = "Button",
            repeat = 3,
            delay = 200,
            retryTapIfNoChange = true,
            waitToSettleTimeoutMs = 1000
        )

        // Double taps
        doubleTapOn(text = "Settings")
        doubleTapOn(id = "menu_icon")
        doubleTapOn(point = "100, 200")

        // Long press
        longPressOn(text = "Context Menu")
        longPressOn(id = "long_press_area")
        longPressOn(
            point = "75%, 50%",
            repeat = 2,
            delay = 300
        )
    }
}

fun testScrollCommands() {
    KMaestro(path = "maestro", yamlName = "scroll_test") {
        scroll()
        scroll(direction = Direction.UP)
        scroll(direction = Direction.LEFT)

        scrollUntilVisible(text = "Sign Up")
        scrollUntilVisible(
            id = "footer_element",
            direction = Direction.DOWN,
            timeout = 30000,
            speed = 60,
            visibilityPercentage = 80,
            centerElement = true
        )
    }
}

fun testSwipeCommands() {
    KMaestro(path = "maestro", yamlName = "swipe_test") {
        // Direction-based swipe
        swipe(direction = Direction.LEFT, duration = 500)

        // Coordinate-based swipe
        swipe(
            startX = "10%",
            startY = "50%",
            endX = "90%",
            endY = "50%",
            duration = 800
        )

        // Swipe from element
        swipe(
            from = mapOf("id" to "carousel_item"),
            direction = Direction.RIGHT,
            duration = 600
        )
    }
}

fun testWaitCommands() {
    KMaestro(path = "maestro", yamlName = "wait_test") {
        waitForAnimationToEnd()
        waitForAnimationToEnd(timeout = 8000)

        extendedWaitUntil(
            visible = mapOf(
                "text" to "Loading complete",
                "enabled" to true
            ),
            timeout = 15000
        )

        extendedWaitUntil(
            notVisible = mapOf(
                "id" to "loading_spinner"
            ),
            timeout = 20000
        )
    }
}

fun testMediaCommands() {
    KMaestro(path = "maestro", yamlName = "media_test") {
        addMedia("/path/to/image1.jpg", "/path/to/video.mp4")

        takeScreenshot()
        takeScreenshot("login_screen")

        startRecording()
        startRecording("test_session")
        stopRecording()
    }
}

fun testScriptCommands() {
    KMaestro(path = "maestro", yamlName = "script_test") {
        evalScript("Math.random()")

        runScript("console.log('Hello')")
        runScript(
            script = "process.env.TEST_VAR",
            env = mapOf(
                "TEST_VAR" to "test_value",
                "DEBUG" to "true"
            )
        )

        extractTextWithAI("Extract the username from the profile page")
    }
}

fun testFlowControlCommands() {
    KMaestro(path = "maestro", yamlName = "flow_test") {
        runFlow("login_flow")

        repeat(
            times = 3,
            commands = listOf(
                "- tapOn: \"Next\"",
                "- waitForAnimationToEnd"
            )
        )

        retry(
            maxRetries = 5,
            commands = listOf(
                "- tapOn: \"Retry Button\"",
                "- assertVisible: \"Success\""
            )
        )
    }
}

fun testKeyPressCommands() {
    KMaestro(path = "maestro", yamlName = "keypress_test") {
        pressKey(KeyType.ENTER)
        pressKey(KeyType.BACK)
        pressKey(KeyType.HOME)
        pressKey(KeyType.VOLUME_UP)
        pressKey(KeyType.VOLUME_DOWN)
        pressKey(KeyType.POWER)
        pressKey(KeyType.TAB)
        pressKey(KeyType.BACKSPACE)
    }
}

fun testLinkCommands() {
    KMaestro(path = "maestro", yamlName = "link_test") {
        openLink("https://example.com")
        openLink(
            url = "https://test.com",
            autoVerify = true,
            browser = true
        )
    }
}

fun testTravelCommand() {
    KMaestro(path = "maestro", yamlName = "travel_test") {
        travel(
            points = listOf(
                40.7128 to -74.0060, // New York
                34.0522 to -118.2437, // Los Angeles
                41.8781 to -87.6298  // Chicago
            ),
            speed = 5000
        )
    }
}

fun testComplexScenario() {
    KMaestro(path = "maestro", yamlName = "complex_scenario") {
        // Launch app
        launchApp(
            appId = "com.example.testapp",
            clearState = true,
            arguments = mapOf("testMode" to true)
        )

        // Wait for app to load
        waitForAnimationToEnd()
        assertVisible(text = "Welcome")

        // Login flow
        tapOn(id = "login_button")
        assertVisible(text = "Login")

        inputText("test@example.com")
        tapOn(id = "password_field")
        inputText("password123")
        hideKeyboard()

        tapOn(text = "Sign In")

        // Wait for login to complete
        extendedWaitUntil(
            visible = mapOf("text" to "Dashboard"),
            timeout = 10000
        )

        // Navigate and interact
        scrollUntilVisible(text = "Profile", direction = Direction.DOWN)
        tapOn(text = "Profile")

        // Take screenshot for verification
        takeScreenshot("profile_page")

        // Test swipe gesture
        swipe(direction = Direction.LEFT)

        // Logout
        tapOn(id = "menu_button")
        longPressOn(text = "Logout")
        assertVisible(text = "Confirm Logout")
        tapOn(text = "Yes")

        // Verify logout
        assertVisible(text = "Welcome")
    }
}