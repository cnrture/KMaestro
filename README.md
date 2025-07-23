# 🎭 KMaestro

[![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-APACHE-yellow.svg)](https://opensource.org/licenses/APACHE)

**KMaestro** is a type-safe, fluent Kotlin DSL for creating [Maestro](https://maestro.mobile.dev)
mobile testing flows. Write beautiful, maintainable test automation code with full IDE support and
comprehensive API coverage.

## ✨ Features

- 🎯 **Complete Maestro API Coverage** - All 32+ Maestro commands supported
- 🛡️ **Type Safety** - Compile-time validation and IntelliSense support
- 🎨 **Fluent DSL** - Write readable test flows with natural Kotlin syntax
- 📱 **Cross-Platform** - Supports both Android and iOS testing
- 🤖 **AI Integration** - Built-in support for Maestro's AI features
- 📸 **Rich Media Support** - Screenshots, video recording, and media handling
- 🔄 **Advanced Flow Control** - Retry mechanisms, conditions, and loops
- 📚 **Comprehensive Documentation** - Detailed KDoc for every function

## 🚀 Quick Start

### Installation

Add KMaestro to your project:

```kotlin
dependencies {
    implementation("com.github.canerture:kmaestro:0.0.1")
}
```

### Basic Usage

```kotlin
import com.canerture.kmaestro.KMaestro

fun createLoginTest() {
    val loginFlow = KMaestro(
        path = "maestro",
        yamlName = "login_test"
    ).apply {
        // Launch the app
        launchApp(
            appId = "com.example.app",
            clearState = true
        )
        
        // Perform login
        tapOn(id = "email_field")
        inputText("user@example.com")
        
        tapOn(id = "password_field")
        inputText("password123")
        
        tapOn(text = "Login")
        
        // Verify successful login
        assertVisible(text = "Welcome")
        takeScreenshot("login_success")
    }.build()
    
    println("Generated YAML saved to: maestro/login_test.yaml")
}
```

## 📖 API Reference

### 🚀 App Lifecycle

#### Launch App

```kotlin
// Basic launch
launchApp()

// Launch with options
launchApp(
    appId = "com.example.app",
    clearState = true,
    clearKeychain = true,
    permissions = mapOf("notifications" to "deny"),
    arguments = mapOf("testMode" to true)
)
```

#### App Management

```kotlin
stopApp("com.example.app")
killApp("com.example.app")
clearState("com.example.app")
```

### 🎯 Element Interactions

#### Tapping

```kotlin
// Basic tapping
tapOn(text = "Login")
tapOn(id = "submit_button")
tapOn(point = "50%, 25%")

// Advanced tapping
tapOn(
    text = "Button",
    repeat = 3,
    delay = 500,
    retryTapIfNoChange = true
)

// Double tap and long press
doubleTapOn(text = "Image")
longPressOn(id = "context_menu")
```

#### Text Input

```kotlin
// Basic input
inputText("Hello World")

// Random data generation
inputRandomEmail()
inputRandomPersonName()
inputRandomNumber(10)
inputRandomText(20)

// Text manipulation
eraseText(5)
copyTextFrom(id = "source_field")
pasteText()
```

### ✅ Assertions

#### Visibility Assertions

```kotlin
// Basic visibility
assertVisible(text = "Welcome")
assertNotVisible(id = "loading_spinner")

// Advanced assertions
assertVisible(
    text = "Submit",
    enabled = true,
    checked = false,
    focused = true
)
```

#### AI-Powered Assertions

```kotlin
assertWithAI("The login form is displayed correctly")
assertNoDefectsWithAI()
assertTrue("window.userLoggedIn === true")
```

### 🎬 Gestures & Navigation

#### Scrolling

```kotlin
scroll(Direction.DOWN)
scrollUntilVisible(
    text = "Terms and Conditions",
    direction = Direction.DOWN,
    timeout = 30000
)
```

#### Swiping

```kotlin
// Direction-based swiping
swipe(direction = Direction.LEFT)

// Coordinate-based swiping
swipe(
    startX = "10%", startY = "50%",
    endX = "90%", endY = "50%"
)

// Element-based swiping
swipe(
    from = mapOf("id" to "carousel"),
    direction = Direction.RIGHT
)
```

#### Navigation

```kotlin
back()
pressKey(KeyType.HOME)
pressKey(KeyType.VOLUME_UP)
```

### 🎥 Media & Recording

#### Screenshots

```kotlin
takeScreenshot("login_screen")
```

#### Video Recording

```kotlin
startRecording("test_session")
// ... test steps ...
stopRecording()
```

#### Media Library

```kotlin
addMedia("/path/to/image.jpg", "/path/to/video.mp4")
```

### 📱 Device Control

#### Location & Network

```kotlin
setLocation(40.7128, -74.0060) // New York City
setAirplaneMode(true)
toggleAirplaneMode()
```

#### GPS Travel Simulation

```kotlin
travel(listOf(
    40.7128 to -74.0060, // New York
    34.0522 to -118.2437  // Los Angeles
), speed = 5000)
```

### 🔄 Flow Control

#### Waiting

```kotlin
waitForAnimationToEnd(timeout = 5000)
extendedWaitUntil(
    visible = mapOf("text" to "Loading complete"),
    timeout = 15000
)
```

#### Repetition & Retry

```kotlin
repeat(3, listOf(
    "- tapOn: \"Next\"",
    "- waitForAnimationToEnd"
))

retry(5, listOf(
    "- tapOn: \"Retry Button\"",
    "- assertVisible: \"Success\""
))
```

#### Flow Management

```kotlin
runFlow("loginFlow")
runScript("console.log('Test started')")
evalScript("window.testMode = true")
```

## 🎨 Advanced Examples

### Complete E-commerce Test Flow

```kotlin
fun createEcommerceTest() {
    KMaestro("maestro", "ecommerce_flow").apply {
        // Launch and setup
        launchApp(
            appId = "com.example.shop",
            clearState = true,
            arguments = mapOf("testMode" to true)
        )
        
        waitForAnimationToEnd()
        takeScreenshot("app_launch")
        
        // Search for product
        tapOn(id = "search_bar")
        inputText("iPhone 15")
        pressKey(KeyType.ENTER)
        
        // Select product
        scrollUntilVisible(text = "iPhone 15 Pro")
        tapOn(text = "iPhone 15 Pro")
        
        // Add to cart
        scrollUntilVisible(text = "Add to Cart")
        tapOn(text = "Add to Cart")
        assertVisible(text = "Added to Cart")
        
        // Go to cart
        tapOn(id = "cart_icon")
        assertVisible(text = "Shopping Cart")
        
        // Checkout process
        tapOn(text = "Checkout")
        
        // Fill shipping info
        tapOn(id = "email_field")
        inputRandomEmail()
        
        tapOn(id = "address_field")
        inputText("123 Test Street")
        
        tapOn(id = "city_field")
        inputText("New York")
        
        hideKeyboard()
        
        // Complete order
        swipe(direction = Direction.DOWN)
        tapOn(text = "Place Order")
        
        // Verify success
        assertWithAI("Order confirmation is displayed")
        takeScreenshot("order_complete")
        
    }.build()
}
```

### Conditional Testing with AI

```kotlin
fun createConditionalTest() {
    KMaestro("maestro", "conditional_flow").apply {
        launchApp(appId = "com.example.app")
        
        // Check if onboarding is needed
        extendedWaitUntil(
            visible = mapOf("text" to "Welcome"),
            timeout = 5000
        )
        
        // Handle onboarding flow
        repeat(3, listOf(
            "- swipe: { direction: LEFT }",
            "- waitForAnimationToEnd"
        ))
        
        tapOn(text = "Get Started")
        
        // Use AI to verify proper setup
        assertWithAI("User has completed onboarding successfully")
        assertNoDefectsWithAI()
        
    }.build()
}
```

## 🛠️ Best Practices

### 1. **Always Use Type-Safe Element Selection**

```kotlin
// ✅ Good
tapOn(id = "login_button")
assertVisible(text = "Welcome")

// ❌ Avoid
tapOn(point = "100, 200") // Hard to maintain
```

### 2. **Add Meaningful Screenshots**

```kotlin
takeScreenshot("before_login")
// ... login steps ...
takeScreenshot("after_login")
```

### 3. **Use Proper Waiting**

```kotlin
// ✅ Wait for specific conditions
waitForAnimationToEnd()
assertVisible(text = "Loading Complete")

// ❌ Don't use fixed delays
// Thread.sleep(3000)
```

### 4. **Leverage AI for Complex Assertions**

```kotlin
assertWithAI("The user profile shows correct information")
assertNoDefectsWithAI() // Catch visual issues
```

### 5. **Structure Your Tests**

```kotlin
fun loginTest() = KMaestro("flows", "login").apply {
    launchApp(appId = APP_ID, clearState = true)
    performLogin("user@test.com", "password")
    verifyLoginSuccess()
}.build()

private fun KMaestro.performLogin(email: String, password: String) {
    tapOn(id = "email_field")
    inputText(email)
    tapOn(id = "password_field") 
    inputText(password)
    tapOn(text = "Login")
}

private fun KMaestro.verifyLoginSuccess() {
    assertVisible(text = "Dashboard")
    assertNotVisible(text = "Login")
}
```

## 📊 Supported Maestro Commands

KMaestro provides 100% coverage of Maestro's command set:

| Category | Commands | Coverage |
|----------|----------|----------|
| **App Lifecycle** | `launchApp`, `stopApp`, `killApp`, `clearState` | ✅ |
| **Interactions** | `tapOn`, `doubleTapOn`, `longPressOn`, `swipe` | ✅ |
| **Input** | `inputText`, `inputRandom*`, `eraseText`, `pasteText` | ✅ |
| **Assertions** | `assertVisible`, `assertNotVisible`, `assertTrue` | ✅ |
| **AI Features** | `assertWithAI`, `extractTextWithAI`, `assertNoDefectsWithAI` | ✅ |
| **Flow Control** | `repeat`, `retry`, `runFlow`, `extendedWaitUntil` | ✅ |
| **Device** | `setLocation`, `setAirplaneMode`, `travel`, `pressKey` | ✅ |
| **Media** | `takeScreenshot`, `startRecording`, `addMedia` | ✅ |
| **Scrolling** | `scroll`, `scrollUntilVisible` | ✅ |
| **Scripts** | `evalScript`, `runScript` | ✅ |

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🔗 Links

- [Maestro Documentation](https://maestro.mobile.dev)

## ⭐ Acknowledgments

- [Maestro Team](https://github.com/mobile-dev-inc/maestro) for the amazing mobile testing framework
- Kotlin community for the excellent language and ecosystem
- All contributors who make this project better

---

Made with ❤️ for the mobile testing community
