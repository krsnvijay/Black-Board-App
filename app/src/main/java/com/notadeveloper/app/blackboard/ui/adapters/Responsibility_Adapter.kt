package com.notadeveloper.app.blackboard.ui.adapters

/**
 * Created by krsnv on 1/13/2018.
 */


import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    holder.icon_phone.visibility = View.GONE
    holder.icon.visibility = View.GONE
    holder.tv2.visibility = View.GONE
    holder.tv2.text = responsibility.faculties_responsible.toString()
    holder.container_responsibility.setOnClickListener{
      val dialog: Dialog
      dialog = Dialog(holder.tv1.context);
      dialog.setContentView(R.layout.faculty_dialog)
      val header: TextView
      header = dialog.findViewById(R.id.header)
      val list_view: ListView
      list_view = dialog.findViewById(R.id.list_view)
      header.text = responsibility.responsibilityName
      val adapter = ArrayAdapter(holder.tv1.context,
              android.R.layout.simple_list_item_1, responsibility.faculties_responsible)
      list_view.setAdapter(adapter)
      dialog.show()
    }
  }

  class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv1: TextView
    val tv2: TextView
    val icon: ImageView
    val icon_phone: ImageView
    val container_responsibility: RelativeLayout

    init {
      tv1 = itemView.findViewById(R.id.text1)
      tv2 = itemView.findViewById(R.id.text2)
      icon = itemView.findViewById(R.id.icon)
      icon_phone = itemView.findViewById(R.id.icon_phone)
      container_responsibility = itemView.findViewById(R.id.container_responsibility)
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
