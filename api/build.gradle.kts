plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.xm.api"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    api("androidx.appcompat:appcompat:1.7.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.concurrent:concurrent-futures:1.1.0")

    /*android KTX*/
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.collection:collection-ktx:1.3.0")
    api("androidx.fragment:fragment-ktx:1.6.2")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    api("androidx.navigation:navigation-runtime-ktx:2.7.5")
    api("androidx.navigation:navigation-fragment-ktx:2.7.5")
    api("androidx.navigation:navigation-ui-ktx:2.7.5")
    api("androidx.palette:palette-ktx:1.0.0")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.6.2")
    api("androidx.room:room-ktx:2.6.1")
    api("androidx.sqlite:sqlite-ktx:2.4.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    api("androidx.work:work-runtime-ktx:2.9.0")
    /*android ktx -end*/

    api("com.drakeet.multitype:multitype:4.3.0")
    api("com.github.bumptech.glide:glide:4.16.0")

    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    api("com.google.code.gson:gson:2.11.0")
    api("com.squareup.retrofit2:retrofit:2.11.0")
    api("com.squareup.retrofit2:converter-gson:2.11.0")

}