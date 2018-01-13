package com.notadeveloper.app.blackboard.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import com.notadeveloper.app.blackboard.MyApplication
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.R.layout
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.available_faculty
import kotlinx.android.synthetic.main.content_main.class_location
import kotlinx.android.synthetic.main.content_main.class_timetable
import kotlinx.android.synthetic.main.content_main.contact_hod
import kotlinx.android.synthetic.main.content_main.faculty_schedule
import kotlinx.android.synthetic.main.content_main.info
import kotlinx.android.synthetic.main.content_main.my_schedule
import kotlinx.android.synthetic.main.content_main.responsibilities

class MainActivity : AppCompatActivity(), OnClickListener {
  override fun onClick(p0: View?) {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    setSupportActionBar(toolbar)

    val current_faculty = MyApplication.getFaculty()
    info.text = current_faculty.name + "\n(${current_faculty.facultyType})\nDept-${current_faculty.dept} \nIncharge of:${current_faculty.inchargeOf ?: "None"} \n Responsibilities:${current_faculty.responsibilities}"
    ButtonVisibilty(current_faculty.facultyType)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null)
          .show()
    }
    class_location.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", class_location.text.toString())
      startActivity(i)
    }
    class_timetable.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", class_timetable.text.toString())
      startActivity(i)
    }
    faculty_schedule.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", faculty_schedule.text.toString())
      startActivity(i)
    }
    my_schedule.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", my_schedule.text.toString())
      startActivity(i)
    }
    contact_hod.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", contact_hod.text.toString())
      startActivity(i)
    }
    available_faculty.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", available_faculty.text.toString())
      startActivity(i)
    }
    responsibilities.setOnClickListener {
      val i = Intent(this, FormActivity::class.java)
      i.putExtra("action_type", responsibilities.text.toString())
      startActivity(i)
    }

  }

  fun ButtonVisibilty(faculty_type: String) {
    when (faculty_type) {
      "Professor" -> {
        contact_hod.visibility = View.GONE
        faculty_schedule.visibility = View.GONE
        available_faculty.visibility = View.GONE
      }
      "HOD" -> {
        my_schedule.visibility = View.GONE
        contact_hod.visibility = View.GONE
      }
      "Dean" -> {
        my_schedule.visibility = View.GONE
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId


    return if (id == R.id.logout) {
      MyApplication.deleteFaculty()
      startActivity(Intent(this, LoginActivity::class.java))
      finish()
      true
    } else if (id == R.id.about) {
      val builder = AlertDialog.Builder(this)
      builder.setMessage("Developed by Vijay,Chirag,Chaitanya,Shrushti")
          .setTitle("Not A Developer")
      val dialog = builder.create()
      dialog.show()
      true
    } else super.onOptionsItemSelected(item)
  }

}
