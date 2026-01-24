plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)


    id ("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.hilt.android)

    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "org.fmm.teleworking"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.fmm.teleworking"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures { compose = true}
    composeOptions {
        // Con Kotlin 2.x + plugin moderno normalmente NO se necesita
    //    kotlinCompilerExtensionVersion = "1.5.5"
    }
    packagingOptions { resources.excludes.add("META-INF/DEPENDENCIES")}
}

dependencies {

    implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.appcompat)
    implementation (libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Dagger Hilt
    //DaggerHilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //kapt(libs.androidx.hilt.compiler.v130)

    // JSON
    implementation(libs.kotlinx.serialization.json)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.datetime)

    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)

}