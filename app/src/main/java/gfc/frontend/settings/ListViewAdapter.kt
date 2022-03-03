package gfc.frontend.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gfc.frontend.dataclasses.UserInfo
import android.widget.BaseAdapter
import gfc.frontend.R


internal class ListViewAdapter(private val context: Context, private val data: ArrayList<UserInfo>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(
                R.layout.family_member, null
            ) as View
        }
        val fn = convertView.findViewById<View>(R.id.userFriendlyName) as TextView
        val un = convertView.findViewById<View>(R.id.userName) as TextView
        val role = convertView.findViewById<View>(R.id.userRole) as TextView
        val points = convertView.findViewById<View>(R.id.childPoints) as TextView
        fn.text = data[position].friendlyName
        un.text = data[position].username
        points.text = "Punkty: " + data[position].points.toString()
        if(data[position].role == "PARENT"){
            role.text = "RODZIC"
        }
        else {
            role.text = "DZIECKO"
        }
        return convertView
    }
}