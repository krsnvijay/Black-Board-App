package com.notadeveloper.app.blackboard.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.CurrentFacultyList
import com.notadeveloper.app.blackboard.models.CurrentFacultySchedule
import com.notadeveloper.app.blackboard.models.FacultyList
import com.notadeveloper.app.blackboard.models.FacultySchedule
import com.notadeveloper.app.blackboard.ui.adapters.ExpandableListAdapter
import com.notadeveloper.app.blackboard.ui.adapters.facultylist_adapter
import com.notadeveloper.app.blackboard.ui.adapters.facultytimetable_adapter
import com.notadeveloper.app.blackboard.util.RetrofitInterface
import com.notadeveloper.app.blackboard.util.snack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_form.form_lin
import kotlinx.android.synthetic.main.activity_form.lvExp
import kotlinx.android.synthetic.main.activity_form.parent_layout
import kotlinx.android.synthetic.main.activity_form.recycler_view
import kotlinx.android.synthetic.main.form_layout.autocomplete_text_view
import kotlinx.android.synthetic.main.form_layout.day
import kotlinx.android.synthetic.main.form_layout.dept
import kotlinx.android.synthetic.main.form_layout.hour
import kotlinx.android.synthetic.main.form_layout.section
import kotlinx.android.synthetic.main.form_layout.submit
import kotlinx.android.synthetic.main.form_layout.year
import kotlin.properties.Delegates


