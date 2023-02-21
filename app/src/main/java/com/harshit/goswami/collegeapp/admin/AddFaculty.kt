package com.harshit.goswami.collegeapp.admin

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddFacultyBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard
import java.io.IOException

class AddFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddFacultyBinding
    private var facultyDepartment = ""
    private var facultyName = ""
    private var facultyContactNo = ""
//    private var facultyQualifications = ""
//    private var facultyPassword = ""
    private var imgUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var  dbRef = FirebaseDatabase.getInstance().reference.child("Faculty Data")
    private var downloadImgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ManageFaculty.loggedUser == "HOD"){
            binding.TILdepartment.visibility = View.GONE
        }
        settingAutoCompleteTextView()
        onClickListeners()

    }

    private val getResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        val bitmap: Bitmap?
        if (result != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    imgUri = result
                    val source = ImageDecoder.createSource(contentResolver, imgUri!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    binding.imgFaculty.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                try {// Setting image on image view using Bitmap
                    bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                            contentResolver,
                            imgUri
                        )
                    binding.imgFaculty.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }

    private fun settingAutoCompleteTextView() {
        val items = listOf("BScIT", "BMS", "BAF", "BAMMC")
        val adapter = ArrayAdapter(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            items
        )
        binding.department.setAdapter(adapter)

    }

    private fun onClickListeners() {
        binding.imgFaculty.setOnClickListener {
            getResult.launch("image/*")
        }
        binding.btnAddFaculty.setOnClickListener {
            validation()
            if (imgUri == null) uploadData() else uploadImageAndData()
        }
    }

    private fun validation() {
        if (binding.facultyName.text.toString().isNotEmpty()) {
            facultyName = binding.facultyName.text.toString()
        } else {
            binding.facultyName.error = "this field Should not be Empty"
        }
        if (android.util.Patterns.PHONE.matcher(binding.ContactNo.text.toString())
                .matches()
            && binding.ContactNo.length() == 10
        ) {
            facultyContactNo = binding.ContactNo.text.toString()
        } else {
            binding.ContactNo.error = "Please enter Valid Number"
        }
if (ManageFaculty.loggedUser == "admin") {
    if (binding.department.text.toString().isNotEmpty()) {
            facultyDepartment = binding.department.text.toString()
        } else {
            binding.department.error = "please select department"
        }
}
//
//        if (binding.Qualification.text.toString().isNotEmpty()) {
//            facultyQualifications = binding.Qualification.text.toString()
//        } else {
//            binding.Qualification.error = "please Enter Qualifications."
//        }
//
//        if (binding.loginPass.text.toString().isNotEmpty() && binding.loginPass.length() >5) {
//            facultyPassword = binding.loginPass.text.toString()
//        } else if(binding.loginPass.text.toString().isEmpty()) {
//            binding.loginPass.error = "please Enter Password."
//        }else{
//            binding.loginPass.error = "password length should be greater than 5."
//        }
    }

    private fun uploadImageAndData() {
        if (imgUri != null) {
            if (ManageFaculty.loggedUser == "admin") {
                if (facultyName != "" && facultyDepartment != "" && facultyContactNo != "") {
                    // Defining the child of storageReference
                    storageRef =
                        FirebaseStorage.getInstance().reference.child("Faculty Images")
                            .child(facultyName+"(${facultyDepartment})")
                    storageRef!!.putFile(imgUri!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Image uploaded successfully
                                Toast.makeText(
                                    this,
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                storageRef!!.downloadUrl.addOnSuccessListener {
                                    downloadImgUrl = it.toString()
                                    uploadData()
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }
            if (ManageFaculty.loggedUser == "HOD") {
                if (facultyName != "" && facultyContactNo != "") {
                    // Defining the child of storageReference
                    storageRef =
                        FirebaseStorage.getInstance().reference.child("Faculty Images")
                            .child(facultyName+"(${facultyDepartment})")
                    storageRef!!.putFile(imgUri!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Image uploaded successfully
                                Toast.makeText(
                                    this,
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                storageRef!!.downloadUrl.addOnSuccessListener {
                                    downloadImgUrl = it.toString()
                                    uploadData()
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }
        }
    }

    private fun uploadData() {
        if (facultyName != "" && facultyContactNo != "") {
            if (ManageFaculty.loggedUser == "HOD"){
               dbRef.child(TeacherDashboard.loggedTeacherDep)
                   .child(facultyName)
                   .setValue(
                    FacultyData(
                        facultyName,
                        TeacherDashboard.loggedTeacherDep,
                        downloadImgUrl.toString(),
                        facultyContactNo,
                        facultyContactNo,
                        "teacher"
                    )
                )
                    .addOnSuccessListener {
                        Toast
                            .makeText(
                                this,
                                "Successfully Uploaded",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }.addOnFailureListener {
                        Toast
                            .makeText(
                                this,
                                "dataFailed " + it.message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
            }}
        if (facultyName != "" && facultyDepartment != "" && facultyContactNo != "") {
            if (ManageFaculty.loggedUser == "admin"){
                dbRef.child(binding.department.text.toString())
                .child(facultyName).setValue(
                    FacultyData(
                        facultyName,
                        facultyDepartment,
                        downloadImgUrl.toString(),
                        facultyContactNo,
                        facultyContactNo,
                        "HOD"
                    )
                )
                    .addOnSuccessListener {
                        Toast
                            .makeText(
                                this,
                                "Successfully Uploaded",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }.addOnFailureListener {
                        Toast
                            .makeText(
                                this,
                                "dataFailed " + it.message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
            }}
        }
    }
