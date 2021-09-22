package com.rozan.liquordeliveryapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.Wave
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.io.IOException

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        //Background thread, usually for showing while server checks user details.
        CoroutineScope(Dispatchers.IO).launch { //coroutine scope is a light weight thread.
            delay(3000)
            runOnUiThread(kotlinx.coroutines.Runnable {

                progressBar = findViewById(R.id.spin_kit)
                val doubleBounce: Sprite = DoubleBounce()
                progressBar.indeterminateDrawable = doubleBounce


            })
            checkLogin()
//            finish()
        }
    }

    private fun checkLogin() {
    val sharedPref=getSharedPreferences("MyPref", MODE_PRIVATE)
    val username=sharedPref.getString("username","")
    val password=sharedPref.getString("password","")
        if (username != null && !username.equals("")) {

            startActivity(Intent(this@SplashScreenActivity,AilaActivity::class.java))
    }
        else{

            startActivity(Intent(this@SplashScreenActivity,LoginActivity::class.java))
        }
        CoroutineScope(Dispatchers.IO).launch {
            if (username == null && password == null) {
                withContext(Main) {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }
            }
            else {
                try {
                    val repository = UserRepository()
                    val response = repository.checkUser(username.toString(), password.toString())
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer ${response.token}"
                        ServiceBuilder.userId="${response.userId}"
                        ServiceBuilder.data=response.data!!
                        startActivity(Intent(this@SplashScreenActivity, AilaActivity::class.java))
                        finish()
                    }
                }
                catch (ex: IOException) {
                    withContext(Main) {
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }

    }

}