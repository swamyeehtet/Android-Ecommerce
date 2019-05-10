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
import com.zwe.myapplication.modals.CartProduct
import com.zwe.myapplication.modals.Product
import kotlinx.android.synthetic.main.cart_row.view.*
import kotlinx.android.synthetic.main.product_row.view.*
import org.jetbrains.anko.toast

class CartAdapter(val context : Context, val products : List<CartProduct>) : RecyclerView.Adapter
                <CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_row,p0,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val product=products[p1]
        var productCount : Int = product.count
        p0.itemView.tv_cart_Title.text= "${product.name} ($productCount)"
        Picasso.get().load(product.image).into(p0.itemView.tv_cart_Image)
        p0.itemView.tv_cart_Price.text="US$ "+product.price.toString()

        p0.itemView.btnAdd.setOnClickListener {
             productCount += 1
             p0.itemView.tv_cart_Title.text= "${product.name} ($productCount)"

        }
        p0.itemView.btnSubtract.setOnClickListener {
            productCount -= 1
            p0.itemView.tv_cart_Title.text= "${product.name} ($productCount)"
        }
        p0.itemView.btnRemove.setOnClickListener {
            productCount = 0
            p0.itemView.tv_cart_Title.text= "${product.name} ($productCount)"
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}