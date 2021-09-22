package com.rozan.liquordeliveryapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.BinderThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Carts
import com.rozan.liquordeliveryapplication.model.AddToCartNotification
import com.rozan.liquordeliveryapplication.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class AilaDetailsActivity : AppCompatActivity(), View.OnClickListener,SensorEventListener {
    private lateinit var imgAila:ImageView
    private lateinit var tvName:TextView
    private lateinit var tvType:TextView
    private lateinit var tvMl:TextView
    private lateinit var tvPrice:TextView
    private lateinit var tvDes:TextView
    private lateinit var tvDescrip:TextView
    private lateinit var btnSub:Button
    private lateinit var btnAdd: Button
    private lateinit var ailaQty: TextView
    private lateinit var btnAddToCart: Button

    var counter=1
    private var ailaImage:String?=null
    private  var ailaName:String?=null
    private var ailaType:String?=null

    private var ailaMl:String?=null
    private var ailaPrice:Double?=0.0
    private var ailaid:String?=null

    private lateinit var sensorManager: SensorManager
    private var gyrosensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aila_details)

        imgAila=findViewById(R.id.imgAila)
        tvName=findViewById(R.id.ailaName)
        tvType=findViewById(R.id.ailaType)
        tvMl=findViewById(R.id.ailaMl)
        tvPrice=findViewById(R.id.ailaPrice)
        tvDes=findViewById(R.id.ailaDes)
        tvDescrip=findViewById(R.id.ailaDescrip)
        btnSub=findViewById(R.id.btnSub)
        btnAdd=findViewById(R.id.btnAdd)
        ailaQty=findViewById(R.id.ailaQty)
        btnAddToCart=findViewById(R.id.btnAddCart)

        gyrosensors()
        val intent=intent.getParcelableExtra<Aila>("aila")
        if (intent!=null){
            ailaid=intent._id
             ailaName=intent.ailaName
             ailaType=intent.ailaType
             ailaMl=intent.ailaMl
             ailaPrice=intent.ailaPrice
            ailaImage=intent.ailaImage.toString()
            tvName.text=ailaName
            tvType.text=ailaType
            tvMl.text=ailaMl.toString()
            tvPrice.text=ailaPrice.toString()

            val imagePath = ServiceBuilder.loadImagePath() + intent.ailaImage!!.split("\\")[1]
            Glide.with(this)
                    .load(imagePath)
                    .into(imgAila)



        }
        btnAdd.setOnClickListener(this)
        btnSub.setOnClickListener(this)
        btnAddToCart.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAdd->{
                counter++
                ailaQty.text=counter.toString()
                if(counter>1){
                    btnSub.isClickable=true
                }

            }
            R.id.btnSub->{
                counter--
                ailaQty.text=counter.toString()
                if(counter<=1){
                    btnSub.isClickable=false
                }


            }
            R.id.btnAddCart -> {
                addToCart()
            }
        }
    }
    private fun addToCart(){
        val ailaQty=ailaQty.text.toString().toInt()
        val userId = ServiceBuilder.userId!!
        val cart= Carts(ailaQty = ailaQty,userId = userId)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRepository= CartRepository()
                val response=cartRepository.addToCart(ailaid!!,cart)
                if (response.success==true){

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@AilaDetailsActivity, "Product added to cart", Toast.LENGTH_SHORT).show()
                        addtoCartNotification()
                    }
                }
            }
            catch (ex: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AilaDetailsActivity,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

        private fun addtoCartNotification(){
                val notificationManager=NotificationManagerCompat.from(this)
                val addToCartNotification=AddToCartNotification(this)
                addToCartNotification.createNotificationChannel()

                val notification=NotificationCompat.Builder(this,addToCartNotification.CHHANEL1)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle("Notification")
                        .setContentText("Product Added to Cart!")
                        .setColor(Color.BLACK)
                        .build()
                notificationManager.notify(1,notification)


            }

    fun gyrosensors(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(!checkSensor()){
            return
        }else{
            gyrosensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, gyrosensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    private fun checkSensor(): Boolean{
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
            flag=false
        }
        return flag
    }
    override fun onSensorChanged(event: SensorEvent?) {

        val values = event!!.values[1]
        if (values<-10){
                addToCart()
                startActivity(Intent(this,CartActivity::class.java))

        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


}