package dev.ogabek.kotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.manager.AuthHandler
import dev.ogabek.kotlin.manager.AuthManager
import dev.ogabek.kotlin.utils.Extensions.toast
import java.lang.Exception

class SignInActivity : BaseActivity() {

    lateinit var et_email: EditText
    lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    fun initViews(){
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener { firebaseSignIn(et_email.text.toString(), et_password.text.toString()) }
        val tv_signup = findViewById<TextView>(R.id.tv_signup)
        tv_signup.setOnClickListener { callSignUpActivity() }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess() {
                dismissLoading()
                toast("Sign In Successfully")
                callMainActivity()
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                exception?.printStackTrace()
                toast("Sign In Failed")
            }
        })
    }
}