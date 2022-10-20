import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdk = 32
    const val minSdk = 26
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "0.0.1"
    val javaVersion = JavaVersion.VERSION_11
    val jvmTarget = "11"
}