package com.harshit.goswami.collegeapp.teacher

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
import com.harshit.goswami.collegeapp.admin.ManageFaculty
import com.harshit.goswami.collegeapp.admin.ManageStudent
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherDashboardBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminEditProfileBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminViewProfileBinding
import java.io.IOException

class TeacherDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherDashboardBinding
    private lateinit var editProfileBinding: DialogAdminEditProfileBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private var fileUri: Uri? = null
    var teacherName = ""
    var teacherId = ""
    var teacherDepartment = ""
    var teacherImgUrl = ""
    var teacherPassword = ""
    companion object{
        var loggedTeacherName = ""
        var loggedTeacherType = ""
        var loggedTeacherDep = ""
    }
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)
        editProfileBinding = DialogAdminEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val teacherPref: SharedPreferences =
            getSharedPreferences("teacherPref", MODE_PRIVATE)
        loggedTeacherName = teacherPref.getString("TeacherName", "No Faculty").toString()
        loggedTeacherType = teacherPref.getString("teacherType", "No type").toString()
        loggedTeacherDep  = teacherPref.getString("teacherDep", "No dep").toString()
        getTeacherPicture()
        onClicklisteners()
        if (loggedTeacherType == "teacher"){
            binding.TDHodContainer.visibility = View.GONE
        }
    }
    private fun onClicklisteners() {
        binding.TDUploadNotes.setOnClickListener {
            val i =  Intent(
                this,
                UploadNotes::class.java
            )
            i.putExtra("clickedButton","notes")
            startActivity(i)
        }
        binding.TDUploadAssignment.setOnClickListener {
            val i =  Intent(
                this,
                UploadNotes::class.java
            )
            i.putExtra("clickedButton","assignment")
            startActivity(i)
        }
        binding.TDTeacherImage.setOnClickListener {
            if (LoginActivity.isTeacherLogin) {
                loggedTeacherName = intent.getStringExtra("LoggedTeacher").toString()
                val popupMenu = PopupMenu(this, binding.TDTeacherImage)
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
                                getSharedPreferences("teacherPref", MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putBoolean("teacherLogin", false).apply()
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
        binding.TDManageClass.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadClass::class.java
                )
            )
        }
        binding.TDAddEvent.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadedEvents::class.java
                )
            )
        }
        binding.TDManageFaculty.setOnClickListener {
            val i =  Intent(
                this,
                ManageFaculty::class.java
            )
            i.putExtra("userType","HOD")
            startActivity(i)
        }
        binding.TDYourStudents.setOnClickListener {
            val i =  Intent(
                this,
                ManageStudent::class.java
            )
            i.putExtra("userType","HOD")
            startActivity(i)
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

        builder.setCancelable(true)
        builder.setView(viewProfileBinding.root)
        builder.setCanceledOnTouchOutside(true)
        loggedTeacherName = intent.getStringExtra("LoggedTeacher").toString()

        Toast.makeText(
            this@TeacherDashboard,
            loggedTeacherName,
            Toast.LENGTH_SHORT
        ).show()

        FirebaseDatabase.getInstance().reference.child("BScIT").child("Faculty Data")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            if (data.key.toString() == loggedTeacherName) {
                                Glide.with(this@TeacherDashboard)
                                    .load(data.getValue(FacultyData::class.java)!!.downloadUrl)
                                    .into(viewProfileBinding.DVPImgAdmin)

                                viewProfileBinding.DVPAdminName.text =
                                    data.getValue(FacultyData::class.java)!!.name
                                viewProfileBinding.DVPAdminDepartment.text =
                                    data.getValue(FacultyData::class.java)!!.department
                            }
                        }
                    } else Toast.makeText(
                        this@TeacherDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@TeacherDashboard,
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
        val editProfileBinding = DialogAdminEditProfileBinding.inflate(layoutInflater)
        try {
            val editProfileDialog = AlertDialog.Builder(
                this,
                com.harshit.goswami.collegeapp.R.style.CustomAlertDialogEditProfile
            ).create()
            editProfileDialog.window?.setGravity(Gravity.TOP)
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
        fireDb.child("BScIT").child("Faculty Data")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            if (data.key.toString() == loggedTeacherName) {
                                Glide.with(this@TeacherDashboard)
                                    .load(data.getValue(FacultyData::class.java)!!.downloadUrl)
                                    .into(editProfileBinding.DEPImgAdmin)
                                editProfileBinding.DEPEdtFullname.setText(data.getValue(FacultyData::class.java)!!.name)
//                                editProfileBinding.DEPEdtDepartment.setText(
//                                    data.getValue(
//                                        FacultyData::class.java
//                                    )!!.department
//                                )
                                editProfileBinding.DEPEdtPassword.setText(data.getValue(FacultyData::class.java)!!.password)
                                editProfileBinding.DEPEdtUserId.setText(data.getValue(FacultyData::class.java)!!.contactNo)
                            }
                        }

                    } else Toast.makeText(
                        this@TeacherDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@TeacherDashboard,
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
        if (editProfileBinding.DEPEdtFullname.text.toString() != "") {
            teacherName =
                editProfileBinding.DEPEdtFullname.text.toString()
        } else editProfileBinding.DEPEdtFullname.error = "this field Should not be Empty"

//        if (editProfileBinding.DEPEdtDepartment.text.toString() != "") {
//            teacherDepartment =
//                editProfileBinding.DEPEdtDepartment.text.toString()
//        } else editProfileBinding.DEPEdtDepartment.error = "this field Should not be Empty"

        if (editProfileBinding.DEPEdtUserId.text.toString() != "") {
            teacherId =
                editProfileBinding.DEPEdtUserId.text.toString()
        } else editProfileBinding.DEPEdtUserId.error = "this field Should not be Empty"

        if (editProfileBinding.DEPEdtPassword.text.toString() != "") {
            teacherPassword =
                editProfileBinding.DEPEdtPassword.text.toString()
        } else editProfileBinding.DEPEdtPassword.error = "Please Enter your Password"

    }

    private fun uploadImgAndData() {
        if (fileUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            val storageRef =
                FirebaseStorage.getInstance().reference.child("Faculty Images")
                    .child(loggedTeacherName)
            // adding listeners on upload
            // or failure of image
            val uploadTask = storageRef.putFile(fileUri!!)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.addOnSuccessListener {

                        progressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        storageRef.downloadUrl
                            .addOnSuccessListener { uri: Uri ->
                                teacherImgUrl = uri.toString()
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
            fireDb.child("BScIT").child("Faculty Data").child(loggedTeacherName)
                .setValue(
                    FacultyData(
                        teacherName,
                        teacherDepartment,
                        teacherImgUrl,
                        teacherId,
                        teacherPassword,
                        loggedTeacherType
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
//        } else Toast
//            .makeText(
//                this,
//                "Field is empty!",
//                Toast.LENGTH_SHORT
//            )
//            .show()
    }

    private fun getTeacherPicture() {
        fireDb.child("Faculty Data").child(loggedTeacherDep)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        if(loggedTeacherType == "HOD"){
                        if (snapshot.child("HOD").key.toString() == "HOD"){
                            Glide.with(this@TeacherDashboard)
                                .load(snapshot.child("HOD").getValue(FacultyData::class.java)!!.downloadUrl)
                                .into(binding.TDTeacherImage)
                        }
                    }
                        if (loggedTeacherType == "teacher"){
                            for (data in snapshot.children) {
                                if(data.key.toString() == loggedTeacherName)
                                    Glide.with(this@TeacherDashboard)
                                        .load(data.getValue(FacultyData::class.java)!!.downloadUrl)
                                        .into(binding.TDTeacherImage)
                            }
                        }
                    } else Toast.makeText(
                        this@TeacherDashboard,
                        "snapshot not exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@TeacherDashboard,
                        "Error!-${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

}