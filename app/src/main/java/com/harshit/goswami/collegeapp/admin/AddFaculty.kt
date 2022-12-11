package com.harshit.goswami.collegeapp.admin

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.goswami.collegeapp.admin.DeleteNotice.Companion.binding
import com.harshit.goswami.collegeapp.admin.dataClasses.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddFacultyBinding
import java.io.IOException
import java.util.*

class AddFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddFacultyBinding
    private var facultyDepartment = ""
    private var facultyName = ""
    private var facultyContactNo = ""
    private var facultyQualifications = ""
    private var facultyPassword = ""
    private var imgUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var dbRef: DatabaseReference? = null
    private var downloadImgUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val items = listOf("BScIT", "BMS", "BAF", "BMM")
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
    private fun validation(){
        if (binding.facultyName.text.toString().isNotEmpty()) {
            facultyName = binding.facultyName.text.toString()
        } else {
            binding.facultyName.error = "this field Should not be Empty"
        }
        if (android.util.Patterns.PHONE.matcher(binding.ContactNo.text.toString())
                .matches()
            && binding.ContactNo.length() == 10 ) {
            facultyContactNo = binding.ContactNo.text.toString()
        } else {
            binding.ContactNo.error = "Please enter Valid Number"
        }

        if (binding.department.text.toString().isNotEmpty()) {
            facultyDepartment = binding.department.text.toString()
        } else {
            binding.department.error = "please select department"
        }

        if (binding.Qualification.text.toString().isNotEmpty()) {
            facultyQualifications = binding.Qualification.text.toString()
        } else {
            binding.Qualification.error = "please Enter Qualifications."
        }

        if (binding.loginPass.text.toString().isNotEmpty() && binding.loginPass.length() >5) {
            facultyPassword = binding.loginPass.text.toString()
        } else if(binding.loginPass.text.toString().isEmpty()) {
            binding.loginPass.error = "please Enter Password."
        }else{
            binding.loginPass.error = "password length should be greater than 5."

        }
    }
    private fun uploadImageAndData() {
        if (imgUri != null) {
            if (facultyName != "" && facultyDepartment != "" && facultyContactNo != "" && facultyQualifications != "") {
            // Defining the child of storageReference
            storageRef =
                FirebaseStorage.getInstance().reference.child("Faculty Images").child("BScTI")
                    .child(UUID.randomUUID().toString())

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
          /*              .addOnProgressListener { taskSnapshot ->
                val progress = (100.0
                        * taskSnapshot.bytesTransferred
                        / taskSnapshot.totalByteCount)
                progressDialog.setMessage(
                    "Uploaded "
                            + progress.toInt() + "%"
                )*/
            }
        }
    }
    private fun uploadData() {

        if (facultyName != "" && facultyDepartment != "" && facultyContactNo != "" && facultyQualifications != "" && facultyPassword != "") {
            dbRef = FirebaseDatabase.getInstance().reference.child("BScIT").child("Faculty Data")
            dbRef!!.child(facultyName).setValue(
                FacultyData(
                    facultyName,
                    facultyDepartment,
                    downloadImgUrl.toString(),
                    facultyContactNo,
                    facultyPassword
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
        }
    }




}