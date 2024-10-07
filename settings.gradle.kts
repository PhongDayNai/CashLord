pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://maven.fabric.io/public") }
        maven { url = uri("https://android-sdk.is.com/") }
        //flatDir { dirs 'libs' }
        maven { url = uri("https://jitpack.io") }
        //maven { url = uri("https://s3.amazonaws.com/moat-sdk-builds") }
        //maven { url = uri("https://artifacts.applovin.com/android") }
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }

        //maven { url = uri("https://sdk.safedk.com/maven") }
    }
}

rootProject.name = "Cash King"
include(":app")
 