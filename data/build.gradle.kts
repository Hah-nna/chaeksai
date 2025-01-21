
plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace ="com.jeong.sesac.data"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }
}



dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation(libs.firebase.common.ktx)

    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    implementation(libs.retrofit)
    implementation(libs.datastore.preference)
    implementation(project(":feature"))
}
