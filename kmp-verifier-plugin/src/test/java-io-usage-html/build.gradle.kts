plugins {
    id("com.android.library")
    kotlin("android")
    id("dev.schwaab.kmpverifier")
}

android {
    setCompileSdkVersion(33)
    namespace = "dev.schwaab.test"
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
    reportFormat.set(dev.schwaab.kmpverifier.ReportFormat.HTML)
}
