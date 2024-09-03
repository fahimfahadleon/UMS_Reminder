plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/home/fahim/AndroidStudioProjects/UMSReminder/ums.jks")
            storePassword = "64742812"
            keyAlias = "key0"
            keyPassword = "64742812"
        }
        create("release") {
            storeFile = file("/home/fahim/AndroidStudioProjects/UMSReminder/ums.jks")
            storePassword = "64742812"
            keyAlias = "key0"
            keyPassword = "64742812"
        }
    }
    namespace = "com.unicornitsolutions.umsreminder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unicornitsolutions.umsreminder"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    implementation(libs.permissionx)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)




}