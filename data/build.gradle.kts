plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.com.google.android.ksp)
    id("dagger.hilt.android.plugin")
    // id("com.google.devtoold.ksp")
    //id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.me.data"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // Dagger
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // RETROFIT
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.timber)
}
