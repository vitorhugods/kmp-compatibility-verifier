package dev.schwaab.kmpverifier.writer

import dev.schwaab.kmpverifier.writer.ReportWriter.Companion.SUCCESS_MESSAGE
import io.kotest.matchers.string.shouldContain
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

class TxtWriterTest {

    private lateinit var workDirectory: TemporaryFolder
    private lateinit var reportFile: File
    private lateinit var fileBeingVerified: File
    private lateinit var writer: TxtWriter

    @BeforeTest
    fun setup() {
        workDirectory = TemporaryFolder().also { it.create() }
        reportFile = workDirectory.newFile(REPORT_FILE_NAME)
        fileBeingVerified = workDirectory.newFile(FILE_BEING_VERIFIED)
        writer = TxtWriter(workDirectory.root, reportFile)
    }

    @Test
    fun `given a report, when closing, then file should contain the reported line`() {
        writer.reportIncompatibility(fileBeingVerified, 3, "Oops")
        writer.close()

        reportFile.readText().shouldContain("${fileBeingVerified.name} [L:3] → Oops\n")
    }

    @Test
    fun `given multiple reports, when closing, then file should contain all reports`() {
        val anotherFile = workDirectory.newFile("AnotherFile.kt")
        val incompatibilities = listOf(
            IncompatibilityRecord(fileBeingVerified, 3, "Oops"),
            IncompatibilityRecord(fileBeingVerified, 42, "🤔"),
            IncompatibilityRecord(anotherFile, 987, "👍🏻")
        )
        incompatibilities.forEach {
            writer.reportIncompatibility(it.problematicFile, it.lineNumber, it.explanation)
        }

        writer.close()

        reportFile.readText().let { reportContent ->
            incompatibilities.forEach {
                reportContent.shouldContain(
                    "${it.problematicFile.name} [L:${it.lineNumber}] → ${it.explanation}\n"
                )
            }
        }
    }

    @Test
    fun `given no reports, when closing, then file should contain success message`() {
        writer.close()

        reportFile.readText().shouldContain(
            SUCCESS_MESSAGE
        )
    }

    private companion object {
        const val REPORT_FILE_NAME = "reportFile.txt"
        const val FILE_BEING_VERIFIED = "fileBeingVerified.kt"
    }
}