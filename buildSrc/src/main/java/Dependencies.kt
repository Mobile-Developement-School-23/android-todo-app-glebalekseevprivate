package dependencies
import Versions

object Dependencies {
    object Dagger {
        const val DAGGER = "com.google.dagger:dagger:${Versions.Libraries.DAGGER}"
        const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Versions.Libraries.DAGGER}"
    }

    object Work {
        const val WORK = "androidx.work:work-runtime:${Versions.AndroidX.WORK}"
        const val WORK_KTX = "androidx.work:work-runtime-ktx:${Versions.AndroidX.WORK}"
    }

    object Room {
        const val ROOM = "androidx.room:room-ktx:${Versions.AndroidX.ROOM}"
        const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.AndroidX.ROOM}"
        const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.AndroidX.ROOM}"
    }

    object YandexAuth {
        const val YANDEX_AUTH = "com.yandex.android:authsdk:${Versions.Libraries.YANDEX_AUTH}"
    }

    object Retrofit {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.Libraries.RETROFIT}"
        const val CONVERTER_GSON =
            "com.squareup.retrofit2:converter-gson:${Versions.Libraries.RETROFIT}"
    }

    object LoggingInterceptorOkhttp {
        const val LOGGING_INTERCEPTOR_OKHTTP =
            "com.squareup.okhttp3:logging-interceptor:${Versions.Libraries.LOGGING_INTERCEPTOR_OKHTTP}"
    }

    object Core {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.AndroidX.CORE_KTX}"
    }


    object AppCompat {
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.AndroidX.APP_COMPAT}"
    }

    object Material {
        const val MATERIAL = "com.google.android.material:material:${Versions.Libraries.MATERIAL}"
    }

    object ConstraintLayout {
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.CONSTRAINT_LAYOUT}"
    }

    object Lifecycle {
        const val LIFECYCLE_VIEWMODEL =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.LIFECYCLE_VERSION}"
        const val LIFECYCLE_RUNTIME =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.LIFECYCLE_VERSION}"
    }

    object Navigation {
        const val NAVIGATION_FRAGMENT =
            "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.NAVIGATION}"
        const val NAVIGATION_UI =
            "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.NAVIGATION}"
    }

    object SwipeRefreshLayout {
        const val SWIPE_REFRESH_LAYOUT =
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.AndroidX.SWIPE_REFRESH_LAYOUT}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.Test.JUNIT}"
    }

    object AndroidTest {
        const val ANDROID_JUNIT = "androidx.test.ext:junit:${Versions.Test.ANDROID_JUNIT}"
        const val ESPRESSO_CORE =
            "androidx.test.espresso:espresso-core:${Versions.Test.ESPRESSO_CORE}"
    }
}
