package com.zwe.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.zwe.myapplication.adapters.CartAdapter
import com.zwe.myapplication.libby.H
import com.zwe.myapplication.libby.H.Companion.USER_TOKEN
import com.zwe.myapplication.libby.H.Companion.clearCart
import com.zwe.myapplication.libby.H.Companion.l
import com.zwe.myapplication.modals.CartProduct
import com.zwe.myapplication.modals.ErrorMessager
import com.zwe.myapplication.services.ServiceBuilder
import com.zwe.myapplication.services.WebService
import kotlinx.android.synthetic.main.activity_my_cart.*
import kotlinx.android.synthetic.main.my_cart_layout.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCart : AppCompatActivity() {

    var cartCount : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        supportActionBar?.title = "My Cart"

        val cartKeys = H.getAllKeys()

        toast("get key Success")

        H.l("$cartKeys")

        myCartRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        getCartItems(cartKeys)
    }
    private fun getCartItems(cartKeys : String){
        val services : WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartProducts : Call<List<CartProduct>> = services.getCartPreviewItems("Bearer $USER_TOKEN",cartKeys)

        responseCartProducts.enqueue(object : Callback<List<CartProduct>>{
            override fun onFailure(call: Call<List<CartProduct>>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<List<CartProduct>>, response: Response<List<CartProduct>>) {
                if(response.isSuccessful){
                    val products : List<CartProduct> = response.body()!!
                    myCartRecycler.adapter = CartAdapter(this@MyCart,products)
                }else{
                    l("Something went wrong!")
                }
            }

        })
    }
    private fun billOut(){
        val cartKeys = H.getAllKeys()
        val services : WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartOrder : Call<ErrorMessager> = services.billOutOrder("Bearer $USER_TOKEN",cartKeys)

        responseCartOrder.enqueue(object : Callback<ErrorMessager>{
            override fun onFailure(call: Call<ErrorMessager>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<ErrorMessager>, response: Response<ErrorMessager>) {
                if(response.isSuccessful){
                    val message : ErrorMessager = response.body()!!
                    toast(message.msg)
                    clearCart()

                }else{
                    l("Something went wrong in Mycart!")
                }
            }

        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.billOut){
            billOut()
        }
        else if(item?.itemId==R.id.uploadProduct){
            startActivity(Intent(this@MyCart,ProductUpload::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
