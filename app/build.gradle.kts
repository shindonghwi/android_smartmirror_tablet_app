plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "orot.apps.smartcounselor"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = AppConfig.javaVersion
        targetCompatibility = AppConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.composeCompiler
    }
}

dependencies {

    implementation(project(":test:SognoraWebSocket"))
    Libraries.apply {

        Libraries.KTX.apply {
            implementation(core)
        }

        Libraries.Compose.apply {
            implementation(material)
            implementation(activity)
            implementation(ui)
            implementation(uiTooling)
            implementation(navigation)
            implementation(constraintLayout)
        }

        Libraries.Hilt.apply {
            implementation(NavigationCompose)
            implementation(dagger)
        }

        Libraries.AndroidX.apply {
            implementation(constraintLayout)
        }

        Libraries.Retrofit.apply {
            implementation(retrofit)
            implementation(retrofit_gson)
        }
    }


    Kapts.apply {
        Kapts.Hilt.apply {
            kapt(hiltCompiler)
        }
    }
}
