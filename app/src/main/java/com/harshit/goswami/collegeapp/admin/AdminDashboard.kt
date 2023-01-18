package com.harshit.goswami.collegeapp.admin

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.harshit.goswami.collegeapp.LoginActivity
import com.harshit.goswami.collegeapp.data.AdminLoginData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDashboardBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminEditProfileBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminViewProfileBinding
import java.io.IOException


class AdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var editProfileBinding: DialogAdminEditProfileBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private lateinit var editProfileDialog: AlertDialog
    var adminName = ""
    var adminId = ""
    var adminDepartment = ""
    var adminImgUrl = ""
    var adminPassword = ""
    private var fileUri: Uri? = null

    val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result: Uri? ->
        val bitmap: Bitmap
        if (result != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    fileUri = result
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, fileUri!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    editProfileBinding.DEPImgAdmin.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                try { // Setting image on image view using Bitmap
                    bitmap = MediaStore.Images.Media
                        .getBitmap(
                            contentResolver,
                            fileUri
                        )
                    editProfileBinding.DEPImgAdmin.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }

    //    override fun onBackPressed() {
//        super.onBackPressed()
//        editProfileDialog.dismiss()
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = DialogAdminEditProfileBinding.inflate(layoutInflater)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardClickListeners()
        getAdminPicture()

        binding.ADAdminImage.setOnClickListener {
            if (LoginActivity.isAdminLogin) {
                val popupMenu = PopupMenu(this, binding.ADAdminImage)
                popupMenu.menuInflater.inflate(
                    com.harshit.goswami.collegeapp.R.menu.logged_admin_menu,
                    popupMenu.menu
                )
                popupMenu.setOnMenuItemClickListener {
                    when (it.title) {
                        "View Profile" -> {
                            dialogViewProfile()
                        }
                        "Edit Profile" -> {
                            dialogEditProfile()
                        }
                        "Log Out" -> {
                            val pref: SharedPreferences =
                                getSharedPreferences("adminPref", MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putBoolean("adminLogin", false).apply()
                            startActivity(
                                Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                            )
                            finish()
                        }

                    }
                    true
                }

                popupMenu.show()
            }
        }
    }

    private fun dialogViewProfile() {
        val viewProfileBinding =
            DialogAdminViewProfileBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(
            this,
            com.harshit.goswami.collegeapp.R.style.CustomAlertDialogViewProfile
        ).create()
        builder.window?.setGravity(Gravity.TOP)
//                            val window: Window? = builder.window
//                            window!!.setGravity(Gravity.TOP)
//                            window.setLayout(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT
//                            )
        builder.setCancelable(true)
        builder.setView(viewProfileBinding.root)
        builder.setCanceledOnTouchOutside(true)

        fireDb.child("Admin")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                            Glide.with(this@AdminDashboard)
                                .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                .into(viewProfileBinding.DVPImgAdmin)
                            Glide.with(this@AdminDashboard)
                                .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                .into(binding.ADAdminImage)
                            viewProfileBinding.DVPAdminName.text =
                                snapshot.getValue(AdminLoginData::class.java)!!.fullName

                    } else Toast.makeText(
                        this@AdminDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AdminDashboard,
                        "Error!-${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        viewProfileBinding.VPBtnEditProfile.setOnClickListener {
            builder.dismiss()
            dialogEditProfile()
        }
        builder.show()
    }

    private fun dialogEditProfile() {
        try {
            editProfileBinding = DialogAdminEditProfileBinding.inflate(layoutInflater)
            editProfileDialog = AlertDialog.Builder(
                this,
                com.harshit.goswami.collegeapp.R.style.CustomAlertDialogEditProfile
            ).create()
            editProfileDialog.window?.setGravity(Gravity.TOP)
//        val window: Window? = editProfileDialog.window
//        window!!.setGravity(Gravity.TOP)
//        window.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
            editProfileDialog.setCancelable(true)
            editProfileDialog.setView(editProfileBinding.root)
            editProfileDialog.setCanceledOnTouchOutside(false)
            editProfileDialog.show()
        } catch (e: Exception) {
            Log.d("dialog Error-", "${e.message}")
        }


        editProfileBinding.DEPImgAdmin.setOnClickListener {
            imagePicker.launch(
                "image/*"
            )
        }
        fireDb.child("Admin")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                            Glide.with(this@AdminDashboard)
                                .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                .into(editProfileBinding.DEPImgAdmin)
                            editProfileBinding.DEPEdtFullname.setText(snapshot.getValue(AdminLoginData::class.java)!!.fullName)
//                            editProfileBinding.DEPEdtDepartment.setText(data.getValue(AdminLoginData::class.java)!!.department)
                            editProfileBinding.DEPEdtPassword.setText(snapshot.getValue(AdminLoginData::class.java)!!.password)
                            editProfileBinding.DEPEdtUserId.setText(snapshot.getValue(AdminLoginData::class.java)!!.userId)
                    } else Toast.makeText(
                        this@AdminDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AdminDashboard,
                        "Error!-${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        editProfileBinding.DEPBtnSubmit.setOnClickListener {
            UpdateProfile()
        }
    }

    private fun UpdateProfile() {
        validation()
        if (fileUri == null) uploadData()
        else uploadImgAndData()
    }

    private fun validation() {
        if (editProfileBinding.DEPEdtFullname.text.toString().isNotEmpty()) adminName =
            editProfileBinding.DEPEdtFullname.text.toString()
        else editProfileBinding.DEPEdtFullname.error = "this field Should not be Empty"

//        if (editProfileBinding.DEPEdtDepartment.text.toString().isNotEmpty()) adminDepartment =
//            editProfileBinding.DEPEdtDepartment.text.toString()
//        else editProfileBinding.DEPEdtDepartment.error = "this field Should not be Empty"

        if (editProfileBinding.DEPEdtUserId.text.toString().isNotEmpty()) adminId =
            editProfileBinding.DEPEdtUserId.text.toString()
        else editProfileBinding.DEPEdtUserId.error = "this field Should not be Empty"

        if (editProfileBinding.DEPEdtPassword.text.toString().isNotEmpty()) adminPassword =
            editProfileBinding.DEPEdtPassword.text.toString()
        else editProfileBinding.DEPEdtPassword.error = "Please Enter your Password"

    }

    private fun uploadImgAndData() {
        if (fileUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            val storageRef =
                FirebaseStorage.getInstance().reference.child("Admin").child("BScIT")
            // adding listeners on upload
            // or failure of image
            val uploadTask = storageRef.putFile(fileUri!!)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.addOnSuccessListener {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        storageRef.downloadUrl
                            .addOnSuccessListener { uri: Uri ->
                                adminImgUrl = uri.toString()
                                try {
                                    uploadData()
                                } catch (e: Exception) {
                                    Toast
                                        .makeText(
                                            this,
                                            "Data Failed:- " + e.message,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                    }
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast
                    .makeText(
                        this,
                        "Failed: " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val progress = (100.0
                        * taskSnapshot.bytesTransferred
                        / taskSnapshot.totalByteCount)
                progressDialog.setMessage(
                    "Uploaded "
                            + progress.toInt() + "%"
                )
            }
        }
    }

    private fun uploadData() {
        if (adminName != "" && adminId != "" && adminPassword != "") {
            fireDb.child("BScIT").child("Admin").child("AdminBScIT")
                .setValue(
                    AdminLoginData(
                        adminId,
                        adminName,
                        adminImgUrl,
                        adminDepartment,
                    )
                ).addOnSuccessListener {
                    Toast
                        .makeText(
                            this,
                            "Updated Successfully!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                .addOnFailureListener {
                    Toast
                        .makeText(
                            this,
                            "Failed: " + it.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
        }
    }

    private fun getAdminPicture() {
        fireDb.child("Admin")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                            Glide.with(this@AdminDashboard)
                                .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                .into(binding.ADAdminImage)

                    } else Toast.makeText(
                        this@AdminDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AdminDashboard,
                        "Error!-${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun cardClickListeners() {

        binding.cardUploadNotice.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadNotice::class.java
                )
            )
        }
        binding.uploadImage.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadImage::class.java
                )
            )
        }
        binding.addStudent.setOnClickListener {
            val i =  Intent(
                this,
                ManageStudent::class.java
            )
            i.putExtra("userType","admin")
            startActivity(i)
        }
        binding.updateFaculty.setOnClickListener {
            val i =  Intent(
                this,
                ManageFaculty::class.java
            )
            i.putExtra("userType","admin")
            startActivity(i)
        }
        binding.deleteNotice.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DeleteNotice::class.java
                )
            )
        }
    }
}