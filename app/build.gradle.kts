import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    jacoco
}

android {
    namespace = "com.thanhitutc.moviedbcompose"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.thanhitutc.moviedbcompose"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        setProperty(
            "archivesBaseName",
            "MovieDBCompose_${SimpleDateFormat("yyyyMMdd-HHmm").format(Date())}_v${versionName}(${versionCode})"
        )
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    val serverDimension = "server"
    flavorDimensions.addAll(listOf(serverDimension))
    productFlavors {
        create("dev") {
            dimension = serverDimension
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "MovieDb Dev")
            buildConfigField("boolean", "MOCK_DATA", "true")
            val keyFile = rootProject.file("signing/debug.properties")
            if (keyFile.exists()) {
                val properties = Properties()
                properties.load(keyFile.inputStream())
                val signKeyName = "dev-key"
                signingConfigs {
                    create(signKeyName) {
                        storeFile = properties["keystore"]?.let { rootProject.file(it) }
                        storePassword = properties["storePassword"]?.toString()
                        keyAlias = properties["keyAlias"]?.toString()
                        keyPassword = properties["keyPassword"]?.toString()
                    }
                }
                signingConfig = signingConfigs.getByName(signKeyName)
            }
        }
        create("prd") {
            dimension = serverDimension
            resValue("string", "app_name", "MovieDb")
            buildConfigField("boolean", "MOCK_DATA", "false")
            val keyFile = rootProject.file("signing/release.properties")
            if (keyFile.exists()) {
                val properties = Properties()
                properties.load(keyFile.inputStream())
                val signKeyName = "prd-key"
                signingConfigs {
                    create(signKeyName) {
                        storeFile = properties["keystore"]?.let { rootProject.file(it) }
                        storePassword = properties["storePassword"]?.toString()
                        keyAlias = properties["keyAlias"]?.toString()
                        keyPassword = properties["keyPassword"]?.toString()
                    }
                }
                signingConfig = signingConfigs.getByName(signKeyName)
            }
        }
    }
    applicationVariants.all {
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/\"")
        buildConfigField("String", "WALL_HAVEN_API_KEY", "\"81220b3dccf20362c5451ee74639e4ea\"")
        when (flavorName) {
            "dev" -> {
            }

            "prd" -> {
            }
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

//    kotlin {
//        jvmToolchain(8)
//    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//tasks.withType(type = org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask::class) {
//    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
//}

kapt {
    correctErrorTypes = true
}

jacoco {
    toolVersion = "0.8.8"
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")


    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // room
    // https://developer.android.com/topic/libraries/architecture/room
    implementation("androidx.room:room-runtime:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")
    ksp("androidx.hilt:hilt-compiler:1.1.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("io.coil-kt:coil-compose:2.5.0")
}
