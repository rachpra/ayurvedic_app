package com.rozan.liquordeliveryapplication.ui.account

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.rozan.liquordeliveryapplication.AilaActivity
import com.rozan.liquordeliveryapplication.LoginActivity
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.User
import com.rozan.liquordeliveryapplication.entity.Users
import com.rozan.liquordeliveryapplication.repository.UserRepository
import com.rozan.liquordeliveryapplication.ui.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : Fragment(),SensorEventListener {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var profileImage: ImageView
    private lateinit var tvFullname:TextView
    private lateinit var tvName:TextView
    private lateinit var tvUsername:TextView
    private lateinit var tvMail:TextView
    private lateinit var tvDOB:TextView
    private lateinit var tvLogout:TextView
    private lateinit var btnEdit: Button

    lateinit var popAddPost: Dialog
    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDOB: EditText
    private lateinit var btnSave:Button

    private lateinit var sensorManager: SensorManager
    private var proximitysensor: Sensor? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
                ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.account_fragment, container, false)
        proximity()
        accountViewModel.text.observe(viewLifecycleOwner, Observer {
            val context = root.context
            val data = ServiceBuilder.data!!  // fetching user data from servicebuilder
            profileImage = root.findViewById(R.id.profile)
            tvFullname = root.findViewById(R.id.tvFullname)
            tvName = root.findViewById(R.id.tvName)
            tvUsername = root.findViewById(R.id.tvUsername)
            tvMail = root.findViewById(R.id.tvMail)
            tvDOB= root.findViewById(R.id.tvDOB)
            tvLogout= root.findViewById(R.id.tvLogout)
            profileImage = root.findViewById(R.id.profile)

            tvFullname.text=data[0].firstName +" "+ data[0].lastName
            tvName.text=data[0].firstName +" "+ data[0].lastName
            tvUsername.text=data[0].username
            tvMail.text=data[0].email
            tvDOB.text=data[0].dob


            profileImage.setOnClickListener{
                loadPopUpMenu()
            }
            tvLogout.setOnClickListener{
                logout()
            }


            if (ServiceBuilder.loadImagePath() + data[0].userImage!! == " ") {
                Glide.with(context)
                    .load(R.drawable.logo)
                    .fitCenter()
                    .into(profileImage)
            }

                val imagePath = ServiceBuilder.loadImagePath() + data[0].userImage!!.split("\\")[1]
                Glide.with(context)
                    .load(imagePath)
                    .into(profileImage)


            popupWindow(root,context)
            iniPopup(root,context,data)
        })
        return root
    }
    fun proximity(){
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(!checkSensor()){
            return
        }else{
            proximitysensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, proximitysensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    private fun checkSensor(): Boolean{
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)==null){
            flag=false
        }
        return flag
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values<3)
            logoutUser()
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    fun logout(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete student")
        builder.setMessage("Are you sure you want to logout??")
        builder.setIcon(android.R.drawable.stat_sys_warning)
        builder.setPositiveButton("Yes") { _, _ ->
           logoutUser()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

       }

    private fun logoutUser() {
        startActivity( Intent( context,  LoginActivity::class.java ) )
        val preferences = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()

    }


    private fun popupWindow(root:View,context: Context) {
        btnEdit = root.findViewById(R.id.btnEdit)
        btnEdit.setOnClickListener {
            popAddPost.show()
        }
    }

    private fun iniPopup(root: View,context: Context,data:MutableList<Users>) {
        popAddPost = Dialog(context)
        popAddPost.setContentView(R.layout.popup_edit)
        popAddPost.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popAddPost.window?.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.window?.attributes?.gravity = Gravity.CENTER;

        etFirstName = popAddPost.findViewById(R.id.etFirstName)
        etLastName = popAddPost.findViewById(R.id.etLastName)
        etUsername = popAddPost.findViewById(R.id.etUsername)
        etEmail = popAddPost.findViewById(R.id.etEmail)
        etDOB = popAddPost.findViewById(R.id.etDOB)
        btnSave=popAddPost.findViewById(R.id.btnSave)


        etFirstName.setText(data[0].firstName)
        etLastName.setText(data[0].lastName)
        etUsername.setText(data[0].username)
        etEmail.setText(data[0].email)
        etDOB.setText(data[0].dob)

        btnSave.setOnClickListener {
            updateUser()
            popAddPost.dismiss()

        }
    }

    private fun updateUser() {
        val userId=ServiceBuilder.userId!!
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val user = Users(
                       firstName =  etFirstName.text.toString(), lastName = etLastName.text.toString(),
                        email = etEmail.text.toString(), username = etUsername.text.toString(),dob = etDOB.text.toString()
                )
                val response = userRepository.updateUser(userId, user)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                                context,
                                "Updated successfully",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                            context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadPopUpMenu(){
        val popupMenu = PopupMenu(context, profileImage)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)  //to open implicit activity outside of the app
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //opens camera of system
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = activity?.contentResolver
                val cursor =
                    contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                profileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                profileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                uploadImage()
            }
        }
    }
    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
    private fun uploadImage() {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file)
            val image =
                MultipartBody.Part.createFormData("userImage", file.name, reqFile)
            val userId=ServiceBuilder.userId!!
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val productRepository = UserRepository()
                    val response = productRepository.uploadImage(userId, image)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                } catch (ex: java.lang.Exception) {
                    withContext(Main) {
                        Log.d("Mero Error ", ex.localizedMessage)
                        Toast.makeText(
                            context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}