package com.notadeveloper.app.blackboard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.notadeveloper.app.blackboard.models.Faculty
import com.squareup.leakcanary.LeakCanary


/**
 * Created by krsnv on 10/10/2017.
 */
class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }
    LeakCanary.install(this)
    Stetho.initializeWithDefaults(this)
    preferences = getSharedPreferences(
        "credentials", Context.MODE_PRIVATE)

  }

  companion object {
    lateinit var instance: MyApplication
    lateinit var preferences: SharedPreferences
    fun getFaculty(): Faculty {
      val faculty = Faculty(
          facultyId = preferences.getString("faculty_id", ""),
          name = preferences.getString("name", ""),
          dept = preferences.getString("dept", ""),
          inchargeOf = preferences.getString("incharge_of", null),
          facultyType = preferences.getString("faculty_type", ""),
          email = preferences.getString("email", ""),
          password = preferences.getString("password", ""),
          phone = preferences.getString("phone", ""),
          responsibilities = preferences.getStringSet("responsibilities",
              emptySet<String>()).toList(),
          detail = null
      )
      return faculty
    }

    fun setFaculty(faculty: Faculty) {
      with(preferences.edit()) {
        putString("faculty_id", faculty.facultyId)
        putString("name", faculty.name)
        putString("dept", faculty.dept)
        putString("incharge_of", faculty.inchargeOf)
        putString("faculty_type", faculty.facultyType)
        putString("email", faculty.email)
        putString("password", faculty.password)
        putString("phone", faculty.phone)
        putStringSet("responsibilities", faculty.responsibilities.toSet())
        commit()
      }
    }

    fun deleteFaculty() {
      with(preferences.edit()) {
        putString("faculty_id", "")
        putString("name", "")
        putString("dept", "")
        putString("incharge_of", "")
        putString("faculty_type", "")
        putString("email", "")
        putString("password", "")
        putString("phone", "")
        commit()
      }
    }
  }
}