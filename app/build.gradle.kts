import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.google.services)
    id("kotlin-parcelize")
    id("kotlin-kapt")
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
        buildConfigField(
            "String",
            "KAKAO_API_KEY",
            "\"${localProperties.getProperty("KAKAO_API_KEY", "")}\""
        )

        buildConfigField(
            "String",
            "KAKAO_REST_API_KEY",
            "\"${localProperties.getProperty("KAKAO_REST_API_KEY", "")}\""
        )

        manifestPlaceholders["KAKAO_API_KEY"] =
            localProperties.getProperty("KAKAO_API_KEY", "")
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
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.ru.ldralighieri.corbind)
    implementation(libs.google.services)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.places)
    testImplementation(libs.junit)
    implementation(libs.play.services.location)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.ru.ldralighieri.corbind.material)
    implementation(project(":data"))
    implementation(project(":feature"))
    implementation(libs.corbind.appcompat)
    implementation(libs.corbind.activity)

    // preference
    implementation(libs.datastore.preference)

    // retrofit + moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    implementation(libs.retrofit)

    // kakao
    implementation(libs.kakao.maps)
    implementation(libs.kakao.sdk)

    // shared preference
    implementation("androidx.preference:preference-ktx:1.2.1")

    // tedPermission
    implementation(libs.tedpermission.normal)

    // Moshi 관련
    kapt(libs.moshi.kotlin.codegen)

    // cameraX 관련 dependencies
    implementation("androidx.camera:camera-core:1.4.1")
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")
    implementation("androidx.camera:camera-extensions:1.4.1")
    implementation("com.google.guava:guava:33.3.1-android")

    // Coil 관련 dependencies
    implementation("io.coil-kt.coil3:coil:3.0.4")
    implementation("io.coil-kt.coil3:coil-gif:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // MLKit barcode
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    // xml circleView
    implementation("de.hdodenhof:circleimageview:3.1.0")
}