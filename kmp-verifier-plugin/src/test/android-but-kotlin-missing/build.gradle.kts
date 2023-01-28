plugins {
    id("com.android.library")
    id("dev.schwaab.kmpverifier")
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

