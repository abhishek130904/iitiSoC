import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("com.google.gms.google-services")
}


kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation("org.jetbrains.compose.material3:material3:1.5.11")
                implementation("org.jetbrains.compose.material:material-icons-extended:1.5.11")

                // Coroutines — single version
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

                // Ktor — all 2.3.12
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("io.ktor:ktor-client-logging:2.3.12")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                // Date/time — single version
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

                // Navigation — PreCompose (single version)
                implementation("moe.tlaster:precompose:1.5.7")
                implementation("moe.tlaster:precompose-viewmodel:1.5.7")

                // Logging
                implementation("co.touchlab:kermit:2.0.3")

                // DI
                implementation("io.insert-koin:koin-core:3.5.3")

                // Decompose
                implementation("com.arkivanov.decompose:decompose:3.1.0")
                implementation("com.arkivanov.decompose:extensions-compose:3.1.0")

                // Skiko & image loading
                implementation("org.jetbrains.skiko:skiko:0.8.12")
                implementation("media.kamel:kamel-image:0.9.0")

            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation("androidx.core:core-ktx:1.10.1")
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
                implementation("androidx.navigation:navigation-compose:2.7.7")

                // (Optional but recommended)
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
                implementation("androidx.activity:activity-compose:1.8.2")

                // Koin for Android (only if you need it)
                implementation("io.insert-koin:koin-android:3.5.3")
                implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
                implementation("com.google.android.gms:play-services-auth:21.2.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
                implementation("com.russhwolf:multiplatform-settings-android:1.1.1")
            }
        }

//        ios {
//            binaries {
//                framework()
//            }
//        }
//
//        val iosMain by getting {
//            dependencies {
//                implementation("io.ktor:ktor-client-darwin:2.3.12")
//            }
//        }
//        val iosTest by getting{
//
//        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "org.example.project.travel"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project.travel"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Read secrets from local.properties (never committed to VCS)
        val localProps = rootProject.file("local.properties")
        val properties = Properties()
        if (localProps.exists()) {
            properties.load(localProps.inputStream())
        }

        buildConfigField(
            "String",
            "UNSPLASH_KEY",
            "\"${properties.getProperty("unsplash.api.key", "")}\""
        )
        buildConfigField(
            "String",
            "RECOMMENDATION_BASE_URL",
            "\"${properties.getProperty("recommendation.base.url", "")}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(keystorePropertiesFile.inputStream())
            }
            // Use safe casts/checks so Gradle sync doesn't crash if placeholders are not updated yet
            val storeFilePath = keystoreProperties["storeFile"] as? String
            if (!storeFilePath.isNullOrEmpty()) {
                storeFile = file(storeFilePath)
            }
            storePassword = keystoreProperties["storePassword"] as? String
            keyAlias = keystoreProperties["keyAlias"] as? String
            keyPassword = keystoreProperties["keyPassword"] as? String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true          // Enable ProGuard/R8
            isShrinkResources = true        // Remove unused resources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.ui.android)
    implementation(libs.places)
    implementation(libs.firebase.dataconnect)
    implementation(libs.androidx.compose.material.core)
    implementation(libs.androidx.core.i18n)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.compose.material)
//    implementation(project(":composeApp"))
    debugImplementation(compose.uiTooling)

    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    implementation("com.airbnb.android:lottie-compose:6.3.0")
    implementation("androidx.compose.foundation:foundation:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}


