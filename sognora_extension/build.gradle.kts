plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android{
    namespace = "orot.apps.sognora_compose_extension"
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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

    Libraries.apply {
        Libraries.KTX.apply {
            api (lifecycleRuntime)
            api (lifecycleViewModel)
        }

        Libraries.Compose.apply {
            api (material)
            api (activity)
            api (ui)
            api (uiTooling)
            api (navigation)
            api (constraintLayout)
        }

        Libraries.OkHttp.apply {
            implementation(okhttp)
        }
    }


}