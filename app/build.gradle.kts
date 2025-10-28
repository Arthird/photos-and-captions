plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.ybd"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.ybd"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Для ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Для ViewPager2 (свайпы)
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Для SharedPreferences (помогает с KTX)
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Для фоновых задач (WorkManager - таймер)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Примечание: androidx.core.ktx уже есть в libs, но если там старая версия,
    // эта строка гарантирует использование 1.13.1
    implementation("androidx.core:core-ktx:1.13.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}