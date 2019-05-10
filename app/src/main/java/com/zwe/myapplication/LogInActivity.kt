package com.zwe.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zwe.myapplication.libby.H
import com.zwe.myapplication.libby.H.Companion.USER_TOKEN
import com.zwe.myapplication.libby.H.Companion.l
import com.zwe.myapplication.modals.Token
import com.zwe.myapplication.services.ServiceBuilder
import com.zwe.myapplication.services.WebService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title="logIn"
        tvLogInAds.isSelected=true

        btnLogIn.setOnClickListener{
            val email=etEmail.text.toString()
            val password=etPassword.text.toString()

            loginAUser(email,password)

        }
        btnCancel.setOnClickListener{
            etEmail.text=null
            etPassword.text=null
        }
        tvToRegister.setOnClickListener {
            val intent=Intent(this@LogInActivity,Register::class.java)
            startActivity(intent)
        }
    }

    fun loginAUser(email : String,password : String){
        val services : WebService=ServiceBuilder.buildService(WebService::class.java)
        val responseLogin : Call<Token> = services.loginUser(email,password)

        responseLogin.enqueue(object : Callback<Token>{
            override fun onFailure(call: Call<Token>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                val token : Token=response.body()!!
                USER_TOKEN=token.token
                l(token.token)

                val intent=Intent(this@LogInActivity,CategoryActivity::class.java)
                startActivity(intent)
            }

        })

    }
}
