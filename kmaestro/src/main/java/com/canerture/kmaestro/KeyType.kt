package com.canerture.kmaestro

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