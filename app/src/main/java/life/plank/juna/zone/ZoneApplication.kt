package life.plank.juna.zone

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import io.alterac.blurkit.BlurKit
import io.fabric.sdk.android.Fabric
import life.plank.juna.zone.injection.component.*
import life.plank.juna.zone.injection.module.ContextModule

class ZoneApplication : Application() {

    lateinit var appComponent: AppComponent
        private set
    lateinit var uiComponent: UiComponent
        private set
    lateinit var networkComponent: NetworkComponent
        private set
    lateinit var viewModelComponent: ViewModelComponent
        private set

    companion object {
        lateinit var application: ZoneApplication
            private set

        val appContext: Context
            get() = application.applicationContext

        val gson: Gson
            get() = application.networkComponent.gson
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        Fabric.with(this, Crashlytics())
        BlurKit.init(this)

        appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(applicationContext))
                .build()
        networkComponent = appComponent.networkComponentBuilder()
                .build()
        uiComponent = networkComponent.viewComponentBuilder()
                .build()
        viewModelComponent = DaggerViewModelComponent.builder().build()
    }
}