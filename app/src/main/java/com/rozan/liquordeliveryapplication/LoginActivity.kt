package com.rozan.liquordeliveryapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.entity.User
import com.rozan.liquordeliveryapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvSignUp: TextView
    private lateinit var btnLogin: Button
    private lateinit var checkme: CheckBox
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var btnFb: LoginButton
    private val EMAIL = "email"
    val callbackManager = CallbackManager.Factory.create();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvSignUp = findViewById(R.id.tvSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        checkme = findViewById(R.id.checkme)
        btnFb = findViewById(R.id.btnfb)
        constraintLayout = findViewById(R.id.constraintLayout)
        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        btnLogin.setOnClickListener {
            login()
        }

        btnFb.setReadPermissions(Arrays.asList(EMAIL));
        btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun login() {
        if (checkEmpty()) {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepository()
                    val response = repository.checkUser(username, password)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer ${response.token}"
                        ServiceBuilder.userId="${response.userId}"
                        ServiceBuilder.data=response.data!!
                        if (checkme.isChecked) {
                            saveSharedPref()
                            startActivity(
                                    Intent(
                                            this@LoginActivity,
                                            AilaActivity::class.java
                                    )
                            )
                            finish()
                        } else {
                            startActivity(
                                    Intent(
                                            this@LoginActivity,
                                            AilaActivity::class.java
                                    )
                            )
                            finish()
                        }


                    } else {
                        withContext(Dispatchers.Main) {
                            val snack =
                                    Snackbar.make(
                                            constraintLayout,
                                            "Invalid username or password",
                                            Snackbar.LENGTH_LONG
                                    )
                            snack.setAction("OK", View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.setActionTextColor(Color.WHITE)
                            snack.setBackgroundTint(Color.parseColor("#515BD4"))

                            snack.show()

                        }
                    }
                } catch (ex: IOException) {
                    withContext(Main){
                        val snack =
                            Snackbar.make(
                                constraintLayout,
                                "Invalid username or password",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK", View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.setActionTextColor(Color.WHITE)
                        snack.setBackgroundTint(Color.parseColor("#515BD4"))

                        snack.show()
                    }

                }
            }
        }
    }

    private fun fbLogin() {


    }


    private fun saveSharedPref() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)  //shared preference banako
        val editor = sharedPref.edit()

        editor.putString("username", username)
        editor.putString("password", password)

        editor.apply()

    }

    private fun checkEmpty(): Boolean {
        var flag = true
        when {
            TextUtils.isEmpty(etUsername.text) -> {
                etUsername.error = "Enter your username"
                etUsername.requestFocus()
                flag = false
            }

            TextUtils.isEmpty(etPassword.text) -> {
                etPassword.error = "Enter your password"
                etPassword.requestFocus()
                flag = false
            }
        }
        return flag
    }
}