class FormActivity : AppCompatActivity() {
    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        realm = Realm.getDefaultInstance()
        val bundle: Bundle = intent.extras
        val actiontype = bundle.getString("action_type")
        UIchange(actiontype)
//    val trlist: ArrayList<TableRow> = ArrayList<TableRow>()
//    for (i in 0..5) {
//      val tr: TableRow = TableRow(this)
//      trlist.add(tr)
//      for (j in 0..8) {
//        val t: TextView = TextView(this)
//        t.setText("Free")
//        tr.addView(t)
//      }
//      table_layout.addView(tr)
//
//    }

    }

    fun UIchange(actiontype: String) {
        when (actiontype) {
            getString(R.string.class_location), getString(R.string.class_timetable) -> {
                day.visibility = View.GONE
                hour.visibility = View.GONE
                autocomplete_text_view.visibility = View.GONE
              submit.setOnClickListener {
                    val class_id = dept.selectedItem.toString() + "-" + year.selectedItem.toString() + "-" + section.selectedItem.toString()
                    val compositeDisposable: CompositeDisposable = CompositeDisposable()
                    val apiService = RetrofitInterface.create()

                    compositeDisposable.add(
                            apiService.getClass(class_id)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({ result ->
                                        Log.e("eg", result.classTimetable.toString())
                                        val mondaylist = ArrayList<String>()
                                        val tuesdaylist = ArrayList<String>()
                                        val wednesdaylist = ArrayList<String>()
                                        val thursdaylist = ArrayList<String>()
                                        val fridaylist = ArrayList<String>()
                                        for (item in result.classTimetable) {
                                            val day1 = item.day
                                            if (day1.equals("Monday"))
                                                mondaylist.add((item.hour).toInt() - 1, item.subjCode)
                                            else if (day1.equals("Tuesday"))
                                                tuesdaylist.add((item.hour).toInt() - 1, item.subjCode)
                                            else if (day1.equals("Wednesday"))
                                                wednesdaylist.add((item.hour).toInt() - 1, item.subjCode)
                                            else if (day1.equals("Thursday"))
                                                thursdaylist.add((item.hour).toInt() - 1, item.subjCode)
                                            else if (day1.equals("Friday"))
                                                fridaylist.add((item.hour).toInt() - 1, item.subjCode)
                                        }
                                        val listDataHeader = ArrayList<String>()
                                        val listDataChild = HashMap<String, List<String>>()
                                        lvExp.visibility = View.VISIBLE
                                        if (mondaylist.isEmpty()==false) {
                                            listDataChild.put("Monday", mondaylist)
                                            listDataHeader.add("Monday")
                                        }
                                        if (tuesdaylist.isEmpty()==false) {
                                            listDataChild.put("Tuesday", tuesdaylist)
                                            listDataHeader.add("Tuesday")
                                        }
                                        if (wednesdaylist.isEmpty()==false) {
                                            listDataChild.put("Wednesday", wednesdaylist)
                                            listDataHeader.add("Wednesday")
                                        }
                                        if (thursdaylist.isEmpty()==false) {
                                            listDataChild.put("Thursday", thursdaylist)
                                            listDataHeader.add("Thursday")
                                        }
                                        if (fridaylist.isEmpty()==false) {
                                            listDataChild.put("Friday", fridaylist)
                                            listDataHeader.add("Friday")
                                        }
                                        if(listDataHeader.isEmpty()==false)
                                        {
                                            parent_layout.snack("NO DATA")
                                        }
                                        Log.e("eg", listDataHeader.toString())
                                        Log.e("dhg", listDataChild.toString())
                                        val listAdapter = ExpandableListAdapter(this, listDataHeader, listDataChild)

                                        // setting list adapter
                                        lvExp.setAdapter(listAdapter)
                                        parent_layout.snack(result.classId + " is at " + result.location)
//                    val adapter = classtimetable_adapter(result.classTimetable)
//                    recycler_view.layoutManager = LinearLayoutManager(this)
//                    recycler_view.adapter = adapter
//                    form_lin.visibility = View.GONE
//                    recycler_view.visibility = View.VISIBLE
                                    }, { error ->
                                        parent_layout.snack("Requested Data not Stored In DB")
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
                    val compositeDisposable: CompositeDisposable = CompositeDisposable()
                    val apiService = RetrofitInterface.create()
                    compositeDisposable.add(
                            apiService.getAvailability(dept.selectedItem.toString(), day.selectedItem.toString(),
                                    hour.selectedItem.toString())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({ result ->
                                        Log.e("eg", result.availability.toString())
                                        val adapter = facultylist_adapter(result.availability)
                                        recycler_view.layoutManager = LinearLayoutManager(this)
                                        recycler_view.adapter = adapter
                                        form_lin.visibility = View.GONE
                                        recycler_view.visibility = View.VISIBLE
                                    }, { error ->
                                        parent_layout.snack("Requested Data not Stored In DB")
                                        error.printStackTrace()
                                    })
                    )
                }
            }
            getString(R.string.my_schedule) -> {
                val curentuserschedule = realm.where(CurrentFacultySchedule::class.java).findAll()
                val lst: ArrayList<FacultySchedule>? = ArrayList()
                for (item in curentuserschedule) {
                    lst?.add(FacultySchedule(item.classId, item.subjCode, item.day, item.hour, item.classIdLocation))
                }
                val adapter = facultytimetable_adapter(lst)
                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.adapter = adapter
                form_lin.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE


            }
            getString(R.string.contact_hod_s) -> {
                val currentuserlist = realm.where(CurrentFacultyList::class.java).findAll()
                val lst: ArrayList<FacultyList>? = ArrayList()
                for (item in currentuserlist) {
                    lst?.add(FacultyList(item.facultyId, item.name, item.phone, item.dept))
                }
                val adapter = facultylist_adapter(lst)
                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.adapter = adapter
                form_lin.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE


            }
            getString(R.string.faculty_schedule) -> {
                year.visibility = View.GONE
                section.visibility = View.GONE
                dept.visibility = View.GONE
                day.visibility = View.GONE
                hour.visibility = View.GONE
                val facultylist = realm.where(CurrentFacultyList::class.java).findAll()
                val flist: HashMap<String, String> = HashMap()
                for (item in facultylist) {
                    flist[item.name] = item.facultyId

                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        ArrayList(flist.keys))
                autocomplete_text_view.setAdapter(adapter)
                submit.setOnClickListener {
                    val name = autocomplete_text_view.text.toString()
                    if (!name.isEmpty() && flist.contains(name)) {
                        val compositeDisposable: CompositeDisposable = CompositeDisposable()
                        val apiService = RetrofitInterface.create()
                        val id = flist.get(name) ?: ""
                        compositeDisposable.add(
                                apiService.getFaculty(id)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe({ result ->
                                            Log.e("eg", result.facultySchedule.toString())
                                            val adapter2 = facultytimetable_adapter(result.facultySchedule)
                                            recycler_view.layoutManager = LinearLayoutManager(this)
                                            recycler_view.adapter = adapter2
                                            form_lin.visibility = View.GONE
                                            recycler_view.visibility = View.VISIBLE
                                        }, { error ->
                                            parent_layout.snack("Requested Data not Stored In DB")
                                            error.printStackTrace()
                                        })
                        )
                    }
                }
            }
        }
    }

}
