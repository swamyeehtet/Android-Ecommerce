package com.zwe.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.squareup.picasso.Picasso
import com.zwe.myapplication.R
import com.zwe.myapplication.SingleProductActivity
import com.zwe.myapplication.libby.H.Companion.l
import com.zwe.myapplication.modals.Product
import kotlinx.android.synthetic.main.product_row.view.*
import org.jetbrains.anko.toast

class ProductAdapter(val context : Context,val products : List<Product>) : RecyclerView.Adapter
                <ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_row,p0,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val product=products[p1]
        p0.itemView.tvTitle.text=product.name
        Picasso.get().load(product.image).into(p0.itemView.tvImage)
        p0.itemView.tvPrice.text="US $"+product.price.toString()
        p0.itemView.btnDeatail.setOnClickListener {
            val intent = Intent(context,SingleProductActivity::class.java)
            intent.putExtra("product",product)
            context.startActivity(intent)
        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}