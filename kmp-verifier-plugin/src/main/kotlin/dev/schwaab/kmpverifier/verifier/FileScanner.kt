package dev.schwaab.kmpverifier.verifier

import dev.schwaab.kmpverifier.writer.ReportWriter
import java.io.File

interface FileScanner {
    fun scanFile(file: File, writer: ReportWriter)

    companion object {
        fun all(): List<FileScanner> = listOf(
            SimpleTextFinderScanner()
        )
    }
}
