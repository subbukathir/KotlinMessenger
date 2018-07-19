package com.linlax.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvAlreadyRegistered.setOnClickListener({
            val intent = Intent(this,LogingActivity::class.java)
            startActivity(intent)
        })
    }
}
