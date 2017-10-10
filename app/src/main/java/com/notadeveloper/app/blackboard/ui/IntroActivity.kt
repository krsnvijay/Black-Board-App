package com.notadeveloper.app.blackboard.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import com.github.paolorotolo.appintro.AppIntroFragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.github.paolorotolo.appintro.AppIntro
import com.notadeveloper.app.blackboard.R
import io.realm.Realm
import kotlin.properties.Delegates


/**
 * Created by krsnv on 10/11/2017.
 */
class IntroActivity : AppIntro() {
  private var realm: Realm by Delegates.notNull()
  override fun onCreate(@Nullable savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    realm = Realm.getDefaultInstance()
    if (!realm.isEmpty){
      startActivity(Intent(this,MainActivity::class.java))
      finish()
    }
    addSlide(AppIntroFragment.newInstance("Welcome! To BlackBoard", "College Management, Now made easier", R.drawable.ic_blackboard, ContextCompat.getColor(this,R.color.colorAccent)))
    addSlide(AppIntroFragment.newInstance("Supports different levels of college management!", "For Dean/HOD/Professor/Class-Incharge", R.drawable.ic_avatar, ContextCompat.getColor(this,R.color.colorPrimaryDark)))
    addSlide(AppIntroFragment.newInstance("Don't Know the Class Location?", "We got you covered!", R.drawable.ic_university, ContextCompat.getColor(this,R.color.colorBlue)))
    addSlide(AppIntroFragment.newInstance("Class/Faculty Schedule", "Now made more accessible", R.drawable.ic_event_black_24dp, ContextCompat.getColor(this,R.color.colorRed)))
    addSlide(AppIntroFragment.newInstance("One Application for all your College needs", "Login To Get Started", R.drawable.ic_smartphone, ContextCompat.getColor(this,R.color.colorPurple)))


  }

  override fun onSkipPressed(currentFragment: Fragment) {
    super.onSkipPressed(currentFragment)
    startActivity(Intent(this,LoginActivity::class.java))
    finish()
    // Do something when users tap on Skip button.
  }

  override fun onDonePressed(currentFragment: Fragment) {
    super.onDonePressed(currentFragment)
    startActivity(Intent(this,LoginActivity::class.java))
    finish()
    // Do something when users tap on Done button.
  }

  override fun onDestroy() {
    super.onDestroy()
    realm.close()
  }
}