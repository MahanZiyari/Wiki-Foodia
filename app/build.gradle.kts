plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //Ksp
    id("com.google.devtools.ksp")
    //Hilt
    id("com.google.dagger.hilt.android")
    // Safe Args
    id("androidx.navigation.safeargs")
    // Kotlin Parcelize
    id("kotlin-parcelize")
}


android {
    namespace = "ir.mahan.wikifoodia"
    compileSdk = 35

    defaultConfig {
        applicationId = "ir.mahan.wikifoodia"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Hilt-Dagger
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    // ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.8.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.6")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    //OkHTTP client
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //Image Loading
    implementation ("io.coil-kt:coil:2.6.0")
    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")
    // Calligraphy
    implementation ("io.github.inflationx:calligraphy3:3.1.1")
    implementation ("io.github.inflationx:viewpump:2.0.3")
    // Lottie
    implementation ("com.airbnb.android:lottie:6.1.0")
    // Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.github.omtodkar:ShimmerRecyclerView:v0.4.1")
    // Other
    implementation ("com.github.MrNouri:DynamicSizes:1.0")
    implementation ("kr.co.prnd:readmore-textview:1.0.0")


}