package com.notadeveloper.app.blackboard.ui.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.Faculty
import com.notadeveloper.app.blackboard.util.RetrofitInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_schedule.*
import java.util.jar.Manifest

/**
 * Created by krsnv on 10/11/2017.
 */


/**
 * Created by krsnv on 10/10/2017.
 */
class facultylist_adapter(
    private val list: List<Faculty>) : RecyclerView.Adapter<facultylist_adapter.viewholder>() {
  var mFilteredList: List<Faculty>

  init {
    mFilteredList = list
  }

  lateinit var context: Context
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
    context = parent.context
    mFilteredList = list
    val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

    return viewholder(itemView)
  }

  override fun onBindViewHolder(holder: viewholder, position: Int) {
    val faculty = mFilteredList.get(position)
    holder.tv1.text = faculty.name + "("+ faculty.dept+")"
    holder.tv2.text = faculty.phone
    holder.container_faculty.setOnClickListener {
      val compositeDisposable = CompositeDisposable()
      val apiService = RetrofitInterface.create()
      compositeDisposable.add(
          apiService.getFacultySchedule(faculty.facultyId)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe({ result ->
                Log.e("eg", result.toString())
                val builder = AlertDialog.Builder(context)
                val inflater = LayoutInflater.from(context).inflate(R.layout.dialog_schedule,null)
                builder.setView(inflater)
                builder.setTitle("Schedule of "+faculty.name)
// 3. Get the AlertDialog from create()
                val dialog = builder.create()
                dialog.show()
                dialog.call.text = "CALL "+faculty.name
                dialog.call.setOnClickListener{
                    val i: Intent = Intent(Intent.ACTION_CALL)
                    val b:Bundle = Bundle()
                    i.setData(Uri.parse("tel:"+faculty.phone))
                    if(ActivityCompat.checkSelfPermission(holder.tv1.context, android.Manifest.permission.CALL_PHONE)!=
                            PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(holder.tv1.context,"Please grant the required permission!",Toast.LENGTH_SHORT).show()
                        ActivityCompat.requestPermissions(holder.tv1.context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE),1)
                    }
                    else
                    {
                        startActivity(holder.tv1.context,i,b)

                    }
                }


                val expLv: ExpandableListView = dialog.findViewById(R.id.explv_dialog)
                val mondaylist = Array<String>(8, { "Free Period" })
                val tuesdaylist = Array<String>(8, { "Free Period" })
                val wednesdaylist = Array<String>(8, { "Free Period" })
                val thursdaylist = Array<String>(8, { "Free Period" })
                val fridaylist = Array<String>(8, { "Free Period" })
                for (item in result) {
                  val day1 = item.day
                  if (day1.equals("Monday"))
                    mondaylist[(item.hour).toInt() - 1] = item.classId + "," + item.subjCode + "," + item.classLocation
                  else if (day1.equals("Tuesday"))
                    tuesdaylist[(item.hour).toInt() - 1] = item.classId + "," + item.subjCode + "," + item.classLocation
                  else if (day1.equals("Wednesday"))
                    wednesdaylist[(item.hour).toInt() - 1] = item.classId + "," + item.subjCode + "," + item.classLocation
                  else if (day1.equals("Thursday"))
                    thursdaylist[(item.hour).toInt() - 1] = item.classId + "," + item.subjCode + "," + item.classLocation
                  else if (day1.equals("Friday"))
                    fridaylist[(item.hour).toInt() - 1] = item.classId + "," + item.subjCode + "," + item.classLocation
                }
                val listDataHeader = ArrayList<String>()
                val listDataChild = HashMap<String, List<String>>()
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
                  Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show()
                }
                Log.e("eg", listDataHeader.toString())
                Log.e("dhg", listDataChild.toString())
                val listAdapter = ExpandableListAdapter(context, listDataHeader, listDataChild)
                expLv.setAdapter(listAdapter)

              }, { error ->
                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
              })
      )
    }



  }
    public fun request_permissions()
    {}



  class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv1: TextView
    val tv2: TextView
    val container_faculty: RelativeLayout

    init {
      tv1 = itemView.findViewById(R.id.text1)
      tv2 = itemView.findViewById(R.id.text2)
        container_faculty = itemView.findViewById(R.id.container_responsibility)
    }
  }

  override fun getItemCount(): Int = mFilteredList.size
  fun getFilter(dept: String): Filter {

    return object : Filter() {
      override fun performFiltering(charSequence: CharSequence): FilterResults {

        val charString = charSequence.toString()


        val filteredList = ArrayList<Faculty>()

        for (faculty in list) {

          if (faculty.name.toLowerCase().contains(
              charString.toLowerCase()) && faculty.dept == dept) {

            filteredList.add(faculty)
          }
        }

        mFilteredList = filteredList


        val filterResults = FilterResults()
        filterResults.values = mFilteredList
        return filterResults
      }

      override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
        mFilteredList = filterResults.values as List<Faculty>
        notifyDataSetChanged()
      }

    }
  }


}