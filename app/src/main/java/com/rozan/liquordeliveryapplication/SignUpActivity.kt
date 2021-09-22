package com.rozan.liquordeliveryapplication

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.entity.User
import com.rozan.liquordeliveryapplication.entity.Users
import com.rozan.liquordeliveryapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var etFname:EditText
    private lateinit var etLname:EditText
    private lateinit var etDOB:EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var btnSignup:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etFname=findViewById(R.id.etFname)
        etLname=findViewById(R.id.etLname)
        etDOB=findViewById(R.id.etDOB)
        etUsername=findViewById(R.id.etUsername)
        etEmail=findViewById(R.id.etEmail)
        etPassword=findViewById(R.id.etPassword)
        btnSignup=findViewById(R.id.btnSignup)

        etDOB.setOnClickListener {
            loadCalendar()
        }

        btnSignup.setOnClickListener {
            registerUser()
        }
    }



    private fun loadCalendar() {
        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog=DatePickerDialog(this,DatePickerDialog.OnDateSetListener{
            view,year,monthOfYear,dayOfMonth->
            etDOB.setText("$dayOfMonth/${monthOfYear+1}/$year")

        },
        year,
        month,
        day)
        datePickerDialog.show()
    }

    private fun registerUser() {
        if (checkEmpty()) {


            val fname = etFname.text.toString()
            val lname = etLname.text.toString()
            val dob = etDOB.text.toString()
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val user = Users(
                firstName= fname,
                lastName = lname,
                dob = dob,
                username = username,
                email = email,
                password = password
            ) //providing parameters to User

            //Performing task in background thread so using CoroutineScope as it is light weight thread
            CoroutineScope(
                    Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.registerUser(user)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(
                                    this@SignUpActivity,
                                    response.message,
                                    Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Main) {
                        Toast.makeText(
                                this@SignUpActivity,
                                "Error registering user",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            etFname.text.clear()
            etLname.text.clear()
            etDOB.text.clear()
            etUsername.text.clear()
            etEmail.text.clear()
            etPassword.text.clear()
        }

    }
    private fun checkEmpty():Boolean{
    var flag=true

        when{
            TextUtils.isEmpty(etFname.text) -> {
                etFname.error = "Enter your first name"
                etFname.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etLname.text) -> {
                etLname.error = "Enter your last name"
                etLname.requestFocus()
                flag = false
            }

            TextUtils.isEmpty(etUsername.text) -> {
                etUsername.error = "Enter your username"
                etUsername.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etEmail.text)->{
                etEmail.error="Enter valid email"
                etEmail.requestFocus()
                flag=false
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