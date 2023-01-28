package dev.schwaab.kmpverifier

import dev.schwaab.kmpverifier.util.commonTestProject
import dev.schwaab.kmpverifier.writer.ReportWriter.Companion.SUCCESS_MESSAGE
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test

class IntegrationTest {

    @Test
    fun `given java io usage on main source set, when running verification task, should write to txt`() {
        val fixtureDirName = "java-io-usage-txt"
        GradleRunner.create()
            .commonTestProject(fixtureDirName)
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME + "main", "--stacktrace")
            .build()

        val outputDir = File("src/test/$fixtureDirName/build/kmpVerifier")
        val mainReport = File(outputDir, "main.txt")

        mainReport.readText().shouldContain(
            "NotCompatible.kt"
        ).shouldContain(
            "java.io"
        )
    }

    @Test
    fun `given java io usage on main source set, when running verification task, should write to html`() {
        val fixtureDirName = "java-io-usage-html"
        GradleRunner.create()
            .commonTestProject(fixtureDirName)
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME + "main", "--stacktrace")
            .build()

        val outputDir = File("src/test/$fixtureDirName/build/kmpVerifier")
        val mainReport = File(outputDir, "main.html")

        mainReport.readText().shouldContain(
            "NotCompatible.kt"
        ).shouldContain(
            "java.io"
        )
    }

    @Test
    fun `given source set is fully compatible, when running verification task, should write success message to txt`() {
        val fixtureDirName = "fully-compatible-txt"
        GradleRunner.create()
            .commonTestProject(fixtureDirName)
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME + "main", "--stacktrace")
            .build()

        val outputDir = File("src/test/$fixtureDirName/build/kmpVerifier")
        val mainReport = File(outputDir, "main.txt")

        mainReport.readText().shouldContain(
            SUCCESS_MESSAGE
        )
    }

    @Test
    fun `given source set is fully compatible, when running verification task, should write success message to html`() {
        val fixtureDirName = "fully-compatible-html"
        GradleRunner.create()
            .commonTestProject(fixtureDirName)
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME + "main", "--stacktrace")
            .build()

        val outputDir = File("src/test/$fixtureDirName/build/kmpVerifier")
        val mainReport = File(outputDir, "main.html")

        mainReport.readText().shouldContain(
            SUCCESS_MESSAGE
        )
    }

}