plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.mmb.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}

kapt {
    correctErrorTypes = true
}