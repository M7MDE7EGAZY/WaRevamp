
plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "its.madruga.warevamp"
    compileSdk = 34

    defaultConfig {
        applicationId = "its.madruga.warevamp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "okhttp3/**"
            excludes += "kotlin/**"
            excludes += "org/**"
            excludes += "**.properties"
            excludes += "**.bin"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.preference)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.material)

    implementation(libs.dexkit)
    implementation(libs.appcompat)
    compileOnly(libs.libxposed)

    implementation(libs.rikkax.appcompat)
    implementation(libs.rikkax.material)
    implementation(libs.rikkax.material.preference)
}


configurations.all {
    exclude("androidx.appcompat", "appcompat")
}