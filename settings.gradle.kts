
rootProject.name = "KMP-Verifier"

val pluginModule = "kmp-verifier-plugin"

val excludedModules = setOf("buildSrc", pluginModule)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    }
}

rootDir
    .walk()
    .maxDepth(1)
    .filter {
        it.name !in excludedModules && it.isDirectory
                && file("${it.absolutePath}/build.gradle.kts").exists()
    }.forEach {
        include(":${it.name}")
    }

includeBuild(pluginModule)