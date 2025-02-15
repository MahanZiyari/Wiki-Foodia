package ir.mahan.wikifoodia.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // initializing Calligraphy
        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/atlas_regular.ttf")
                        .build()
                )
            ).build()
        )
        // Night Mode Disable
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Timber
        Timber.plant(Timber.DebugTree())
    }
}