package dev.schwaab.kmpverifier

import org.gradle.api.Project
import org.gradle.api.Task

internal val Project.umbrellaVerificationTask: Task
    get() = tasks.getByName(KMPVerifierPlugin.UMBRELLA_TASK_NAME)