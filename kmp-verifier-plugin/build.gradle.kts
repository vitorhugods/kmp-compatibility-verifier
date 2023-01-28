plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `kotlin-dsl-precompiled-script-plugins`
}

gradlePlugin {
    plugins.register("kmp-verifier") {
        id = libs.plugins.kmp.verifier.get().pluginId
        implementationClass = "dev.schwaab.kmpverifier.KMPVerifierPlugin"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.plugin)
    implementation(libs.kotlinx.html)

    testImplementation(gradleTestKit())
    testImplementation(libs.kotest.assertions)
    testImplementation(kotlin("test"))
}