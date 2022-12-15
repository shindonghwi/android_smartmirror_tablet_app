buildscript {
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }
    dependencies {
        classpath(Libraries.Gradle.gradle)
        classpath(Libraries.Kotlin.gradlePlugin)
        classpath(Libraries.Hilt.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

allprojects {
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}