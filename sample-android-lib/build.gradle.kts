import dev.schwaab.kmpverifier.ReportFormat

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kmp.verifier.get().pluginId)
}

android {
    setCompileSdkVersion(33)
    defaultConfig {
        targetSdk = 33
        minSdk = 26
    }
    buildTypes {
        debug {}
        release {}
    }
}

kmpVerifier {
    reportFormat.set(ReportFormat.HTML)
    outputDirectory.set(file("reportDir"))
}