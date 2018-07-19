package com.linlax.kotlinmessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_loging.*

class LogingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loging)

        tvBackToRegister.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}
