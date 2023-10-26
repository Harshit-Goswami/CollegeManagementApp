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
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.harshit.goswami.collegeapp.LoginActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.modal.AdminLoginData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDashboardBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminChangePasswordBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminViewProfileBinding
import com.harshit.goswami.collegeapp.student.GalleryActivity
import java.io.IOException


class AdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var changePasswordBinding: DialogAdminChangePasswordBinding
    private lateinit var viewProfileBinding: DialogAdminViewProfileBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private lateinit var builder: AlertDialog

    //    var adminName = ""
    var adminImgUrl = ""
    var adminOldPassword = ""
    private var adminNewPassword = ""
    var adminReNewPassword = ""
    private var fileUri: Uri? = null

    private val imagePicker = registerForActivityResult(
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
                    viewProfileBinding.DVPImgAdmin.setImageBitmap(bitmap)
                    uploadProfileImg()
                } catch (e: IOException) {
                    Log.e("Errrr",e.message,e)                }
            } else {
                try { // Setting image on image view using Bitmap
                    bitmap = MediaStore.Images.Media
                        .getBitmap(
                            /* cr = */ contentResolver,
                            /* url = */ fileUri
                        )
                    viewProfileBinding.DVPImgAdmin.setImageBitmap(bitmap)
                    uploadProfileImg()
                } catch (e: IOException) {
                    // Log the exception
                    Log.e("Errrr",e.message,e)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewProfileBinding = DialogAdminViewProfileBinding.inflate(layoutInflater)
        changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardClickListeners()
        getAdminPicture()

        binding.ADAdminImage.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.ADAdminImage)
            popupMenu.menuInflater.inflate(
                R.menu.logged_admin_menu,
                popupMenu.menu
            )
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.view_profile -> {
                        dialogViewProfile()
                    }
                    R.id.change_password -> {
                        dialogChangePassword()
                    }
                    R.id.log_out -> {
                        val pref: SharedPreferences =
                            getSharedPreferences("adminPref", MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putBoolean("adminLogin", false).apply()
                        val i = Intent(
                            this,
                            LoginActivity::class.java
                        )
                        i.putExtra("SelectedUser", "admin")
                        startActivity(i)
                        finish()
                    }

                }
                true
            }
            popupMenu.show()
        }
    }

    private fun dialogViewProfile() {
        val viewProfileBinding =
            DialogAdminViewProfileBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(
            this,
            R.style.CustomAlertDialogViewProfile
        ).create()
        builder.window?.setGravity(Gravity.TOP)
        builder.setCancelable(true)
        builder.setView(viewProfileBinding.root)
        builder.setCanceledOnTouchOutside(true)
        viewProfileBinding.DVPAdminDepartment.visibility = View.GONE
        fireDb.child("Admin")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Glide.with(this@AdminDashboard)
                            .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                            .into(viewProfileBinding.DVPImgAdmin)
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
        viewProfileBinding.DVPImgAdmin.setOnClickListener {
            imagePicker.launch("image/*")
        }
        builder.show()
    }

    private fun dialogChangePassword() {
        try {
            changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
           val changePasswordDialog = AlertDialog.Builder(
                this,
                R.style.CustomAlertDialogEditProfile
            ).create()
            changePasswordDialog.window?.setGravity(Gravity.TOP)
            changePasswordDialog.setCancelable(true)
            changePasswordDialog.setView(changePasswordBinding.root)
            changePasswordDialog.setCanceledOnTouchOutside(false)
            changePasswordDialog.show()
        } catch (e: Exception) {
            Log.d("dialog Error-", "${e.message}")
        }

        changePasswordBinding.DEPBtnSubmit.setOnClickListener {
            validation()
            uploadData()
        }
    }

    private fun validation() {
        if (changePasswordBinding.DEPEdtOldPassword.text.toString().isNotEmpty()) adminOldPassword =
            changePasswordBinding.DEPEdtOldPassword.text.toString()
        else changePasswordBinding.DEPEdtOldPassword.error = "Please Enter your Old Password"

        if (changePasswordBinding.DEPEdtNewPassword.text.toString().isNotEmpty()) adminNewPassword =
            changePasswordBinding.DEPEdtNewPassword.text.toString()
        else changePasswordBinding.DEPEdtNewPassword.error = "Please Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString().isEmpty())
            changePasswordBinding.DEPEdtReNewPassword.error = "Please Re-Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString() == adminNewPassword) adminReNewPassword =
            changePasswordBinding.DEPEdtReNewPassword.text.toString()
        else changePasswordBinding.DEPEdtReNewPassword.error = "Password Does not match!!"


    }

    private fun uploadProfileImg() {
        if (fileUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            val storageRef =
                FirebaseStorage.getInstance().reference.child("Admin")
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
                                    fireDb
                                        .child("Admin")
                                        .child("profilePicUrl")
                                        .setValue(adminImgUrl)
                                        .addOnSuccessListener {
                                            Toast
                                                .makeText(
                                                    this,
                                                    "Image Uploaded👍",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            builder.dismiss()
                                        }
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
        if (adminOldPassword != "" && adminReNewPassword != "") {
            fireDb.child("Admin").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        if (adminOldPassword == snapshot.getValue(AdminLoginData::class.java)?.password) {
                            fireDb.child("Admin")
                                .setValue(
                                    AdminLoginData(
                                        snapshot.getValue(AdminLoginData::class.java)?.userId.toString(),
                                        snapshot.getValue(AdminLoginData::class.java)?.fullName.toString(),
                                        snapshot.getValue(AdminLoginData::class.java)?.profilePicUrl.toString(),
                                        adminReNewPassword,
                                    )
                                ).addOnSuccessListener {
                                    Toast
                                        .makeText(
                                            this@AdminDashboard,
                                            "Updated Successfully!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                        } else changePasswordBinding.DEPEdtOldPassword.error =
                            "Incorrect Old Password!!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun getAdminPicture() {
        fireDb.child("Admin")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        try {
                            Glide.with(this@AdminDashboard)
                                .load(snapshot.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                .into(binding.ADAdminImage)
                        } catch (e: Exception) {
                            Log.d("Errrrr", e.message, e)
                        }

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
                    DeleteNotice::class.java
                )
            )
        }
        binding.uploadImage.setOnClickListener {
          val i =  Intent(
                this,
                GalleryActivity::class.java
            )
            i.putExtra("by","admin")
            startActivity(i)
        }
        binding.addStudent.setOnClickListener {
            val i = Intent(
                this,
                ManageStudent::class.java
            )
            i.putExtra("userType", "admin")
            startActivity(i)
        }
        binding.updateFaculty.setOnClickListener {
            val i = Intent(
                this,
                ManageFaculty::class.java
            )
            i.putExtra("userType", "admin")
            startActivity(i)
        }
    }
}