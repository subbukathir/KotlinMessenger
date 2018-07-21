package com.linlax.kotlinmessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_loging.*

class LogingActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loging)

        tvBackToRegister.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        btnLogin.setOnClickListener {
            login()

        }
    }

    private fun login() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()

        if(!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful)
                        {
                            Toasty.warning(this,"Signin not success",Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }

                        Toasty.success(this,"You are successfully loged in ",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toasty.error(this, "Error : ${it.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
        }else{
            Toasty.warning(this,"Email and password should not be blank ",Toast.LENGTH_SHORT).show()
        }


    }
}
