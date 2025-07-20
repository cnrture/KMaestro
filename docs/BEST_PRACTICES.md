# 🏆 KMaestro Best Practices

This document outlines proven patterns and best practices for writing maintainable, reliable, and
efficient mobile tests with KMaestro.

## 📋 Table of Contents

1. [Code Organization](#code-organization)
2. [Element Selection](#element-selection)
3. [Test Structure](#test-structure)
4. [Data Management](#data-management)
5. [Error Handling](#error-handling)
6. [Performance Optimization](#performance-optimization)
7. [Debugging & Maintenance](#debugging--maintenance)
8. [Team Collaboration](#team-collaboration)

## 🏗️ Code Organization

### 1. Use Clear Naming Conventions

```kotlin
// ✅ Good - Descriptive test names
@Test
fun testValidLoginWithEmailAndPassword() { }

@Test
fun testInvalidLoginShowsErrorMessage() { }

@Test
fun testPasswordResetSendsEmailConfirmation() { }

// ❌ Avoid - Vague names
@Test 
fun test1() { }

@Test
fun loginTest() { }
```

### 2. Group Related Tests

```kotlin
class LoginTests {
    @Test fun testValidLogin() { }
    @Test fun testInvalidLogin() { }
    @Test fun testForgotPassword() { }
}

class CheckoutTests {
    @Test fun testAddToCart() { }
    @Test fun testRemoveFromCart() { }
    @Test fun testCompleteCheckout() { }
}

class OnboardingTests {
    @Test fun testFirstLaunch() { }
    @Test fun testSkipOnboarding() { }
    @Test fun testCompleteOnboarding() { }
}
```

### 3. Create Reusable Flow Components

```kotlin
// Common flows as extension functions
fun KMaestro.performLogin(email: String, password: String) {
    tapOn(id = "email_field")
    inputText(email)
    tapOn(id = "password_field")
    inputText(password)
    hideKeyboard()
    tapOn(id = "login_button")
}

fun KMaestro.navigateToProfile() {
    tapOn(id = "bottom_nav_profile")
    waitForAnimationToEnd()
    assertVisible(text = "Profile")
}

// Usage
@Test
fun testProfileUpdate() {
    KMaestro("profile", "update_test").apply {
        launchApp(appId = APP_ID, clearState = true)
        performLogin("test@example.com", "password")
        navigateToProfile()
        // ... continue with profile-specific steps
    }.build()
}
```

## 🎯 Element Selection

### 1. Prefer ID Selectors

```kotlin
// ✅ Best - ID selectors are most reliable
tapOn(id = "submit_button")
assertVisible(id = "success_message")

// ✅ Good - Text selectors are readable
tapOn(text = "Submit")
assertVisible(text = "Success!")

// ⚠️ Use sparingly - Coordinate selectors are brittle
tapOn(point = "50%, 90%")
```

### 2. Handle Multiple Elements Gracefully

```kotlin
// ✅ Use index for multiple identical elements
tapOn(text = "Edit", index = 1) // Second Edit button

// ✅ Use more specific selectors when possible
tapOn(id = "profile_edit_button") // Better than generic "Edit"

// ✅ Combine selectors for precision
assertVisible(
    text = "Submit",
    enabled = true,
    index = 0
)
```

### 3. Create Element Abstractions

```kotlin
// Define element constants
object LoginElements {
    const val EMAIL_FIELD = "email_input"
    const val PASSWORD_FIELD = "password_input"
    const val LOGIN_BUTTON = "login_submit"
    const val ERROR_MESSAGE = "login_error_text"
}

// Usage
fun KMaestro.performLogin(email: String, password: String) {
    tapOn(id = LoginElements.EMAIL_FIELD)
    inputText(email)
    tapOn(id = LoginElements.PASSWORD_FIELD)
    inputText(password)
    tapOn(id = LoginElements.LOGIN_BUTTON)
}
```

## 🏗️ Test Structure

### 1. Follow AAA Pattern (Arrange, Act, Assert)

```kotlin
@Test
fun testProductPurchase() {
    KMaestro("ecommerce", "purchase").apply {
        // Arrange - Setup initial state
        launchApp(appId = SHOP_APP_ID, clearState = true)
        performLogin(TEST_USER_EMAIL, TEST_USER_PASSWORD)
        navigateToProduct("iPhone 15")
        
        // Act - Perform the action being tested
        tapOn(text = "Add to Cart")
        tapOn(id = "cart_icon")
        tapOn(text = "Checkout")
        
        // Assert - Verify expected outcomes
        assertVisible(text = "Order Confirmed")
        assertVisible(text = "iPhone 15")
        takeScreenshot("purchase_complete")
    }.build()
}
```

### 2. Use Page Object Pattern

```kotlin
class LoginPage(private val kMaestro: KMaestro) {
    fun enterCredentials(email: String, password: String): LoginPage {
        kMaestro.tapOn(id = "email_field")
        kMaestro.inputText(email)
        kMaestro.tapOn(id = "password_field")
        kMaestro.inputText(password)
        return this
    }
    
    fun submit(): DashboardPage {
        kMaestro.hideKeyboard()
        kMaestro.tapOn(id = "login_button")
        kMaestro.waitForAnimationToEnd()
        return DashboardPage(kMaestro)
    }
    
    fun verifyLoginError(): LoginPage {
        kMaestro.assertVisible(text = "Invalid credentials")
        return this
    }
}

class DashboardPage(private val kMaestro: KMaestro) {
    fun verifyLoaded(): DashboardPage {
        kMaestro.assertVisible(text = "Dashboard")
        kMaestro.assertNotVisible(text = "Login")
        return this
    }
}

// Usage
@Test
fun testLogin() {
    val kMaestro = KMaestro("login", "valid_login").apply {
        launchApp(appId = APP_ID, clearState = true)
    }
    
    LoginPage(kMaestro)
        .enterCredentials("user@test.com", "password")
        .submit()
        .verifyLoaded()
    
    kMaestro.build()
}
```

### 3. Implement Test Data Builders

```kotlin
data class TestUser(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String
)

class TestUserBuilder {
    private var email = "test@example.com"
    private var password = "TestPass123!"
    private var firstName = "Test"
    private var lastName = "User"
    private var phone = "1234567890"
    
    fun withEmail(email: String) = apply { this.email = email }
    fun withPassword(password: String) = apply { this.password = password }
    fun withName(firstName: String, lastName: String) = apply {
        this.firstName = firstName
        this.lastName = lastName
    }
    fun withPhone(phone: String) = apply { this.phone = phone }
    
    fun build() = TestUser(email, password, firstName, lastName, phone)
}

// Usage
@Test
fun testRegistration() {
    val testUser = TestUserBuilder()
        .withEmail("unique_${System.currentTimeMillis()}@test.com")
        .withName("John", "Doe")
        .build()
    
    KMaestro("registration", "new_user").apply {
        launchApp(appId = APP_ID)
        fillRegistrationForm(testUser)
        verifyRegistrationSuccess()
    }.build()
}
```

## 💾 Data Management

### 1. Use Environment-Specific Configuration

```kotlin
object TestConfig {
    val APP_ID = when (System.getenv("TEST_ENV") ?: "dev") {
        "prod" -> "com.company.app"
        "staging" -> "com.company.app.staging"
        else -> "com.company.app.dev"
    }
    
    val BASE_URL = when (System.getenv("TEST_ENV") ?: "dev") {
        "prod" -> "https://api.company.com"
        "staging" -> "https://staging-api.company.com"
        else -> "https://dev-api.company.com"
    }
}
```

### 2. Generate Dynamic Test Data

```kotlin
object TestDataGenerator {
    fun generateUniqueEmail() = "test_${System.currentTimeMillis()}@example.com"
    
    fun generateUser() = TestUser(
        email = generateUniqueEmail(),
        password = "TestPass123!",
        firstName = "Test",
        lastName = "User_${System.currentTimeMillis()}"
    )
    
    fun generateProduct() = TestProduct(
        name = "Test Product ${Random.nextInt(1000)}",
        price = Random.nextDouble(10.0, 100.0),
        category = listOf("Electronics", "Clothing", "Books").random()
    )
}
```

### 3. Clean Up Test Data

```kotlin
class DatabaseTestRule {
    private val createdUsers = mutableListOf<String>()
    private val createdOrders = mutableListOf<String>()
    
    fun createTestUser(): TestUser {
        val user = TestDataGenerator.generateUser()
        createdUsers.add(user.email)
        return user
    }
    
    fun cleanup() {
        // Clean up test data after test execution
        createdUsers.forEach { email ->
            // Delete user via API
        }
        createdOrders.forEach { orderId ->
            // Delete order via API
        }
    }
}
```

## 🛡️ Error Handling

### 1. Implement Robust Element Interactions

```kotlin
fun KMaestro.robustTapOn(text: String, maxRetries: Int = 3) {
    repeat(maxRetries) { attempt ->
        try {
            // Wait for element to be visible
            extendedWaitUntil(
                visible = mapOf("text" to text),
                timeout = 5000
            )
            
            // Tap on element
            tapOn(text = text)
            
            // Verify something changed
            waitForAnimationToEnd()
            return
        } catch (e: Exception) {
            if (attempt == maxRetries - 1) throw e
            // Take screenshot for debugging
            takeScreenshot("retry_attempt_${attempt + 1}")
        }
    }
}
```

### 2. Handle Different App States

```kotlin
fun KMaestro.handleOnboardingIfPresent() {
    try {
        // Check if onboarding is present
        extendedWaitUntil(
            visible = mapOf("text" to "Welcome to App"),
            timeout = 3000
        )
        
        // Skip onboarding
        repeat(3) {
            swipe(direction = Direction.LEFT)
            waitForAnimationToEnd()
        }
        tapOn(text = "Get Started")
        
    } catch (e: Exception) {
        // Onboarding not present, continue normally
    }
}
```

### 3. Graceful Failure Recovery

```kotlin
@Test
fun testWithRecovery() {
    KMaestro("recovery", "test_with_fallback").apply {
        launchApp(appId = APP_ID, clearState = true)
        
        try {
            // Primary flow
            performOptimizedLogin()
        } catch (e: Exception) {
            // Fallback flow
            takeScreenshot("primary_flow_failed")
            performBasicLogin()
        }
        
        // Continue with rest of test
        verifyDashboardLoaded()
    }.build()
}

private fun KMaestro.performOptimizedLogin() {
    // Fast login using deep link or API
    openLink("myapp://auto-login?token=test_token")
    waitForAnimationToEnd()
}

private fun KMaestro.performBasicLogin() {
    // Manual login as fallback
    tapOn(text = "Login")
    performLogin("test@example.com", "password")
}
```

## ⚡ Performance Optimization

### 1. Optimize Wait Strategies

```kotlin
// ✅ Good - Specific waits with appropriate timeouts
fun KMaestro.waitForNetworkRequest() {
    extendedWaitUntil(
        notVisible = mapOf("text" to "Loading..."),
        timeout = 15000 // Longer for network operations
    )
}

fun KMaestro.waitForUITransition() {
    waitForAnimationToEnd(timeout = 3000) // Shorter for UI animations
}

// ❌ Avoid - Fixed delays
fun KMaestro.badWaitPattern() {
    Thread.sleep(3000) // Don't do this
}
```

### 2. Minimize App State Resets

```kotlin
class LoginTests {
    companion object {
        private var isLoggedIn = false
        
        @BeforeAll
        @JvmStatic
        fun loginOnce() {
            if (!isLoggedIn) {
                KMaestro("setup", "login_once").apply {
                    launchApp(appId = APP_ID, clearState = true)
                    performLogin(TEST_USER_EMAIL, TEST_USER_PASSWORD)
                }.build()
                isLoggedIn = true
            }
        }
    }
    
    @Test
    fun testFeatureA() {
        // Test assumes user is already logged in
        KMaestro("features", "test_a").apply {
            // Navigate to feature A and test
        }.build()
    }
    
    @Test
    fun testFeatureB() {
        // Test assumes user is already logged in
        KMaestro("features", "test_b").apply {
            // Navigate to feature B and test
        }.build()
    }
}
```

### 3. Use Efficient Element Selectors

```kotlin
// ✅ Efficient - Direct ID selector
tapOn(id = "submit_button")

// ✅ Good - Text selector with context
assertVisible(text = "Success", enabled = true)

// ⚠️ Less efficient - Multiple scrolls
scrollUntilVisible(text = "Rarely Used Feature")
tapOn(text = "Rarely Used Feature")

// ✅ Better - Use deep link if available
openLink("myapp://rarely-used-feature")
```

## 🔧 Debugging & Maintenance

### 1. Add Comprehensive Logging

```kotlin
fun KMaestro.withLogging(action: String, block: KMaestro.() -> Unit) {
    println("Starting: $action")
    takeScreenshot("before_$action")
    
    val startTime = System.currentTimeMillis()
    try {
        block()
        val duration = System.currentTimeMillis() - startTime
        println("Completed: $action in ${duration}ms")
        takeScreenshot("after_$action")
    } catch (e: Exception) {
        println("Failed: $action - ${e.message}")
        takeScreenshot("failed_$action")
        throw e
    }
}

// Usage
@Test
fun testWithLogging() {
    KMaestro("debug", "logged_test").apply {
        launchApp(appId = APP_ID)
        
        withLogging("login") {
            performLogin("test@example.com", "password")
        }
        
        withLogging("navigate_to_profile") {
            navigateToProfile()
        }
    }.build()
}
```

### 2. Create Debug Utilities

```kotlin
object DebugUtils {
    fun KMaestro.dumpScreenInfo(label: String) {
        takeScreenshot("debug_${label}")
        
        // Add debug information to YAML
        evalScript("""
            console.log('=== DEBUG: $label ===');
            console.log('Timestamp: ' + new Date().toISOString());
            console.log('Screen size: ' + screen.width + 'x' + screen.height);
            console.log('Focused element: ' + document.activeElement.tagName);
            console.log('====================');
        """)
    }
    
    fun KMaestro.waitWithDebug(condition: String, timeout: Int = 10000) {
        val startTime = System.currentTimeMillis()
        
        extendedWaitUntil(
            visible = mapOf("text" to condition),
            timeout = timeout
        )
        
        val actualTime = System.currentTimeMillis() - startTime
        println("Waited ${actualTime}ms for condition: $condition")
    }
}
```

### 3. Version Control for Test Files

```kotlin
// Include metadata in generated YAML files
fun KMaestro.withMetadata(): KMaestro {
    evalScript("""
        // Test metadata
        window.testMetadata = {
            generated: '${java.time.Instant.now()}',
            version: '${System.getProperty("test.version", "unknown")}',
            platform: '${System.getProperty("os.name")}',
            user: '${System.getProperty("user.name")}'
        };
        console.log('Test metadata:', window.testMetadata);
    """)
    return this
}
```

## 👥 Team Collaboration

### 1. Standardize Test Conventions

```kotlin
// Create team conventions documentation
object TeamConventions {
    // Naming conventions
    const val TEST_CLASS_SUFFIX = "Tests"
    const val TEST_METHOD_PREFIX = "test"
    
    // Common timeouts
    const val SHORT_TIMEOUT = 3000
    const val MEDIUM_TIMEOUT = 10000
    const val LONG_TIMEOUT = 30000
    
    // Screenshot naming
    fun screenshotName(feature: String, action: String) = 
        "${feature}_${action}_${System.currentTimeMillis()}"
}

// Use in tests
@Test
fun testValidLogin() {
    KMaestro("login", "valid_user").apply {
        // ... test implementation
        takeScreenshot(TeamConventions.screenshotName("login", "success"))
    }.build()
}
```

### 2. Create Shared Test Utilities

```kotlin
// Shared utilities module
object TestUtils {
    fun KMaestro.standardAppLaunch() = apply {
        launchApp(
            appId = TestConfig.APP_ID,
            clearState = true,
            arguments = mapOf("testMode" to true)
        )
        waitForAnimationToEnd()
    }
    
    fun KMaestro.loginAsTestUser() = apply {
        performLogin(TestConfig.TEST_USER_EMAIL, TestConfig.TEST_USER_PASSWORD)
        assertVisible(text = "Dashboard")
    }
    
    fun generateTestReport(testName: String, success: Boolean, duration: Long) {
        // Generate standardized test reports
    }
}
```

### 3. Document Common Patterns

```kotlin
/**
 * Common Test Patterns Documentation
 * 
 * 1. App Launch Pattern:
 *    - Always use clearState for independent tests
 *    - Include testMode argument for consistent behavior
 *    - Wait for animations to complete
 * 
 * 2. Login Pattern:
 *    - Use test user credentials from config
 *    - Hide keyboard before submitting
 *    - Verify successful navigation to dashboard
 * 
 * 3. Navigation Pattern:
 *    - Wait for animations between navigation steps
 *    - Verify page load with assertVisible
 *    - Take screenshots at key navigation points
 */
class DocumentedPatterns {
    
    @Test
    fun exampleFollowingPatterns() {
        KMaestro("example", "documented_pattern").apply {
            // 1. Standard app launch
            TestUtils.standardAppLaunch()
            
            // 2. Standard login
            TestUtils.loginAsTestUser()
            
            // 3. Standard navigation
            navigateToFeature("Profile")
            
        }.build()
    }
}
```

## 🎯 Summary Checklist

Before committing your KMaestro tests, ensure you follow these guidelines:

### ✅ Code Quality

- [ ] Descriptive test and method names
- [ ] Consistent naming conventions
- [ ] Proper error handling
- [ ] Appropriate comments for complex logic

### ✅ Test Design

- [ ] Follow AAA pattern (Arrange, Act, Assert)
- [ ] Use Page Object pattern for complex flows
- [ ] Implement proper wait strategies
- [ ] Include meaningful assertions

### ✅ Maintainability

- [ ] Use ID selectors when possible
- [ ] Avoid hardcoded coordinates
- [ ] Create reusable flow components
- [ ] Generate dynamic test data

### ✅ Performance

- [ ] Optimize wait times
- [ ] Minimize app state resets
- [ ] Use efficient element selectors
- [ ] Parallel test execution where appropriate

### ✅ Debugging

- [ ] Include debug screenshots
- [ ] Add logging for critical steps
- [ ] Implement fallback strategies
- [ ] Document known issues

### ✅ Team Collaboration

- [ ] Follow team conventions
- [ ] Use shared utilities
- [ ] Document test patterns
- [ ] Code review compliance

---

Following these best practices will help you create robust, maintainable, and efficient mobile tests
with KMaestro. Remember that good test automation is an iterative process – continuously refine your
approaches based on team feedback and changing requirements.