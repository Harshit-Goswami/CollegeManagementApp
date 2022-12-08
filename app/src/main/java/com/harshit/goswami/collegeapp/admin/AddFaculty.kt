package com.harshit.goswami.collegeapp.admin
import android.app.ProgressDialog
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
import com.harshit.goswami.collegeapp.admin.dataClasses.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddFacultyBinding
import java.io.IOException
import java.util.*

class AddFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddFacultyBinding
    private var imgUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var dbRef: DatabaseReference? = null
    private var downloadImgUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("BScIT", "BMS", "BAF", "BMM")
        val adapter = ArrayAdapter(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            items
        )
        binding.department.setAdapter(adapter)
        binding.imgFaculty.setOnClickListener {
            getResult.launch("image/*")
        }
        binding.btnAddFaculty.setOnClickListener {
                val title = binding.facultyName.text.toString()
                if (title == "") {
                    binding.facultyName.error = "title is empty"
                    binding.facultyName.isFocusable = true
                } else if (imgUri == null) uploadData() else uploadImageAndData()
        }
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

    private fun uploadImageAndData() {
        if (imgUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            storageRef = FirebaseStorage.getInstance().reference.child("Faculty Images")
                .child(UUID.randomUUID().toString())
            // adding listeners on upload
            // or failure of image
            val uploadTask = storageRef!!.putFile(imgUri!!)
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
                        storageRef!!.downloadUrl.addOnSuccessListener {
                            downloadImgUrl = it.toString()
                            uploadData()
                        }
                    }
                }
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast
                    .makeText(
                        this,
                        "Failed ${it.message}",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }.addOnProgressListener { taskSnapshot ->
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
        val facultyName = binding.facultyName.text.toString()
        val facultyDepartment = binding.department.text.toString()

        dbRef = FirebaseDatabase.getInstance().reference.child("BScIT").child("FacultyData")
        val uniqueKey = dbRef!!.push().key
        dbRef!!.child(uniqueKey.toString()).setValue(FacultyData(facultyName,facultyDepartment, downloadImgUrl.toString()))
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