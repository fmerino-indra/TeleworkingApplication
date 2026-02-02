plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)


// Funcionalmente es lo mismo
//    id ("org.jetbrains.kotlin.kapt")
//    id ("kotlin-kapt")
//    kotlin ("kapt")
// Nos pasamos a ksp
    alias(libs.plugins.devtools.ksp)

}

android {
    namespace = "org.fmm.teleworking"
    //noinspection GradleDependency
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "org.fmm.teleworking"
        minSdk = 29
        //noinspection OldTargetApi
        targetSdk = 35
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
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL",  "\"http://192.168.1.150:8080/api/\"")
//            resValue("string","fmmName", "[DEBUG] CommunityMgmtAppDebug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures { 
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
//    composeOptions {
        // Con Kotlin 2.x + plugin moderno normalmente NO se necesita
    //    kotlinCompilerExtensionVersion = "1.5.5"
//    }
//    packagingOptions { resources.excludes.add("META-INF/DEPENDENCIES")}
}

dependencies {
    // --- CORE & ANDROIDX ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation (libs.androidx.activity.compose)
    implementation(libs.androidx.runtime.livedata)

    // --- JETPACK COMPOSE
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // --- Material Design (UI) --
    implementation(libs.androidx.material3)
    implementation(libs.google.material)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // --- ROOM (Base de datos) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Procesador de anotaciones (generación de código)


    // --- Networking (Retrofit + OkHttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // --- SERIALIZACIÓN (JSON) ---
    // Opción A: Kotlinx Serialization (Recomendado para Compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.datetime)

    // Opción B: Moshi (La tienes incluida, asegúrate de no mezclar ambas en la misma API)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)


    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
//kapt {
//    correctErrorTypes = true
//}
// Fuera del bloque android: para el error de las anotaciones duplicadas
configurations.all {
    exclude(group = "com.intellij", module = "annotations")
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("2.1.0")
        }
        if (requested.group == "org.jetbrains.kotlinx" && requested.name.contains("serialization")) {
            useVersion("1.7.3")
        }
    }
}