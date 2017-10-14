package com.notadeveloper.app.blackboard.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.FacultyList

/**
 * Created by krsnv on 10/11/2017.
 */


/**
 * Created by krsnv on 10/10/2017.
 */
class facultylist_adapter(
    private val list: List<FacultyList>?) : RecyclerView.Adapter<facultylist_adapter.viewholder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
    val context = parent.context
    val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

    return viewholder(itemView)
  }

  override fun onBindViewHolder(holder: viewholder, position: Int) {
    val faculty = list?.get(position)
    holder.tv1.text = faculty?.name + " " + faculty?.facultyId
    holder.tv2.text = faculty?.dept + " " + faculty?.phone
  }

  class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv1: TextView
    val tv2: TextView

    init {
      tv1 = itemView.findViewById(R.id.text1)
      tv2 = itemView.findViewById(R.id.text2)
    }
  }

  override fun getItemCount(): Int = list?.size ?: 0


}