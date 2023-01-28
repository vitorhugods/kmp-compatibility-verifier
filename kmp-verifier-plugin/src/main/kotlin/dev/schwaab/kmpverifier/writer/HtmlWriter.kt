package dev.schwaab.kmpverifier.writer

import dev.schwaab.kmpverifier.writer.ReportWriter.Companion.SUCCESS_MESSAGE
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr
import kotlinx.html.unsafe
import java.io.File
import java.io.OutputStreamWriter

class HtmlWriter(
    private val root: File,
    private val reportFile: File
) : ReportWriter {
    private val incompatibilities = mutableListOf<IncompatibilityRecord>()

    init {
        reportFile.deleteRecursively()
    }

    override fun reportIncompatibility(problematicFile: File, lineNumber: Int, explanation: String) {
        incompatibilities.add(
            IncompatibilityRecord(problematicFile, lineNumber, explanation)
        )
    }

    override fun close() {
        val writer = reportFile.writer()
        if (incompatibilities.isEmpty()) {
            writer.appendHTML().html {
                body {
                    div { text(SUCCESS_MESSAGE) }
                }
            }
        } else {
            writeTable(writer)
            writeCss(writer)
        }
        writer.close()
    }

    private fun writeTable(writer: OutputStreamWriter) {
        writer.appendHTML().html {
            body("container") {
                table("table") {
                    tr("table-header") {
                        th { text("File") }
                        th { text("Line") }
                        th { text("Message") }
                    }
                    incompatibilities.forEach { record ->
                        tr("table-record") {
                            td { text(record.problematicFile.relativeTo(root).path) }
                            td { text(record.lineNumber) }
                            td { text(record.explanation) }
                        }
                    }
                }
            }
        }
    }

    private fun writeCss(writer: OutputStreamWriter) {
        writer.appendHTML().style {
            unsafe {
                raw(
                    """
                        body {
                          font-family: 'lato', sans-serif;
                        }
                        .container {
                          max-width: 1000px;
                          margin-left: auto;
                          margin-right: auto;
                          padding-left: 10px;
                          padding-right: 10px;
                        }
                        .table { }
                        tr {
                          border-radius: 3px;
                          padding: 25px 30px;
                          margin-bottom: 25px;
                        }
                        .table-header {
                          background-color: #318CE7;
                          font-size: 14px;
                          color: white;
                          text-transform: uppercase;
                          letter-spacing: 0.03em;
                        }
                        .table-record {
                          background-color: #ffffff;
                          box-shadow: 0px 0px 9px 0px rgba(0,0,0,0.1);
                        }
                    """.trimIndent()
                )
            }
        }
    }
}