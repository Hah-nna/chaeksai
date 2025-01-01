import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
    id("kotlin-parcelize")
}

// local.properties 안의 내용 읽기
val localProperties: Properties by lazy {
    Properties().apply {
        rootProject.file("local.properties")
            .takeIf { it.exists() }?.inputStream()?.use { load(it) }
    }
}

android {
    namespace = "com.jeong.sesac.sai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jeong.sesac.sai"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       // kakao map 키 가져옴. manifest 파일에서도 사용할 수 있게 함
        buildConfigField("String", "KAKAO_MAP_KEY", "\"${localProperties.getProperty("KAKAO_MAP_KEY", "")}\"")
        manifestPlaceholders["KAKAO_MAP_KEY"] = localProperties.getProperty("KAKAO_MAP_MANAFEST_APP_KEY", "")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    viewBinding {
        enable = true
    }

    /** buildConfig.java 파일 생성을 ㅇㅋㅇㅋ함
     * buildConfig.java 파일 속 BuildConfig 클래스 안에
     * defaultConfig와 30번째 라인(buildConfig로 설정한 내용)이 저장됨
     * 그리고 이 값들을 다른 곳에서도 val apiKey = BuildConfig.KAKAO_MAP_KEY <- 이런식으로 사용가능함
     */
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.ru.ldralighieri.corbind)
    implementation(project(":domainModule"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.ru.ldralighieri.corbind.material)
    implementation("com.kakao.maps.open:android:2.9.5")
    implementation("com.kakao.sdk:v2-all:2.20.6")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.android)
    implementation("ru.ldralighieri.corbind:corbind-appcompat:1.11.0")
    implementation("ru.ldralighieri.corbind:corbind-activity:1.11.0")
    implementation("ru.ldralighieri.corbind:corbind-lifecycle:1.11.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
}