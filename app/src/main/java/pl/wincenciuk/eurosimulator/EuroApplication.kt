package pl.wincenciuk.eurosimulator

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.wincenciuk.eurosimulator.di.mainModule

class EuroApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EuroApplication)
            modules(mainModule)
        }
    }
}