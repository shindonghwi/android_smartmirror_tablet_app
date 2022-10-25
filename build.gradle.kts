buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libraries.Gradle.gradle)
        classpath(Libraries.Kotlin.gradlePlugin)
        classpath(Libraries.Hilt.gradlePlugin)
        classpath(Libraries.Kotlin.serialization)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}