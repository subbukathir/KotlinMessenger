package com.linlax.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener({
            if(!etEmail.text.toString().isEmpty() && !etPassword.text.toString().isEmpty()) {
                Log.d(TAG,"email: " + etEmail.text + " password : "+ etPassword.text)
                firebaseAuth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var currentUser = firebaseAuth.currentUser
                                Log.d(TAG," mail: " + currentUser.toString())
                            }else {
                                Log.d(TAG," mail: " + it.exception.toString())
                            }
                        }
                        .addOnFailureListener {
                            Log.d(TAG," mail: " + it.message)
                        }
            }
        })

        tvAlreadyRegistered.setOnClickListener({
            val intent = Intent(this,LogingActivity::class.java)
            startActivity(intent)
        })
    }
}
