plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.th03final"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.th03final"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
