package dev.schwaab.kmpverifier

enum class AndroidPlugins(val id: String) {
    APPLICATION("com.android.application"),
    LIBRARY("com.android.library"),
    DYNAMIC_FEATURE("com.android.dynamic-feature"),
    FEATURE("com.android.feature"),
    INSTANT_APP("com.android.instantapp"),
}