package com.zwe.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zwe.myapplication.R
import com.zwe.myapplication.SingleCategoryProduct
import com.zwe.myapplication.modals.Category
import kotlinx.android.synthetic.main.category_row.view.*

class CategoryAdapter(val context : Context,val cats : List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row,p0,false))
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var cats=cats[p1]
        p0.itemView.categoryName.text=cats.name
        p0.itemView.categoryName.setOnClickListener {
            val intent=Intent(context,SingleCategoryProduct::class.java)
            intent.putExtra("cat_id",cats.id.toString())
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

}