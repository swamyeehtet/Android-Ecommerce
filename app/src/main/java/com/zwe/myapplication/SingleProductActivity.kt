package com.zwe.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zwe.myapplication.libby.H
import com.zwe.myapplication.modals.Product
import kotlinx.android.synthetic.main.activity_single_product.*

class SingleProductActivity : AppCompatActivity() {

    var cartCount : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product)

        val product = intent.getParcelableExtra<Product>("product")

        tv_single_product_name.text = product.name
        Picasso.get().load(product.image).into(img_single_product)
        tv_single_product_price.text = "$ "+product.price.toString()
        tv_single_product_description.text = product.description

        addToCartImage.setOnClickListener {
            H.addToCart(product.id)
            cartCount?.text = H.getCartCount().toString()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val item : MenuItem = menu.findItem(R.id.cart)
        MenuItemCompat.setActionView(item,R.layout.my_cart_layout)

        val cartView = MenuItemCompat.getActionView(item)
        cartCount = cartView.findViewById(R.id.cart_count)
        val cartImage : ImageView = cartView.findViewById(R.id.cart_image)
        cartCount?.text = H.getCartCount().toString()

        cartImage.setOnClickListener {
            val intent = Intent(this@SingleProductActivity,MyCart::class.java)
            startActivity(intent)
        }

        return super.onCreateOptionsMenu(menu)
    }
}
