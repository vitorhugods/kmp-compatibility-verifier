package dev.schwaab.kmpverifier

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File

class KMPVerifierPlugin : Plugin<Project> {

    private lateinit var extension: KMPVerifierExtension

    override fun apply(project: Project) {
        println("Applying KMPVerifierPlugin to $project")

        extension = project.extensions.create("kmpVerifier", KMPVerifierExtension::class.java)

        project.afterEvaluate {
            setupTasks()
        }
    }

    private fun Project.setupTasks() {
        val hasAnyAndroidPlugin = AndroidPlugins.values().any {
            pluginManager.findPlugin(it.id) != null
        }
        require(hasAnyAndroidPlugin) {
            "KMPVerifierPlugin applied, but no Android plugin found"
        }

        tasks.register(UMBRELLA_TASK_NAME) {
            group = GROUP
            description = "Umbrella KMP Verification task that checks all source sets"
        }

        val kotlinExtension = project.extensions.findByType<KotlinProjectExtension>() ?: throw GradleException(
            "KMPVerifier requires the Kotlin plugin to be applied"
        )

        val defaultOutputDir = File(buildDir, "kmpVerifier")

        kotlinExtension.sourceSets.forEach { sourceSet ->
            objects.newInstance(KMPVerifierSourceSet::class.java, project, sourceSet)
                .registerVerificationTask(
                    extension.outputDirectory.getOrElse(defaultOutputDir),
                    extension.reportFormat.getOrElse(ReportFormat.HTML)
                )
        }
    }

    internal companion object {
        const val GROUP = "kmp-compatibility"
        const val UMBRELLA_TASK_NAME = "verifyKMPCompatibility"
    }
}