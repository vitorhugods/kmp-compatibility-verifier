package dev.schwaab.kmpverifier

import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File
import javax.inject.Inject

abstract class KMPVerifierSourceSet @Inject constructor(
    private val project: Project,
    private val sourceSet: KotlinSourceSet
) {

    private val configuration = project.configurations.create("${sourceSet.name}KMPVerifierClasspath").apply {
        isCanBeConsumed = false
        isVisible = false
    }

    fun registerVerificationTask(
        outputDirectory: File,
        reportFormat: ReportFormat
    ) {
        val taskName = KMPVerifierPlugin.UMBRELLA_TASK_NAME + sourceSet.name.capitalized()

        val sourceSetTask = project.tasks.register(taskName, VerifyKMPCompatibilityTask::class.java) {
            this.kotlinSourceSet.set(sourceSet)
            this.group = KMPVerifierPlugin.GROUP
            this.outputDirectory = outputDirectory
            this.reportFormat.set(reportFormat)
            this.source = sourceSet.kotlin.asFileTree
            this.classpath.setFrom(configuration.fileCollection { true })
        }
        project.umbrellaVerificationTask.dependsOn(sourceSetTask)
    }
}