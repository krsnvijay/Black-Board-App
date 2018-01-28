package com.notadeveloper.app.blackboard.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.notadeveloper.app.blackboard.MyApplication
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.Schedule
import com.notadeveloper.app.blackboard.ui.adapters.ExpandableListAdapter
import com.notadeveloper.app.blackboard.ui.adapters.Responsibility_Adapter
import com.notadeveloper.app.blackboard.ui.adapters.facultylist_adapter
import com.notadeveloper.app.blackboard.util.RetrofitInterface
import com.notadeveloper.app.blackboard.util.snack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.form_layout.autocomplete_text_view
import kotlinx.android.synthetic.main.form_layout.day
import kotlinx.android.synthetic.main.form_layout.dept
import kotlinx.android.synthetic.main.form_layout.hour
import kotlinx.android.synthetic.main.form_layout.section
import kotlinx.android.synthetic.main.form_layout.submit
import kotlinx.android.synthetic.main.form_layout.year


class FormActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_form)
    val bundle: Bundle = intent.extras
    val actiontype = bundle.getString("action_type")
    UIchange(actiontype)

  }

  fun UIchange(actiontype: String) {
    when (actiontype) {
       getString(R.string.class_timetable) -> {
        day.visibility = View.GONE
        hour.visibility = View.GONE
        autocomplete_text_view.visibility = View.GONE
        submit.setOnClickListener {
          val class_id = dept.selectedItem.toString() + "-" + year.selectedItem.toString() + "-" + section.selectedItem.toString()
          val compositeDisposable = CompositeDisposable()
          val apiService = RetrofitInterface.create()
            showProgress(true,recycler_view)

          compositeDisposable.add(
              apiService.getClass(class_id)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe({ result ->
                    Log.e("eg", result.classTimetable.toString())
                      showProgress(false,recycler_view)
                    parent_layout.snack(result.classId + " is at " + result.location)
                    populateSchedule(result.classTimetable, true)
                    recycler_view.layoutManager = LinearLayoutManager(this)
                    recycler_view.visibility = View.VISIBLE
                  }, { error ->
                      showProgress(false,recycler_view)
                    parent_layout.snack("Requested Data not Stored In DB")
                    error.printStackTrace()
                  })
          )
        }
      }
      getString(R.string.class_location)->
      {
          day.visibility = View.GONE
          hour.visibility = View.GONE
          recycler_view.visibility = View.GONE
          autocomplete_text_view.visibility = View.GONE
          submit.setOnClickListener {
              showProgress(true,location_card)
              val class_id = dept.selectedItem.toString() + "-" + year.selectedItem.toString() + "-" + section.selectedItem.toString()
              val compositeDisposable = CompositeDisposable()
              val apiService = RetrofitInterface.create()

              compositeDisposable.add(
                      apiService.getClass(class_id)
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribeOn(Schedulers.io())
                              .subscribe({ result ->
//                                  Log.e("eg", result.classTimetable.toString())
//                                  parent_layout.snack(result.classId + " is at " + result.location)
//                                  populateSchedule(result.classTimetable, true)
//                                  recycler_view.layoutManager = LinearLayoutManager(this)
                                  showProgress(false,location_card)
                                  location_text.text = "Class "+result.classId+" is located at "+result.location
                              }, { error ->
                                  showProgress(false,location_card)
                                  location_text.text = "Requested Data not stored in Data Base"
                                  error.printStackTrace()
                              })
              )
          }
      }
      getString(R.string.available_faculty) -> {
        year.visibility = View.GONE
        section.visibility = View.GONE
        autocomplete_text_view.visibility = View.GONE
        submit.setOnClickListener {
            showProgress(true, recycler_view)
          val compositeDisposable: CompositeDisposable = CompositeDisposable()
          val apiService = RetrofitInterface.create()
          compositeDisposable.add(
              apiService.getAvailability(dept.selectedItem.toString(), day.selectedItem.toString(),
                  hour.selectedItem.toString())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe({ result ->
                      showProgress(false , recycler_view)
                    Log.e("eg", result.toString())
                    val adapter = facultylist_adapter(result)
                    recycler_view.layoutManager = LinearLayoutManager(this)
                    recycler_view.adapter = adapter
                    form_lin.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE
                  }, { error ->
                      showProgress(false, recycler_view)
                    parent_layout.snack("Requested Data not Stored In DB")
                    error.printStackTrace()
                  })
          )
        }
      }
      getString(R.string.my_schedule) -> {
          submit.visibility = View.GONE
          form_lin.visibility = View.GONE
        val compositeDisposable: CompositeDisposable = CompositeDisposable()
        val apiService = RetrofitInterface.create()
        compositeDisposable.add(
            apiService.getFacultySchedule(MyApplication.getFaculty().facultyId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                  populateSchedule(result)
                  recycler_view.layoutManager = LinearLayoutManager(this)

                  recycler_view.visibility = View.VISIBLE
                }, { error ->
                  parent_layout.snack("Requested Data not Stored In DB")
                  error.printStackTrace()
                })
        )

      }
      getString(R.string.contact_hod_s) -> {
          if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)!=
                  PackageManager.PERMISSION_GRANTED){
              Toast.makeText(this,"Please grant the required permission!", Toast.LENGTH_SHORT).show()
              ActivityCompat.requestPermissions(this as Activity, arrayOf(android.Manifest.permission.CALL_PHONE),1)
          }
        //TODO New UI
        form_lin.visibility = View.GONE
        val compositeDisposable = CompositeDisposable()
        val apiService = RetrofitInterface.create()
        compositeDisposable.add(
            apiService.getFacultyList("", "HOD")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                  Log.e("eg", result.toString())
                  val adapter = facultylist_adapter(result)
                  recycler_view.layoutManager = LinearLayoutManager(this)
                  form_lin.visibility = View.GONE
                  recycler_view.adapter = adapter
                  recycler_view.visibility = View.VISIBLE
                }, { error ->
                  parent_layout.snack("Requested Data not Stored In DB")
                  error.printStackTrace()
                })
        )

      }
      getString(R.string.responsibilities) -> {
        year.visibility = View.GONE
        section.visibility = View.GONE
        day.visibility = View.GONE
        hour.visibility = View.GONE
        dept.visibility = View.GONE
          submit.visibility = View.GONE
        autocomplete_text_view.hint = "Search Responsibility"
        val compositeDisposable = CompositeDisposable()
        val apiService = RetrofitInterface.create()
        compositeDisposable.add(
            apiService.getResponsibilities()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                  Log.e("eg", result.toString())
                  val adapter = Responsibility_Adapter(result)
                  autocomplete_text_view.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                      adapter.getFilter().filter(p0)
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }
                  })
                  recycler_view.layoutManager = LinearLayoutManager(this)
                  recycler_view.adapter = adapter
                  recycler_view.visibility = View.VISIBLE
                }, { error ->
                  parent_layout.snack("Requested Data not Stored In DB")
                  error.printStackTrace()
                })
        )

      }
      getString(R.string.faculty_schedule) -> {
          showProgress(true, recycler_view)
        year.visibility = View.GONE
        section.visibility = View.GONE
        day.visibility = View.GONE
        hour.visibility = View.GONE
          submit.visibility = View.GONE
        //TODO New UI
        val compositeDisposable = CompositeDisposable()
        val apiService = RetrofitInterface.create()
        compositeDisposable.add(
            apiService.getFacultyList("", "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    showProgress(false, recycler_view)
                  Log.e("eg", result.toString())
                  val adapter = facultylist_adapter(result)
                  dept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                      TODO(
                          "not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                      adapter.getFilter(dept.selectedItem.toString()).filter("")
                      autocomplete_text_view.setText("")
                    }

                  }
                  adapter.getFilter(dept.selectedItem.toString()).filter("")
                  autocomplete_text_view.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                      adapter.getFilter(dept.selectedItem.toString()).filter(p0)
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }
                  })
                  recycler_view.layoutManager = LinearLayoutManager(this)
                  recycler_view.adapter = adapter
                  recycler_view.visibility = View.VISIBLE
                }, { error ->
                    showProgress(false, recycler_view)
                  parent_layout.snack("Requested Data not Stored In DB")
                  error.printStackTrace()
                })
        )

      }
    }
  }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot call unless you grant permission!", Toast.LENGTH_LONG).show()

                } else {
                }
            }
        }
    }

  fun populateSchedule(schedule: List<Schedule>, classtimetable: Boolean = false) {
    val mondaylist = Array<String>(8, { "Free Period" })
    val tuesdaylist = Array<String>(8, { "Free Period" })
    val wednesdaylist = Array<String>(8, { "Free Period" })
    val thursdaylist = Array<String>(8, { "Free Period" })
    val fridaylist = Array<String>(8, { "Free Period" })
    for (item in schedule) {
      val day1 = item.day
      var dispString = item.classId + "," + item.subjCode + "," + item.classLocation
      if (classtimetable) dispString += "\n" + item.facultyName

      if (day1.equals("Monday"))
        mondaylist[(item.hour).toInt() - 1] = dispString
      else if (day1.equals("Tuesday"))
        tuesdaylist[(item.hour).toInt() - 1] = dispString
      else if (day1.equals("Wednesday"))
        wednesdaylist[(item.hour).toInt() - 1] = dispString
      else if (day1.equals("Thursday"))
        thursdaylist[(item.hour).toInt() - 1] = dispString
      else if (day1.equals("Friday"))
        fridaylist[(item.hour).toInt() - 1] = dispString
    }
    val listDataHeader = ArrayList<String>()
    val listDataChild = HashMap<String, List<String>>()
    lvExp.visibility = View.VISIBLE
    if (mondaylist.isEmpty() == false) {
      listDataChild.put("Monday", mondaylist.toList())
      listDataHeader.add("Monday")
    }
    if (tuesdaylist.isEmpty() == false) {
      listDataChild.put("Tuesday", tuesdaylist.toList())
      listDataHeader.add("Tuesday")
    }
    if (wednesdaylist.isEmpty() == false) {
      listDataChild.put("Wednesday", wednesdaylist.toList())
      listDataHeader.add("Wednesday")
    }
    if (thursdaylist.isEmpty() == false) {
      listDataChild.put("Thursday", thursdaylist.toList())
      listDataHeader.add("Thursday")
    }
    if (fridaylist.isEmpty() == false) {
      listDataChild.put("Friday", fridaylist.toList())
      listDataHeader.add("Friday")
    }
    if (listDataHeader.isEmpty()) {
      parent_layout.snack("NO DATA")
    }
    Log.e("eg", listDataHeader.toString())
    Log.e("dhg", listDataChild.toString())
    val listAdapter = ExpandableListAdapter(this, listDataHeader, listDataChild)

    // setting list adapter
    lvExp.setAdapter(listAdapter)

  }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) private fun showProgress(show: Boolean, view: View) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            view.visibility = if (show) View.GONE else View.VISIBLE
            view.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            login_progress_form.visibility = if (show) View.VISIBLE else View.GONE
            login_progress_form.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    login_progress_form.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress_form.visibility = if (show) View.VISIBLE else View.GONE
            view.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
