// Project-level build.gradle
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    repositories {
        google()  // Google repository for Firebase
        mavenCentral()
    }
    dependencies {
        // Add the Firebase services plugin
        classpath("com.google.gms:google-services:4.3.15" ) // Adjust based on the latest version
    }
}




