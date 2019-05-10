package com.zwe.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.zwe.myapplication.adapters.ProductAdapter
import com.zwe.myapplication.libby.H
import com.zwe.myapplication.libby.H.Companion.USER_TOKEN
import com.zwe.myapplication.libby.H.Companion.l
import com.zwe.myapplication.modals.Products
import com.zwe.myapplication.services.ServiceBuilder
import com.zwe.myapplication.services.WebService
import kotlinx.android.synthetic.main.activity_single_category_product.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleCategoryProduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_category_product)

        if(H.checkUserAuth()){
            val intent= Intent(this,LogInActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        var bundle: Bundle = intent.extras
        var catId = bundle.getString("cat_id")

        singleCategoryProductRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        loadAllCategorOfAProduct(catId)

    }
    private fun loadAllCategorOfAProduct(catId : String){
        val services : WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseProduct : Call<Products> = services.getProductOfACategory("Bearer $USER_TOKEN",catId.toInt())

        responseProduct.enqueue(object : Callback<Products>{
            override fun onFailure(call: Call<Products>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if(response.isSuccessful){
                    val prods = response.body()!!
                    val products = prods.products
                    singleCategoryProductRecycler.adapter=ProductAdapter(this@SingleCategoryProduct,products)
                    l("There are ${products.size}")
                }else{
                    l("Something not right")
                }
            }

        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val item : MenuItem = menu.findItem(R.id.cart)
        MenuItemCompat.setActionView(item,R.layout.my_cart_layout)
        return super.onCreateOptionsMenu(menu)
    }
}
