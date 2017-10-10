package com.notadeveloper.app.blackboard.util

import android.support.design.widget.Snackbar
import android.view.View

/**
 * Created by krsnv on 10/10/2017.
 */
fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
  val snack = Snackbar.make(this, message, length)
  snack.show()
}