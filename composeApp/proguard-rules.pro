# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}
# Keep Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
# Keep Lottie
-keep class com.airbnb.lottie.** { *; }
# Keep Firebase
-keep class com.google.firebase.** { *; }
# Keep Decompose
-keep class com.arkivanov.decompose.** { *; }
# Keep your DTO models
-keep class com.example.travel.model.dto.** { *; }
-keep class com.example.travel.network.** { *; }
-keep class org.example.project.travel.frontend.model.** { *; }

# Suppress slf4j binder warnings (missing optional dependency in release builds)
-dontwarn org.slf4j.impl.StaticLoggerBinder

