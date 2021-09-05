package gfc.frontend.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gfc.frontend.R
import gfc.frontend.dataclasses.temporaryDatabase

class listAdapter :RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return MyViewHolder(listRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.elementTitle
        val description = holder.elementDescription
        val done = holder.elementCheck

        name.setText(temporaryDatabase.tasks[position].title)
        description.setText(temporaryDatabase.tasks[position].description)
//        done.setChecked(temporaryDatabase.tasks[position].done)
    }

    override fun getItemCount(): Int {
        return temporaryDatabase.tasks.size
    }
}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: TextView = itemView.findViewById(R.id.elementCheck)
}