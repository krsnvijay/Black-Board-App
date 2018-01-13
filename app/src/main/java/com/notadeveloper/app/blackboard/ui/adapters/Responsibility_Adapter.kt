package com.notadeveloper.app.blackboard.ui.adapters

/**
 * Created by krsnv on 1/13/2018.
 */


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import com.notadeveloper.app.blackboard.R
import com.notadeveloper.app.blackboard.models.Responsibility

/**
 * Created by krsnv on 10/11/2017.
 */


/**
 * Created by krsnv on 10/10/2017.
 */
class Responsibility_Adapter(
    private val list: List<Responsibility>) : RecyclerView.Adapter<Responsibility_Adapter.viewholder>() {
  var mFilteredList: List<Responsibility>

  init {
    mFilteredList = list
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
    val context = parent.context
    mFilteredList = list
    val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

    return viewholder(itemView)
  }

  override fun onBindViewHolder(holder: viewholder, position: Int) {
    val responsibility = mFilteredList.get(position)
    holder.tv1.text = responsibility.responsibilityName
    holder.tv2.text = responsibility.faculties_responsible.toString()
  }

  class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv1: TextView
    val tv2: TextView

    init {
      tv1 = itemView.findViewById(R.id.text1)
      tv2 = itemView.findViewById(R.id.text2)
    }
  }

  override fun getItemCount(): Int = mFilteredList.size
  fun getFilter(): Filter {

    return object : Filter() {
      override fun performFiltering(charSequence: CharSequence): FilterResults {

        val charString = charSequence.toString()


        val filteredList = ArrayList<Responsibility>()

        for (responsibility in list) {

          if (responsibility.responsibilityName.toLowerCase().contains(
              charString.toLowerCase()) || responsibility.faculties_responsible.toString().toLowerCase().contains(
              charString.toLowerCase())) {

            filteredList.add(responsibility)
          }
        }

        mFilteredList = filteredList


        val filterResults = FilterResults()
        filterResults.values = mFilteredList
        return filterResults
      }

      override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
        mFilteredList = filterResults.values as List<Responsibility>
        notifyDataSetChanged()
      }
    }
  }

}