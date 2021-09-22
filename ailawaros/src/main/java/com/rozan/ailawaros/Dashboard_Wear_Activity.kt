package com.rozan.ailawaros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rozan.ailawaros.adapter.AilaAdapter
import com.rozan.liquordeliveryapplication.repository.AilaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Dashboard_Wear_Activity : WearableActivity() {

    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard__wear_)

        setAmbientEnabled()

        recyclerView = findViewById(R.id.recycler_view)
        loaditem()

    }

    private fun loaditem() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productRepositary = AilaRepository()
                val response = productRepositary.getAllAila()
                val lstProduct = response.data!!
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = AilaAdapter(lstProduct, this@Dashboard_Wear_Activity)
                    recyclerView.layoutManager =
                        LinearLayoutManager(this@Dashboard_Wear_Activity, RecyclerView.VERTICAL, false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Dashboard_Wear_Activity, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}