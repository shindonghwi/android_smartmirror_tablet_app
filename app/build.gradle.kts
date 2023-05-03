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
        getByName("debug"){
            isDebuggable = true
            isMinifyEnabled = false
        }
        getByName("release") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += listOf("mode")
    productFlavors {          // 2
        create("full") {
            dimension = "mode"
            versionNameSuffix = "-full"
            applicationIdSuffix = ".full"
            buildConfigField("String", "SHOW_MODE", "\"FULL\"")
            manifestPlaceholders["appLabel"] = "SmartCounselor"
        }
        create("onlyWatch") {
            versionNameSuffix = "-watch"
            dimension = "mode"
            applicationIdSuffix = ".watch"
            buildConfigField("String", "SHOW_MODE", "\"WATCH\"")
            manifestPlaceholders["appLabel"] = "SmartCounselor(시계)"
        }
        create("onlyRecommendation") {
            versionNameSuffix = "-watch"
            dimension = "mode"
            applicationIdSuffix = ".recommendation"
            buildConfigField("String", "SHOW_MODE", "\"RECOMMENDATION\"")
            manifestPlaceholders["appLabel"] = "SmartCounselor(권고)"
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

    implementation(project(":core:SognoraWebSocket"))
    implementation(project(":core:SognoraAudio"))
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    Libraries.apply {

        Libraries.KTX.apply {
            implementation(core)
        }

        Libraries.Kotlin.apply {
            implementation(Reflect)
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
