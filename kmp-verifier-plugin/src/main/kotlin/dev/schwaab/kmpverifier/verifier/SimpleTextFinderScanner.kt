package dev.schwaab.kmpverifier.verifier

import dev.schwaab.kmpverifier.writer.ReportWriter
import java.io.File

class SimpleTextFinderScanner : FileScanner {

    override fun scanFile(file: File, writer: ReportWriter) {
        var currentLine = 0
        file.reader().forEachLine { line ->
            currentLine += 1
            forbiddenSnippets.forEach { snippet ->
                if (line.contains(snippet)) {
                    writer.reportIncompatibility(file, currentLine, "'$snippet' is being used")
                }
            }
        }
    }

    private companion object {
        val forbiddenSnippets = listOf(
            "java.util",
            "java.io",
            "java.time",
            "io.reactivex.rxjava3",
            "okhttp3.OkHttpClient",
            "retrofit2.Retrofit",
        )
    }
}