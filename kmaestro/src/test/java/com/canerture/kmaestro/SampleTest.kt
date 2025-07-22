package com.canerture.kmaestro

import org.junit.Test

class SampleTest {

    @Test
    fun testLaunchAppCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "launch_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Launch App Commands ===")
        println(result)
    }

    @Test
    fun testAssertionCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "assertion_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Assertion Commands ===")
        println(result)
    }

    @Test
    fun testNavigationCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "navigation_test")

        val result = kMaestro.apply {
            back()
            killApp()
            killApp(appId = "com.example.app")
            stopApp()
            stopApp(appId = "com.example.app")
        }.build()

        println("=== Navigation Commands ===")
        println(result)
    }

    @Test
    fun testStateManagementCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "state_test")

        val result = kMaestro.apply {
            clearState()
            clearState(appId = "com.example.app")
            clearKeychain()

            setAirplaneMode(enabled = true)
            setAirplaneMode(enabled = false)
            toggleAirplaneMode()

            setLocation(latitude = 40.7128, longitude = -74.0060) // New York
        }.build()

        println("=== State Management Commands ===")
        println(result)
    }

    @Test
    fun testInputCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "input_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Input Commands ===")
        println(result)
    }

    @Test
    fun testTapCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "tap_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Tap Commands ===")
        println(result)
    }

    @Test
    fun testScrollCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "scroll_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Scroll Commands ===")
        println(result)
    }

    @Test
    fun testSwipeCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "swipe_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Swipe Commands ===")
        println(result)
    }

    @Test
    fun testWaitCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "wait_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Wait Commands ===")
        println(result)
    }

    @Test
    fun testMediaCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "media_test")

        val result = kMaestro.apply {
            addMedia("/path/to/image1.jpg", "/path/to/video.mp4")

            takeScreenshot()
            takeScreenshot("login_screen")

            startRecording()
            startRecording("test_session")
            stopRecording()
        }.build()

        println("=== Media Commands ===")
        println(result)
    }

    @Test
    fun testScriptCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "script_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Script Commands ===")
        println(result)
    }

    @Test
    fun testFlowControlCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "flow_test")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Flow Control Commands ===")
        println(result)
    }

    @Test
    fun testKeyPressCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "keypress_test")

        val result = kMaestro.apply {
            pressKey(KeyType.ENTER)
            pressKey(KeyType.BACK)
            pressKey(KeyType.HOME)
            pressKey(KeyType.VOLUME_UP)
            pressKey(KeyType.VOLUME_DOWN)
            pressKey(KeyType.POWER)
            pressKey(KeyType.TAB)
            pressKey(KeyType.BACKSPACE)
        }.build()

        println("=== Key Press Commands ===")
        println(result)
    }

    @Test
    fun testLinkCommands() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "link_test")

        val result = kMaestro.apply {
            openLink("https://example.com")
            openLink(
                url = "https://test.com",
                autoVerify = true,
                browser = true
            )
        }.build()

        println("=== Link Commands ===")
        println(result)
    }

    @Test
    fun testTravelCommand() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "travel_test")

        val result = kMaestro.apply {
            travel(
                points = listOf(
                    40.7128 to -74.0060, // New York
                    34.0522 to -118.2437, // Los Angeles
                    41.8781 to -87.6298  // Chicago
                ),
                speed = 5000
            )
        }.build()

        println("=== Travel Command ===")
        println(result)
    }

    @Test
    fun testComplexScenario() {
        val kMaestro = KMaestro(path = "maestro", yamlName = "complex_scenario")

        val result = kMaestro.apply {
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
        }.build()

        println("=== Complex Scenario ===")
        println(result)
    }
}