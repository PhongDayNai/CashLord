plugins {
    alias(libs.plugins.android.application)
    id("applovin-quality-service")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.android)
}

applovin {
    //apiKey = "FsFGfiHBrph8vVPEZ8TbVlSRmdRWz8yhDCrR47HxWus9EzSmWy9qDwgKxkPaxsKf3cbd6h_TGoGI40fehXk9Af"
    apiKey = "OWtU1q845P4UdcDuFrM4Mz1BEdsn5UgzT1ly14TTRmW7TXxTqObXUQhscppTb05vszTXmMG2GUgeSBkoARH5xW"
}

android {
    namespace = "com.cashlord.earn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cashlord.earn"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.applovin:applovin-sdk:+") {
        exclude(group = "com.pangle.global", module = "ads-sdk")
    }
    implementation("com.applovin.mediation:google-ad-manager-adapter:+")
    implementation("com.applovin.mediation:google-adapter:+")
    implementation("com.applovin.mediation:ironsource-adapter:+")
    implementation("com.applovin.mediation:facebook-adapter:+")
    implementation("com.applovin.mediation:bytedance-adapter:+")
    implementation("com.applovin.mediation:unityads-adapter:+")

    implementation("com.android.volley:volley:1.2.1")

    //implementation("com.ironsource.sdk:mediationsdk:8.4.0")
    //implementation("com.ironsource:adqualitysdk:7.21.1")
    //implementation("com.ironsource.adapters:admobadapter:4.3.43") //mediation ironsource - gg
    //implementation("com.ironsource.adapters:facebookadapter:4.3.46") //mediation ironsource - meta
    //implementation("com.ironsource.adapters:mintegraladapter:4.3.26") //mediation ironsource - mintegral
    //implementation("com.ironsource.adapters:unityadsadapter:4.3.39") //mediation ironsource - unity
    //implementation("com.unity3d.ads:unity-ads:4.12.3")
    implementation("com.google.android.gms:play-services-ads-identifier:18.1.0")
    implementation("com.pangle.global:ads-sdk:6.2.0.6")
    implementation("com.vungle:publisher-sdk-android:6.12.0")

    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.google.android.gms:play-services-ads:23.3.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.android.gms:play-services-measurement-api:22.1.0")

    implementation("com.google.firebase:firebase-iid:21.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.core.ktx)

    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("com.github.aabhasr1:OtpView:v1.1.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.airbnb.android:lottie:6.5.2")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.mikhaellopez:circularprogressbar:3.1.0")
    implementation("com.github.AadeshDhimanDeveloper:scratchCardLayout:1.0.1")
    implementation("com.onesignal:OneSignal:[4.0.0, 4.99.99]")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.f0ris.sweetalert:library:1.5.6")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    //implementation(libs.safedk)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}