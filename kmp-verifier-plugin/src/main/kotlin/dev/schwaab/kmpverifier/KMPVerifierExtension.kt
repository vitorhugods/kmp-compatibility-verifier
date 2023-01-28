package dev.schwaab.kmpverifier

import org.gradle.api.provider.Property
import java.io.File

interface KMPVerifierExtension {
    val outputDirectory: Property<File>
    val reportFormat: Property<ReportFormat>
}