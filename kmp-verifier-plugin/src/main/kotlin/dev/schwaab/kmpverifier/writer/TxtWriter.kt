package dev.schwaab.kmpverifier.writer

import dev.schwaab.kmpverifier.writer.ReportWriter.Companion.SUCCESS_MESSAGE
import java.io.File

internal class TxtWriter(private val root: File,
                         reportFile: File) : ReportWriter {
    private val writer = reportFile.writer()
    private var incompatibilities = 0

    init {
        reportFile.deleteRecursively()
    }

    override fun reportIncompatibility(problematicFile: File, lineNumber: Int, explanation: String) {
        incompatibilities++
        writer.write("${problematicFile.relativeTo(root).path} [L:$lineNumber] → $explanation\n")
    }

    override fun close() {
        if (incompatibilities == 0) {
            writer.write(SUCCESS_MESSAGE)
        }
        writer.close()
    }
}