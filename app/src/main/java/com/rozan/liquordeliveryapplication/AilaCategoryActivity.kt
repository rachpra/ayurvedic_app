package com.rozan.liquordeliveryapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rozan.liquordeliveryapplication.adapter.AilaAdapter
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.model.AilaCategory
import com.rozan.liquordeliveryapplication.repository.AilaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AilaCategoryActivity : AppCompatActivity() {
    private lateinit var category:RecyclerView
    private  var categName:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aila_category)
        category=findViewById(R.id.category)

        val intent=intent.getParcelableExtra<AilaCategory>("categ")
        if (intent!=null){
            categName=intent.categName
        }
        loadBeer(this)
    }

    private fun loadBeer(context: Context) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ailaRepository = AilaRepository()
                val response = ailaRepository.getAilaByCateg(categName!!)

                val lstCart = response.data!!

                withContext(Dispatchers.Main) {
                    category.adapter = AilaAdapter(lstCart!!, context)
//                    Toast.makeText(this@AilaCategoryActivity, "ds$categName", Toast.LENGTH_SHORT).show()
                    category.layoutManager = GridLayoutManager(this@AilaCategoryActivity,  2)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}