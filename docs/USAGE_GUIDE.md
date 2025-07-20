# 📘 KMaestro Usage Guide

This comprehensive guide will help you master KMaestro for mobile test automation. Learn through
practical examples and best practices.

## 🎯 Table of Contents

1. [Getting Started](#getting-started)
2. [Basic Patterns](#basic-patterns)
3. [Advanced Techniques](#advanced-techniques)
4. [Testing Scenarios](#testing-scenarios)
5. [Best Practices](#best-practices)
6. [Troubleshooting](#troubleshooting)

## 🚀 Getting Started

### Project Setup

First, add KMaestro to your project:

```kotlin
// build.gradle.kts
dependencies {
    testImplementation("com.canerture:kmaestro:1.0.0")
}
```

### Your First Test

Create a simple test to verify the setup:

```kotlin
import com.canerture.kmaestro.KMaestro

@Test
fun myFirstTest() {
    val yaml = KMaestro("test-flows", "first_test").apply {
        launchApp(appId = "com.example.myapp")
        assertVisible(text = "Welcome")
        takeScreenshot("app_launched")
    }.build()
    
    // YAML file is automatically saved to test-flows/first_test.yaml
    println("Test created: $yaml")
}
```

## 🔧 Basic Patterns

### 1. App Launch Patterns

#### Simple Launch

```kotlin
fun simpleLaunch() = KMaestro("flows", "launch").apply {
    launchApp()
    waitForAnimationToEnd()
}.build()
```

#### Launch with Clean State

```kotlin
fun cleanLaunch() = KMaestro("flows", "clean_launch").apply {
    launchApp(
        appId = "com.example.app",
        clearState = true,
        clearKeychain = true // iOS only
    )
    assertVisible(text = "Welcome")
}.build()
```

#### Launch with Test Configuration

```kotlin
fun testConfigLaunch() = KMaestro("flows", "test_config").apply {
    launchApp(
        appId = "com.example.app",
        arguments = mapOf(
            "testMode" to true,
            "mockApi" to true,
            "userId" to "test_user_123"
        ),
        permissions = mapOf(
            "notifications" to "allow",
            "location" to "deny"
        )
    )
}.build()
```

### 2. Element Selection Strategies

#### By Text Content

```kotlin
tapOn(text = "Login")
assertVisible(text = "Welcome back!")
```

#### By ID (Recommended)

```kotlin
tapOn(id = "login_button")
assertVisible(id = "dashboard_container")
```

#### By Index (When Multiple Elements Match)

```kotlin
tapOn(text = "Edit", index = 1) // Second "Edit" button
```

#### By Coordinates (Last Resort)

```kotlin
tapOn(point = "50%, 25%") // Center horizontally, 25% from top
```

### 3. Input Patterns

#### Form Filling

```kotlin
fun fillLoginForm(email: String, password: String) = KMaestro("flows", "login_form").apply {
    // Focus on email field and enter email
    tapOn(id = "email_field")
    inputText(email)
    
    // Move to password field
    tapOn(id = "password_field")
    inputText(password)
    
    // Hide keyboard before submitting
    hideKeyboard()
    
    // Submit form
    tapOn(text = "Login")
}.build()
```

#### Using Random Data

```kotlin
fun fillRegistrationForm() = KMaestro("flows", "registration").apply {
    tapOn(id = "email_field")
    inputRandomEmail()
    
    tapOn(id = "first_name_field")
    inputRandomPersonName()
    
    tapOn(id = "phone_field")
    inputRandomNumber(10)
    
    tapOn(id = "bio_field")
    inputRandomText(50)
}.build()
```

### 4. Assertion Patterns

#### Basic Visibility

```kotlin
// Element should be visible
assertVisible(text = "Success!")
assertVisible(id = "confirmation_dialog")

// Element should not be visible
assertNotVisible(text = "Loading...")
assertNotVisible(id = "error_banner")
```

#### State-based Assertions

```kotlin
assertVisible(
    text = "Submit",
    enabled = true,
    checked = false
)

assertVisible(
    id = "toggle_switch",
    checked = true,
    focused = false
)
```

#### AI-Powered Assertions

```kotlin
assertWithAI("The user profile shows correct personal information")
assertWithAI("All form fields are properly filled")
assertNoDefectsWithAI() // Catch visual bugs
```

## 🎨 Advanced Techniques

### 1. Complex Navigation Flows

#### Tab-based Navigation

```kotlin
fun navigateThroughTabs() = KMaestro("flows", "tab_navigation").apply {
    launchApp(appId = "com.example.app")
    
    // Navigate through tabs
    tapOn(text = "Home")
    assertVisible(text = "Welcome")
    takeScreenshot("home_tab")
    
    tapOn(text = "Profile")
    assertVisible(text = "Your Profile")
    takeScreenshot("profile_tab")
    
    tapOn(text = "Settings")
    assertVisible(text = "App Settings")
    takeScreenshot("settings_tab")
}.build()
```

#### Deep Link Navigation

```kotlin
fun testDeepLink() = KMaestro("flows", "deep_link").apply {
    openLink("myapp://product/12345")
    waitForAnimationToEnd()
    assertVisible(text = "Product Details")
    assertVisible(text = "iPhone 15 Pro")
}.build()
```

### 2. Scroll and Swipe Patterns

#### Long List Navigation

```kotlin
fun navigateLongList() = KMaestro("flows", "long_list").apply {
    launchApp(appId = "com.example.app")
    
    // Scroll to find specific item
    scrollUntilVisible(
        text = "Item #50",
        direction = Direction.DOWN,
        timeout = 30000,
        speed = 60
    )
    
    tapOn(text = "Item #50")
    assertVisible(text = "Item Details")
}.build()
```

#### Carousel/Swiper Interaction

```kotlin
fun testCarousel() = KMaestro("flows", "carousel").apply {
    launchApp(appId = "com.example.app")
    
    // Swipe through carousel items
    repeat(3, listOf(
        "- swipe: { direction: LEFT, duration: 500 }",
        "- waitForAnimationToEnd"
    ))
    
    // Verify we reached the expected item
    assertVisible(text = "Slide 4")
}.build()
```

### 3. Device State Management

#### Network Testing

```kotlin
fun testOfflineMode() = KMaestro("flows", "offline_test").apply {
    launchApp(appId = "com.example.app")
    
    // Go offline
    setAirplaneMode(true)
    
    // Try to refresh content
    tapOn(text = "Refresh")
    
    // Verify offline message
    assertVisible(text = "No internet connection")
    
    // Go back online
    setAirplaneMode(false)
    
    // Verify content loads
    waitForAnimationToEnd()
    assertNotVisible(text = "No internet connection")
}.build()
```

#### Location-based Testing

```kotlin
fun testLocationFeature() = KMaestro("flows", "location_test").apply {
    launchApp(
        appId = "com.example.maps",
        permissions = mapOf("location" to "allow")
    )
    
    // Set location to New York
    setLocation(40.7128, -74.0060)
    
    tapOn(text = "Find nearby")
    assertVisible(text = "New York")
    
    // Simulate movement to Los Angeles
    travel(listOf(
        40.7128 to -74.0060, // New York
        34.0522 to -118.2437  // Los Angeles
    ), speed = 5000)
    
    assertVisible(text = "Los Angeles")
}.build()
```

### 4. Advanced Flow Control

#### Conditional Logic

```kotlin
fun conditionalFlow() = KMaestro("flows", "conditional").apply {
    launchApp(appId = "com.example.app")
    
    // Check if onboarding is needed
    extendedWaitUntil(
        visible = mapOf("text" to "Skip Tutorial"),
        timeout = 5000
    )
    
    // Skip onboarding if present
    tapOn(text = "Skip Tutorial")
    
    // Now proceed with main flow
    assertVisible(text = "Dashboard")
}.build()
```

#### Retry Mechanisms

```kotlin
fun retryPattern() = KMaestro("flows", "retry").apply {
    launchApp(appId = "com.example.app")
    
    // Retry flaky network operation
    retry(3, listOf(
        "- tapOn: \"Sync Data\"",
        "- assertVisible: \"Sync Complete\""
    ))
}.build()
```

## 📱 Testing Scenarios

### 1. Login Flow Testing

```kotlin
class LoginTests {
    
    @Test
    fun testValidLogin() {
        KMaestro("login-tests", "valid_login").apply {
            launchApp(appId = APP_ID, clearState = true)
            performLogin("valid@email.com", "validPassword")
            verifyLoginSuccess()
        }.build()
    }
    
    @Test
    fun testInvalidLogin() {
        KMaestro("login-tests", "invalid_login").apply {
            launchApp(appId = APP_ID, clearState = true)
            performLogin("invalid@email.com", "wrongPassword")
            verifyLoginError()
        }.build()
    }
    
    private fun KMaestro.performLogin(email: String, password: String) {
        tapOn(text = "Login")
        
        tapOn(id = "email_field")
        inputText(email)
        
        tapOn(id = "password_field")
        inputText(password)
        
        hideKeyboard()
        tapOn(id = "login_button")
    }
    
    private fun KMaestro.verifyLoginSuccess() {
        assertVisible(text = "Welcome")
        assertNotVisible(text = "Login")
        takeScreenshot("login_success")
    }
    
    private fun KMaestro.verifyLoginError() {
        assertVisible(text = "Invalid credentials")
        assertVisible(text = "Login") // Still on login screen
        takeScreenshot("login_error")
    }
}
```

### 2. E-commerce Testing

```kotlin
class EcommerceTests {
    
    @Test
    fun testProductPurchase() {
        KMaestro("ecommerce", "purchase_flow").apply {
            launchApp(appId = SHOP_APP_ID, clearState = true)
            
            // Search and select product
            searchProduct("iPhone 15")
            selectProduct("iPhone 15 Pro")
            
            // Add to cart
            addToCart()
            verifyCartContents()
            
            // Checkout
            proceedToCheckout()
            fillShippingInfo()
            completeOrder()
            
            // Verify success
            verifyOrderComplete()
        }.build()
    }
    
    private fun KMaestro.searchProduct(query: String) {
        tapOn(id = "search_bar")
        inputText(query)
        pressKey(KeyType.ENTER)
        waitForAnimationToEnd()
    }
    
    private fun KMaestro.selectProduct(productName: String) {
        scrollUntilVisible(text = productName)
        tapOn(text = productName)
        assertVisible(text = "Product Details")
    }
    
    private fun KMaestro.addToCart() {
        scrollUntilVisible(text = "Add to Cart")
        tapOn(text = "Add to Cart")
        assertVisible(text = "Added to Cart")
    }
    
    // ... more helper methods
}
```

### 3. Form Testing

```kotlin
@Test
fun testComplexForm() {
    KMaestro("forms", "user_registration").apply {
        launchApp(appId = APP_ID)
        
        tapOn(text = "Sign Up")
        
        // Personal Information
        tapOn(id = "first_name")
        inputText("John")
        
        tapOn(id = "last_name") 
        inputText("Doe")
        
        tapOn(id = "email")
        inputRandomEmail()
        
        tapOn(id = "phone")
        inputRandomNumber(10)
        
        // Address Information
        scroll(Direction.DOWN)
        
        tapOn(id = "address")
        inputText("123 Main St")
        
        tapOn(id = "city")
        inputText("New York")
        
        // Select state from dropdown
        tapOn(id = "state_dropdown")
        tapOn(text = "New York")
        
        tapOn(id = "zip")
        inputText("10001")
        
        hideKeyboard()
        
        // Submit form
        tapOn(text = "Create Account")
        
        // Verify success
        assertVisible(text = "Account Created")
        takeScreenshot("registration_success")
        
    }.build()
}
```

## ✅ Best Practices

### 1. Test Structure

#### Use Page Object Pattern

```kotlin
class LoginPage(private val kMaestro: KMaestro) {
    fun enterEmail(email: String) {
        kMaestro.tapOn(id = "email_field")
        kMaestro.inputText(email)
    }
    
    fun enterPassword(password: String) {
        kMaestro.tapOn(id = "password_field")
        kMaestro.inputText(password)
    }
    
    fun submit() {
        kMaestro.hideKeyboard()
        kMaestro.tapOn(id = "login_button")
    }
    
    fun verifyLoginSuccess() {
        kMaestro.assertVisible(text = "Dashboard")
    }
}
```

#### Create Reusable Components

```kotlin
fun KMaestro.waitForPageLoad(pageTitle: String) {
    waitForAnimationToEnd()
    assertVisible(text = pageTitle)
}

fun KMaestro.dismissKeyboard() {
    hideKeyboard()
    // Additional logic if needed
}

fun KMaestro.takeContextualScreenshot(context: String) {
    val timestamp = System.currentTimeMillis()
    takeScreenshot("${context}_${timestamp}")
}
```

### 2. Error Handling

#### Graceful Failures

```kotlin
fun robustTapOn(elementText: String) = KMaestro("flows", "robust").apply {
    // First, try to find element
    extendedWaitUntil(
        visible = mapOf("text" to elementText),
        timeout = 5000
    )
    
    // Then tap on it
    tapOn(text = elementText)
    
    // Verify something changed
    waitForAnimationToEnd()
}.build()
```

### 3. Test Data Management

#### Use Test Data Classes

```kotlin
data class TestUser(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

fun createTestUser() = TestUser(
    email = "test_${System.currentTimeMillis()}@example.com",
    password = "TestPass123!",
    firstName = "Test",
    lastName = "User"
)

@Test
fun testWithTestData() {
    val user = createTestUser()
    
    KMaestro("user-tests", "registration").apply {
        launchApp(appId = APP_ID)
        fillRegistrationForm(user)
        verifyRegistrationSuccess()
    }.build()
}
```

### 4. Performance Considerations

#### Optimize Waits

```kotlin
// ✅ Good - Wait for specific condition
waitForAnimationToEnd(timeout = 3000)
assertVisible(text = "Content Loaded")

// ✅ Good - Use appropriate timeouts
extendedWaitUntil(
    visible = mapOf("text" to "Search Results"),
    timeout = 10000 // Longer timeout for network operations
)

// ❌ Avoid - Fixed delays
// Thread.sleep(3000)
```

#### Use Appropriate Element Selectors

```kotlin
// ✅ Preferred - ID selectors are fastest
tapOn(id = "submit_button")

// ✅ Good - Text selectors are readable
tapOn(text = "Submit")

// ⚠️ Use sparingly - Coordinate selectors are brittle  
tapOn(point = "50%, 90%")
```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Element Not Found

```kotlin
// Problem: Element not immediately visible
tapOn(text = "Submit") // May fail

// Solution: Wait for element
extendedWaitUntil(visible = mapOf("text" to "Submit"))
tapOn(text = "Submit")

// Or: Scroll to find element
scrollUntilVisible(text = "Submit")
tapOn(text = "Submit")
```

#### 2. Timing Issues

```kotlin
// Problem: Actions happening too fast
tapOn(text = "Next")
assertVisible(text = "Page 2") // May fail

// Solution: Wait for animations
tapOn(text = "Next")
waitForAnimationToEnd()
assertVisible(text = "Page 2")
```

#### 3. Keyboard Interference

```kotlin
// Problem: Keyboard blocking elements
inputText("test@example.com")
tapOn(text = "Submit") // May not be visible

// Solution: Hide keyboard first
inputText("test@example.com")
hideKeyboard()
tapOn(text = "Submit")
```

#### 4. Multiple Elements with Same Text

```kotlin
// Problem: Multiple "Edit" buttons
tapOn(text = "Edit") // Ambiguous

// Solution: Use index
tapOn(text = "Edit", index = 1) // Second Edit button

// Or: Use more specific selector
tapOn(id = "profile_edit_button")
```

### Debug Techniques

#### Add Screenshots for Debugging

```kotlin
fun debugFlow() = KMaestro("debug", "investigation").apply {
    launchApp(appId = APP_ID)
    takeScreenshot("01_app_launched")
    
    tapOn(text = "Login")
    takeScreenshot("02_login_screen")
    
    inputText("user@example.com")
    takeScreenshot("03_email_entered")
    
    // Continue with more screenshots...
}.build()
```

#### Use AI Assertions for Validation

```kotlin
// Instead of specific element checks
assertWithAI("The login form is displayed and ready for input")
assertWithAI("User successfully navigated to the dashboard")
assertNoDefectsWithAI() // Catch unexpected visual issues
```

## 📚 Additional Resources

- [KMaestro API Reference](../API_REFERENCE.md)
- [Maestro Official Documentation](https://maestro.mobile.dev)
- [Example Test Suites](../examples/)
- [Common Patterns Cookbook](COOKBOOK.md)

---

**Need help?** Open an issue on our [GitHub repository](https://github.com/your-org/kmaestro/issues)
or check our [FAQ](FAQ.md).