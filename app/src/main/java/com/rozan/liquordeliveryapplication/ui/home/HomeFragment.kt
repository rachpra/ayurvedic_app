package com.rozan.liquordeliveryapplication.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.adapter.AilaAdapter
import com.rozan.liquordeliveryapplication.adapter.AilaCategoryAdapter
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.model.AilaCategory
import com.rozan.liquordeliveryapplication.repository.AilaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var picksRV: RecyclerView
    private lateinit var recommendedRV: RecyclerView
    private lateinit var whiskyRV: RecyclerView
    private lateinit var vodkaRV: RecyclerView
    private lateinit var wineRV: RecyclerView
    private lateinit var categRecyclerView: RecyclerView

    private var ailaList = arrayListOf<Aila>()
    private var ailaCategory = arrayListOf<AilaCategory>()
    private lateinit var homeViewModel: HomeViewModel
    override fun onAttach(context: Context) {
        super.onAttach(requireContext())

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            val context = root.context
            categoryRecyclerView(root, context)
            ailaRecyclerView(root, context)
        })
        return root
    }

    private fun categoryRecyclerView(view: View, context: Context) {
        categRecyclerView = view.findViewById(R.id.categRecyclerView)
        val adapter = AilaCategoryAdapter(ailaCategory, context)
        categRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        categRecyclerView.adapter = adapter

        loadCateg()
    }

    private fun loadCateg() {
        ailaCategory.add((AilaCategory(categName = "Whisky", categImage = "https://i.pinimg.com/originals/63/68/3c/63683c9df2d0c015fa3320297f216780.jpg")))
        ailaCategory.add((AilaCategory(categName = "Rum", categImage = "https://cdn5.vectorstock.com/i/1000x1000/37/84/cartoon-bottle-rum-vector-29563784.jpg")))
        ailaCategory.add((AilaCategory(categName = "Vodka", categImage = "https://st3.depositphotos.com/3557671/13489/v/950/depositphotos_134893664-stock-illustration-glass-bottle-of-vodka-icon.jpg")))
        ailaCategory.add((AilaCategory(categName = "Wine", categImage = "http://dentistinsaginawtx.com/wp-content/uploads/2014/07/red-wine.jpg")))
        ailaCategory.add((AilaCategory(categName = "Beer", categImage = "https://i.pinimg.com/736x/d7/5a/3c/d75a3c612cf968ce7a81e011ac576cdb.jpg")))
    }

    private fun ailaRecyclerView(view: View, context: Context) {
        picksRV = view.findViewById(R.id.picksRV)
        recommendedRV = view.findViewById(R.id.recommendedRV)
        whiskyRV=view.findViewById(R.id.whiskyRV)
        vodkaRV=view.findViewById(R.id.vodkaRV)
        loadPicks(context)
        loadRec(context)
        loadWhisky(context)
        loadVodka(context)
    }

    private fun loadVodka(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ailaRepository = AilaRepository()
                val response = ailaRepository.getAilaByCateg("Vodka")
                val lstRec=response.data!!
                withContext(Dispatchers.Main) {
                    vodkaRV.adapter = AilaAdapter(lstRec, context)
                    vodkaRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadWhisky(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ailaRepository = AilaRepository()
                val response = ailaRepository.getAilaByCateg("Whisky")
                val lstRec=response.data!!
                withContext(Dispatchers.Main) {
                    whiskyRV.adapter = AilaAdapter(lstRec, context)
                    whiskyRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadRec(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ailaRepository = AilaRepository()
                val response = ailaRepository.getAilaByCateg("Rum")
                val lstRec=response.data!!
                withContext(Dispatchers.Main) {
                    recommendedRV.adapter = AilaAdapter(lstRec, context)
                    recommendedRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadPicks(context: Context) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ailaRepository = AilaRepository()
                val response = ailaRepository.getAllAila(context)

                withContext(Dispatchers.Main) {
                    picksRV.adapter = AilaAdapter(response, context)
                    picksRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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