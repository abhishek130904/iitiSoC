import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
        kotlin("plugin.serialization") version "1.9.0" // match your Kotlin version
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("io.ktor:ktor-client-logging:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                //navigation
//                implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-beta02")

                implementation("moe.tlaster:precompose:1.5.7")
                implementation("moe.tlaster:precompose-viewmodel:1.5.7")

                implementation("io.ktor:ktor-client-cio:2.3.11")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("co.touchlab:kermit:2.0.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

                implementation("io.insert-koin:koin-core:3.5.3")

                implementation("com.arkivanov.decompose:decompose:3.1.0")
                implementation("com.arkivanov.decompose:extensions-compose:3.1.0")

                implementation("moe.tlaster:precompose:1.5.6")
                implementation("moe.tlaster:precompose-viewmodel:1.5.6")

                implementation("org.jetbrains.skiko:skiko:0.8.12")
                implementation("media.kamel:kamel-image:0.9.0")


            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation("androidx.core:core-ktx:1.10.1")
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
                implementation("io.ktor:ktor-client-android:2.3.11")
                implementation ("androidx.navigation:navigation-compose:2.7.7")// Latest stable as of June 2025

                // (Optional but recommended)
                implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
                implementation ("androidx.activity:activity-compose:1.8.2")

                // Koin for Android (only if you need it)
                implementation("io.insert-koin:koin-android:3.5.3")
                implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
                implementation("com.google.android.gms:play-services-auth:21.2.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
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
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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


