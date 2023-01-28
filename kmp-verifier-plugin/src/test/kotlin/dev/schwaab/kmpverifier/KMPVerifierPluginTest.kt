package dev.schwaab.kmpverifier

import dev.schwaab.kmpverifier.util.commonTestProject
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import kotlin.test.Test

class KMPVerifierPluginTest {

    @Test
    fun `given project without android, then throw error`() {
        val result = GradleRunner.create()
            .commonTestProject("android-missing")
            .withPluginClasspath()
            .withArguments("clean", "--stacktrace")
            .buildAndFail()

        result.output.shouldContain(
            "KMPVerifierPlugin applied, but no Android plugin found"
        )
    }

    @Test
    fun `given project with android, but missing kotlin, then throw error`() {
        val result = GradleRunner.create()
            .commonTestProject("android-but-kotlin-missing")
            .withPluginClasspath()
            .withArguments("clean", "--stacktrace")
            .buildAndFail()

        result.output.shouldContain(
            "KMPVerifier requires the Kotlin plugin to be applied"
        )
    }

    @Test
    fun `given android and kotlin plugins are applied, then should configure successfully`() {
        GradleRunner.create()
            .commonTestProject("fully-compatible-txt")
            .withPluginClasspath()
            .withArguments("clean", "--stacktrace")
            .build()
    }

    @Test
    fun `given successful configuration, then the umbrella task should be created and run successfully`() {
        GradleRunner.create()
            .commonTestProject("fully-compatible-txt")
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME, "--stacktrace")
            .build()
    }

    @Test
    fun `given successful configuration, when running umbrella task, then specific source sets verifications should run`() {
        val result = GradleRunner.create()
            .commonTestProject("fully-compatible-txt")
            .withPluginClasspath()
            .withArguments("clean", KMPVerifierPlugin.UMBRELLA_TASK_NAME + "main", "--stacktrace")
            .build()
        result.output.shouldContain(
            KMPVerifierPlugin.UMBRELLA_TASK_NAME + "Main"
        )
    }
}