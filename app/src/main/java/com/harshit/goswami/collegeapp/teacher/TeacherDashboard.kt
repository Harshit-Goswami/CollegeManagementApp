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
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.admin.ManageFaculty
import com.harshit.goswami.collegeapp.admin.ManageStudent
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherDashboardBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminChangePasswordBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminViewProfileBinding
import java.io.IOException

class TeacherDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherDashboardBinding
    private lateinit var viewProfileBinding :
        DialogAdminViewProfileBinding
    private lateinit var changePasswordBinding: DialogAdminChangePasswordBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private var fileUri: Uri? = null
    private lateinit var builder: AlertDialog
//    var teacherName = ""
//    var teacherId = ""

    //    var teacherDepartment = ""
    var teacherImgUrl = ""
    var teacherOldPassword = ""
    var teacherNewPassword = ""
    var teacherReNewPassword = ""


    companion object {
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
//                   viewProfileBinding.DVPImgAdmin.setImageBitmap(bitmap)
                    uploadProfileImage()

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
//                    viewProfileBinding.DVPImgAdmin.setImageBitmap(bitmap)
                    uploadProfileImage()
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewProfileBinding = DialogAdminViewProfileBinding.inflate(layoutInflater)
        changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val teacherPref: SharedPreferences =
            getSharedPreferences("teacherPref", MODE_PRIVATE)
        loggedTeacherName = teacherPref.getString("TeacherName", "No Faculty").toString()
        loggedTeacherType = teacherPref.getString("teacherType", "No type").toString()
        loggedTeacherDep = teacherPref.getString("teacherDep", "No dep").toString()
        getTeacherPicture()
        onClicklisteners()
        if (loggedTeacherType == "teacher") {
            binding.TDHodContainer.visibility = View.GONE
        }
    }

    private fun onClicklisteners() {
        binding.TDUploadNotes.setOnClickListener {
            val i = Intent(
                this,
                UploadNotes::class.java
            )
            i.putExtra("clickedButton", "notes")
            startActivity(i)
        }
        binding.TDUploadAssignment.setOnClickListener {
            val i = Intent(
                this,
                UploadNotes::class.java
            )
            i.putExtra("clickedButton", "assignment")
            startActivity(i)
        }
        binding.TDTeacherImage.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.TDTeacherImage)
            popupMenu.menuInflater.inflate(
                com.harshit.goswami.collegeapp.R.menu.logged_admin_menu,
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
                        setupLogout()
                    }

                }
                true
            }

            popupMenu.show()
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
            val i = Intent(
                this,
                ManageFaculty::class.java
            )
            i.putExtra("userType", "HOD")
            startActivity(i)
        }
        binding.TDYourStudents.setOnClickListener {
            val i = Intent(
                this,
                ManageStudent::class.java
            )
            i.putExtra("userType", "HOD")
            startActivity(i)
        }
    }

    private fun setupLogout() {
        val pref: SharedPreferences =
            getSharedPreferences("teacherPref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("teacherLogin", false).apply()
        editor.putString("TeacherName", "").apply()
        editor.putString("teacherType", "").apply()
        editor.putString("teacherDep", "").apply()
        val i = Intent(
            this,
            LoginActivity::class.java
        )
        i.putExtra("SelectedUser", "teacher")
        startActivity(i)
        finish()
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

        Toast.makeText(
            this@TeacherDashboard,
            loggedTeacherName,
            Toast.LENGTH_SHORT
        ).show()

        FirebaseDatabase.getInstance().reference.child("Faculty Data").child(loggedTeacherDep)
            .child(
                loggedTeacherName
            )
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Glide.with(this@TeacherDashboard)
                            .load(snapshot.getValue(FacultyData::class.java)!!.downloadUrl)
                            .into(viewProfileBinding.DVPImgAdmin)

                        viewProfileBinding.DVPAdminName.text =
                            snapshot.getValue(FacultyData::class.java)!!.name
                        viewProfileBinding.DVPAdminDepartment.text =
                            snapshot.getValue(FacultyData::class.java)!!.department

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
        viewProfileBinding.DVPImgAdmin.setOnClickListener {
            imagePicker.launch("image/*")

        }
        builder.show()
    }
private fun uploadProfileImage(){
    if (fileUri!= null){
        // Code for showing progressDialog while uploading
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        // Defining the child of storageReference
        val storageRef =
            FirebaseStorage.getInstance().reference.child("Faculty Images")
                .child("$loggedTeacherName(${loggedTeacherDep})")
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
                                fireDb.child("Faculty Data").child(loggedTeacherDep).child(
                                    loggedTeacherName).child("downloadUrl").setValue(teacherImgUrl)
                                    .addOnSuccessListener {
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
    private fun dialogChangePassword() {
        try {
            changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
            val editProfileDialog = AlertDialog.Builder(
                this,
                R.style.CustomAlertDialogEditProfile
            ).create()
            editProfileDialog.window?.setGravity(Gravity.TOP)
            editProfileDialog.setCancelable(true)
            editProfileDialog.setView(changePasswordBinding.root)
            editProfileDialog.setCanceledOnTouchOutside(false)
            editProfileDialog.show()
        } catch (e: Exception) {
            Log.d("dialog Error-", "${e.message}")
        }


        changePasswordBinding.DEPBtnSubmit.setOnClickListener {
            validation()
            uploadData()
        }

    }

    private fun validation() {
        if (changePasswordBinding.DEPEdtOldPassword.text.toString()
                .isNotEmpty()
        ) teacherOldPassword =
            changePasswordBinding.DEPEdtOldPassword.text.toString()
        else changePasswordBinding.DEPEdtOldPassword.error = "Please Enter your Old Password"

        if (changePasswordBinding.DEPEdtNewPassword.text.toString()
                .isNotEmpty()
        ) teacherNewPassword =
            changePasswordBinding.DEPEdtNewPassword.text.toString()
        else changePasswordBinding.DEPEdtNewPassword.error = "Please Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString().isEmpty())
            changePasswordBinding.DEPEdtReNewPassword.error = "Please Re-Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString() == teacherNewPassword) teacherReNewPassword =
            changePasswordBinding.DEPEdtReNewPassword.text.toString()
        else changePasswordBinding.DEPEdtReNewPassword.error = "Password Does not match!!"

    }

/*
    private fun uploadImgAndData() {
        if (fileUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            val storageRef =
                FirebaseStorage.getInstance().reference.child("Faculty Images")
                    .child("$loggedTeacherName(${loggedTeacherDep})")
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
*/

    private fun uploadData() {
        if (teacherOldPassword != "" && teacherReNewPassword != "") {
            fireDb.child("Faculty Data").child(loggedTeacherDep)
                .child(loggedTeacherName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (teacherOldPassword == snapshot.getValue(FacultyData::class.java)?.password) {
                                fireDb.child("Faculty Data").child(loggedTeacherDep)
                                    .child(loggedTeacherName)
                                    .setValue(
                                        FacultyData(
                                            loggedTeacherName,
                                            loggedTeacherDep,
                                            snapshot.getValue(FacultyData::class.java)?.downloadUrl.toString(),
                                            snapshot.getValue(FacultyData::class.java)?.contactNo.toString(),
                                            teacherReNewPassword,
                                            snapshot.getValue(FacultyData::class.java)?.teacherType.toString()
                                        )
                                    ).addOnSuccessListener {
                                        Toast
                                            .makeText(
                                                this@TeacherDashboard,
                                                "Updated Successfully!",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
//                                        setupLogout()
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

    private fun getTeacherPicture() {
        try {
            fireDb.child("Faculty Data").child(loggedTeacherDep).child(loggedTeacherName)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            try {
                                Glide.with(this@TeacherDashboard)
                                    .load(snapshot.getValue(FacultyData::class.java)!!.downloadUrl)
                                    .into(binding.TDTeacherImage)
                            } catch (_: Exception) {
                            }
                        } else {
                            setupLogout()
                            Toast.makeText(
                                this@TeacherDashboard,
                                "snapshot not exist!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@TeacherDashboard,
                            "Error!-${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        } catch (e: Exception) {
            Log.e("profile image error", e.message.toString())
        }
    }

}