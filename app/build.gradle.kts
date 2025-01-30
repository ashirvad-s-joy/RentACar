plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.fire.base)
}

android {
    namespace = "com.inte.assignment"
    compileSdk = 35
    useLibrary("android.car")

    defaultConfig {
        applicationId = "com.inte.assignment"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        aidl = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation (libs.androidx.app)
    implementation (libs.androidx.app.automotive )// Replace with the latest version

    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.firebase:firebase-database:20.0.5")  // For Firebase Realtime Database
    // Or for Firestore
    implementation("com.google.firebase:firebase-firestore:24.2.0")
}