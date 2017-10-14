package com.notadeveloper.app.blackboard.ui.adapters

/**
 * Created by Chirag on 10/12/2017.
 */

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.notadeveloper.app.blackboard.R
import java.util.HashMap

class ExpandableListAdapter(private val _context: Context,
    private val _listDataHeader: List<String> // header titles
    ,
    // child data in format of header title, child title
    private val _listDataChild: HashMap<String, List<String>>) : BaseExpandableListAdapter() {

  override fun getChild(groupPosition: Int, childPosititon: Int): Any =
      this._listDataChild[this._listDataHeader[groupPosition]]?.get(childPosititon) ?: "No Data"

  override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

  override fun getChildView(groupPosition: Int, childPosition: Int,
      isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
    var convertView = convertView

    val childText = getChild(groupPosition, childPosition) as String

    if (convertView == null) {
      val infalInflater = this._context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      convertView = infalInflater.inflate(R.layout.listt_item, null)
    }

    val txtListChild = convertView!!
        .findViewById<TextView>(R.id.lblListItem)

    txtListChild.text = (childPosition + 1).toString() + ". " + childText
    return convertView
  }

  override fun getChildrenCount(groupPosition: Int): Int =
      this._listDataChild[this._listDataHeader[groupPosition]]?.size ?: 0

  override fun getGroup(groupPosition: Int): Any = this._listDataHeader[groupPosition]

  override fun getGroupCount(): Int = this._listDataHeader.size

  override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

  override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
      convertView: View?, parent: ViewGroup): View {
    var convertView = convertView
    val headerTitle = getGroup(groupPosition) as String
    if (convertView == null) {
      val infalInflater = this._context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      convertView = infalInflater.inflate(R.layout.list_group, null)
    }

    val lblListHeader = convertView!!
        .findViewById<TextView>(R.id.lblListHeader)
    lblListHeader.setTypeface(null, Typeface.BOLD)
    lblListHeader.text = headerTitle

    return convertView
  }

  override fun hasStableIds(): Boolean = false

  override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}