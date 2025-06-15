rootProject.name = "TravelFrontEnd"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}



dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/releases/") }
    }
}


include(":composeApp")