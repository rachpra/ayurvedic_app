package com.rozan.liquordeliveryapplication.ui.favorites

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.adapter.FavoriteAdapter
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.ui.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment(),SensorEventListener {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor?=null
    override fun onAttach(context: Context) {
        super.onAttach(requireContext())

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
                ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        accelerometer()
        favoritesViewModel.text.observe(viewLifecycleOwner, Observer {
            val context = root.context
            favRecyclerView(root, context)
        })
        return root
    }

    private fun favRecyclerView(view: View, context: Context) {
        favRecyclerView=view.findViewById(R.id.favRV)

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val fav= AilaDB.getInstance(context).getFavoritesDAO().loadAllFavorite()
                withContext(Main){
                    favRecyclerView.adapter=FavoriteAdapter(fav,context)
                    favRecyclerView.layoutManager= LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }

            }
            catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                            "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun accelerometer(){
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(!checkSensor()){
            return
        }else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
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
        val values = event!!.values
        val xAxis = values[0]
        val yAxis = values[1]
        val zAxis = values[2]
        Toast.makeText(context, "$xAxis,$yAxis,$zAxis", Toast.LENGTH_SHORT).show()
        if (xAxis>5 ||yAxis>0.3||zAxis>5){

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}