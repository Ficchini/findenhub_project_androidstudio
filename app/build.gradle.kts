plugins {
    id("com.google.gms.google-services")
    id("com.android.application")
}

android {
    namespace = "com.findenhub_project.app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.findenhub_project.app"
        minSdk = 24
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
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    // Firebase BOM — gerencia versões automaticamente
    implementation(platform("com.google.firebase:firebase-bom:34.14.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.0.0")

    // Material Design
    implementation ("com.google.android.material:material:1.11.0")

    // ConstraintLayout
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle + ViewModel + LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // Activity + Fragment KTX (necessário mesmo em Java)
    implementation ("androidx.activity:activity:1.8.2")
    implementation ("androidx.fragment:fragment:1.6.2")

    // Glide — carregamento de imagens
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}