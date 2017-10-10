package com.notadeveloper.app.blackboard.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.CurrentFaculty
import com.notadeveloper.app.blackboard.models.CurrentFacultyList
import com.notadeveloper.app.blackboard.models.CurrentFacultySchedule
import com.notadeveloper.app.blackboard.models.FacultyList
import com.notadeveloper.app.blackboard.util.RetrofitInterface
import com.notadeveloper.app.blackboard.util.snack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.email_sign_in_button
import kotlinx.android.synthetic.main.activity_login.login_form
import kotlinx.android.synthetic.main.activity_login.login_progress
import kotlinx.android.synthetic.main.activity_login.password
import kotlin.properties.Delegates

/**
 * A login screen that offers login via ID/password.
 */
class LoginActivity : AppCompatActivity() {


  private var realm: Realm by Delegates.notNull()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    // Set up the login form.
    realm = Realm.getDefaultInstance()
    if (!realm.isEmpty){
    startActivity(Intent(this,MainActivity::class.java))
      finish()
    }
//    if(user.isValid)
//      startActivity(Intent(this, MainActivity::class.java))
    password.setOnEditorActionListener(
        TextView.OnEditorActionListener { textView, id, keyEvent ->
          if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin()
            return@OnEditorActionListener true
          }
          false
        })
    email_sign_in_button.setOnClickListener { attemptLogin() }

  }


  /**
   * Callback received when a permissions request has been completed.
   */


  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private fun attemptLogin() {
/*    if (mAuthTask != null) {
      return
    }*/

    // Reset errors.
    email.error = null
    password.error = null

    // Store values at the time of the login attempt.
    val userid = email.text.toString()
    val pass = password.text.toString()

    var cancel = false
    var focusView: View? = null

    // Check for a valid password, if the user entered one.
    if (TextUtils.isEmpty(pass)) {
      password.error = getString(R.string.error_invalid_password)
      focusView = password
      cancel = true
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(userid)) {
      email.error = getString(R.string.error_field_required)
      focusView = email
      cancel = true
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView!!.requestFocus()
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true)
      authenticate(userid, pass)
//           mAuthTask!!.execute(null as Void?)
    }
  }

  fun authenticate(userid: String, pass: String) {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val apiService = RetrofitInterface.create()
    compositeDisposable.add(
        apiService.loginFaculty(userid, pass)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
              realm.executeTransaction {
                // Add a faculty to realm db
                realm.deleteAll()
                val faculty = realm.createObject(CurrentFaculty::class.java, result.facultyId)
                faculty.password = result.password
                faculty.email = result.email
                faculty.phone = result.phone
                faculty.dept = result.dept
                faculty.name = result.name
                faculty.facultyType = result.facultyType
                faculty.inchargeOf = result.inchargeOf
                for (item in result.facultyList.orEmpty())
                {
                  val facultylist=realm.createObject(CurrentFacultyList::class.java)
                  facultylist.facultyId=item.facultyId
                  facultylist.phone = item.phone
                  facultylist.dept = item.dept
                  facultylist.name = item.name

                }
                for (item in result.facultySchedule.orEmpty())
                {
                  val facultyshedule=realm.createObject(CurrentFacultySchedule::class.java)
                  facultyshedule.classId=item.classId
                  facultyshedule.classIdLocation = item.classIdLocation
                  facultyshedule.day = item.day
                  facultyshedule.hour = item.hour
                  facultyshedule.subjCode = item.subjCode

                }





              }

              showProgress(false)
              login_form?.snack("Hello $result Login Sucessful")
              startActivity(Intent(this, MainActivity::class.java))
            }, { error ->
              showProgress(false)
              login_form?.snack("Login Failed! Check Credentials")
              error.printStackTrace()
            })
    )


  }


  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) private fun showProgress(show: Boolean) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

      login_form.visibility = if (show) View.GONE else View.VISIBLE
      login_form.animate().setDuration(shortAnimTime.toLong()).alpha(
          (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
      })

      login_progress.visibility = if (show) View.VISIBLE else View.GONE
      login_progress.animate().setDuration(shortAnimTime.toLong()).alpha(
          (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          login_progress.visibility = if (show) View.VISIBLE else View.GONE
        }
      })
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      login_progress.visibility = if (show) View.VISIBLE else View.GONE
      login_form.visibility = if (show) View.GONE else View.VISIBLE
    }
  }


  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
/*  inner class UserLoginTask internal constructor(private val mEmail: String,
      private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void): Boolean? {
      // TODO: attempt authentication against a network service.

      try {
        // Simulate network access.
        Thread.sleep(2000)
      } catch (e: InterruptedException) {
        return false
      }

      for (credential in DUMMY_CREDENTIALS) {
        val pieces = credential.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (pieces[0] == mEmail) {
          // Account exists, return true if the password matches.
          return pieces[1] == mPassword
        }
      }

      // TODO: register the new account here.
      return true
    }

    override fun onPostExecute(success: Boolean?) {
      mAuthTask = null
      showProgress(false)

      if (success!!) {
        finish()
      } else {
        password.error = getString(R.string.error_incorrect_password)
        password.requestFocus()
      }
    }

    override fun onCancelled() {
      mAuthTask = null
      showProgress(false)
    }
  }*/
  override fun onDestroy() {
    super.onDestroy()
    realm.close()
  }
}

