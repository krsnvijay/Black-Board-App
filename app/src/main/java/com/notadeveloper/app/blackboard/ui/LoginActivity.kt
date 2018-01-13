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
import com.notadeveloper.app.blackboard.MyApplication
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.util.RetrofitInterface
import com.notadeveloper.app.blackboard.util.snack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.email_sign_in_button
import kotlinx.android.synthetic.main.activity_login.login_form
import kotlinx.android.synthetic.main.activity_login.login_progress
import kotlinx.android.synthetic.main.activity_login.password

/**
 * A login screen that offers login via ID/password.
 */
class LoginActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    // Set up the login form.
    if (!MyApplication.getFaculty().facultyId.isEmpty()) {
      startActivity(Intent(this, MainActivity::class.java))
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
              MyApplication.setFaculty(result)
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


}

