package com.zwe.myapplication

import android.content.Intent
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG



class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title="Register"

        register_button.setOnClickListener {
            val register_username=register_username.text
            val register_email=register_email.text
            val register_password=register_password.text
        }
        register_cancel.setOnClickListener {
            register_username.text=null
            register_email.text=null
            register_password.text=null
        }
        register_tv_gtl.setOnClickListener{
            finish()
        }
    }
}
