package dev.schwaab.kmpverifier.writer

import dev.schwaab.kmpverifier.ReportFormat
import java.io.File

interface ReportWriter {

    fun reportIncompatibility(
        problematicFile: File,
        lineNumber: Int,
        explanation: String
    )

    fun close()

    companion object {
        fun get(
            projectRoot: File,
            directory: File,
            fileName: String,
            format: ReportFormat
        ): ReportWriter = when (format) {
            ReportFormat.TXT -> TxtWriter(projectRoot, File(directory, "$fileName.txt"))
            ReportFormat.HTML -> HtmlWriter(projectRoot, File(directory, "$fileName.html"))
        }

        const val SUCCESS_MESSAGE = "Source set is fully compatible"
    }
}

internal data class IncompatibilityRecord(
    val problematicFile: File,
    val lineNumber: Int,
    val explanation: String
)