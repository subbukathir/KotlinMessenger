package com.linlax.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val TAG = "RegisterActivity"

    val firebaseAuth = FirebaseAuth.getInstance()

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnRegister.setOnClickListener({
                createUser()
        })

        tvAlreadyRegistered.setOnClickListener({
            val intent = Intent(this,LogingActivity::class.java)
            startActivity(intent)
        })

        ivProfileRegister.setOnClickListener {
            Log.d(TAG,"photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            Log.d(TAG,"photo selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)

            ivProfileRegister.setBackgroundDrawable(bitmapDrawable)

            iv_selectedPhoto.setImageBitmap(bitmap)

            ivProfileRegister.alpha = 0f
            ivProfileRegister.setText("")
        }else {
            ivProfileRegister.setText(R.string.select_photo)
        }
    }

    private fun createUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if(!email.isEmpty() && !password.isEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful){
                            Toasty.warning(this, "not success",Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }
                        Toasty.success(this,"Successfully registered" ,Toast.LENGTH_SHORT).show()

                        uploadImageToFirebase()
                    }
        }else Toasty.warning(this,"Email and password should not be blank" ,Toast.LENGTH_SHORT).show()
    }

    private fun uploadImageToFirebase() {

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(TAG,"Successfully uploaded image: ${it.metadata?.path}}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.e(TAG,"file location: $it")

                        saveUserToFirebaseDatabase("$it")
                    }
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = firebaseAuth.uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, etUsername.text.toString(),profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.e(TAG,"We saved user to firebase database")
                    Toasty.success(this,"We saved user to firebase database successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.e(TAG,"failed : ${it.message}")
                    Toasty.error(this,"We are sorry : ${it.message}",Toast.LENGTH_SHORT).show()
                }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)