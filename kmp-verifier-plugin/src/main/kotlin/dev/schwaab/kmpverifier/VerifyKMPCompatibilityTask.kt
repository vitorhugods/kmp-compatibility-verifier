package dev.schwaab.kmpverifier

import dev.schwaab.kmpverifier.verifier.FileScanner
import dev.schwaab.kmpverifier.writer.ReportWriter
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class VerifyKMPCompatibilityTask : SourceTask() {

    @get:Input
    abstract val reportFormat: Property<ReportFormat>

    @get:Internal
    abstract val kotlinSourceSet: Property<KotlinSourceSet>

    @get:Inject
    internal abstract val workerExecutor: WorkerExecutor

    @get:Classpath
    internal abstract val classpath: ConfigurableFileCollection

    @get:OutputDirectory
    var outputDirectory: File? = null

    @InputFiles
    @SkipWhenEmpty
    @IgnoreEmptyDirectories
    @PathSensitive(PathSensitivity.RELATIVE)
    override fun getSource(): FileTree {
        return super.getSource()
    }

    @TaskAction
    fun verifySourceSet() {
        val workQueue = workerExecutor.classLoaderIsolation {
            classpath.from(this@VerifyKMPCompatibilityTask.classpath)
        }

        workQueue.submit(VerifyKMP::class.java) {
            projectRoot.set(project.rootDir)
            outputDirectory.set(this@VerifyKMPCompatibilityTask.outputDirectory)
            reportFormat.set(this@VerifyKMPCompatibilityTask.reportFormat)
            sourceFiles.set(source.asFileTree.filter { !it.isDirectory }.files)
            sourceSetName.set(kotlinSourceSet.get().name)
        }
    }

    interface VerifyWorkParameters : WorkParameters {
        val outputDirectory: DirectoryProperty
        val projectRoot: Property<File>
        val sourceSetName: Property<String>
        val reportFormat: Property<ReportFormat>
        val sourceFiles: SetProperty<File>
    }

    abstract class VerifyKMP : WorkAction<VerifyWorkParameters> {

        private val scanners = FileScanner.all()

        private val writer = ReportWriter.get(
            parameters.projectRoot.get(),
            parameters.outputDirectory.get().asFile,
            parameters.sourceSetName.get(),
            parameters.reportFormat.get()
        )

        override fun execute() {
            parameters.sourceFiles
                .get()
                .forEach { file ->
                    scanners.forEach { scanner -> scanner.scanFile(file, writer) }
                }
            writer.close()
        }
    }

}