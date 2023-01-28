package dev.schwaab.kmpverifier.util

import org.gradle.testkit.runner.GradleRunner
import java.io.File

internal fun GradleRunner.commonTestProject(testDirectoryName: String): GradleRunner {
    val testDirectory = File("src/test/$testDirectoryName")
    File(testDirectory, "settings.gradle").writeText(
        """
            apply from: "../settings.gradle.kts"
        """.trimIndent()
    )
    return withProjectDir(testDirectory)
}
