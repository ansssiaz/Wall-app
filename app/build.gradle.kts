import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.ansssiaz.wallapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ansssiaz.wallapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secretProps: Properties = rootDir.resolve("secrets.properties")
            .bufferedReader()
            .use {
                Properties().apply {
                    load(it)
                }
            }
        buildConfigField("String", "API_KEY", secretProps.getProperty("API_KEY"))
        buildConfigField("String", "AUTHORIZATION", secretProps.getProperty("AUTHORIZATION"))
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.threetenabp)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.adapter.rxjava3)
    implementation(libs.rxkotlin)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.arrow.core)
    implementation(libs.koleton)
    implementation(libs.glide)

    implementation(libs.hilt.android)
    ksp(libs.google.dagger.compiler)
    ksp(libs.dagger.hilt.compiler)
}