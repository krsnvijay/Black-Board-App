package com.notadeveloper.app.blackboard

import com.squareup.leakcanary.LeakCanary
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import kotlin.properties.Delegates
import io.realm.RealmConfiguration




/**
 * Created by krsnv on 10/10/2017.
 */
class MyApplication : Application() {


  override fun onCreate() {
    super.onCreate()
    Realm.init(this);
    val config = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .build()
    Realm.setDefaultConfiguration(config)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }
    LeakCanary.install(this)
    Stetho.initializeWithDefaults(this)
    Stetho.initialize(
        Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
            .build())


    // Normal app init code...
  }

}