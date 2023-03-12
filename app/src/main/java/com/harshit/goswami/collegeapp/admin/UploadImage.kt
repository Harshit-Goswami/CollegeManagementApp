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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.goswami.collegeapp.databinding.ActivityAdminUploadImageBinding
import java.io.IOException
import java.util.*


class UploadImage : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUploadImageBinding
//    private var category: String? = null
    private var imgUri: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    private var database: FirebaseDatabase? = null
//    private var dbRef: DatabaseReference? = null
    private var downloadImgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val items = arrayOf("Select category:", "Convocation", "Independence Day", "Other events")
        binding.imageCategory.adapter =
            ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, items)
       // binding.imageCategory.setOnItemClickListener { _, _, _, _ ->  }
        binding.imageCategory.selectedItem.toString()
        binding.selectImgGallery.setOnClickListener { getResult.launch("image/*") }
        binding.btnUploadImage.setOnClickListener {
            if (imgUri == null) {
                Toast.makeText(this, " Please Select Image ", Toast.LENGTH_SHORT).show()
            } else if (binding.imageCategory.selectedItem.toString() == "Select category") {
                Toast.makeText(this, " Please Select Image Category ", Toast.LENGTH_SHORT).show()
            } else uploadImage()

        }
    }

    private fun uploadImage() {
        if (imgUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            storageRef = storage?.reference?.child("GalleryImages")?.child( binding.imageCategory.selectedItem.toString())
                ?.child(UUID.randomUUID().toString())
            // adding listeners on upload
            // or failure of image
            val uploadTask = storageRef!!.putFile(imgUri!!)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.addOnSuccessListener {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss()
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
        FirebaseFirestore.getInstance().collection("Campus Gallery Images")
            .document(binding.imageCategory.selectedItem.toString())
            .collection("images").document().set(mapOf("url" to downloadImgUrl.toString()) )
            .addOnSuccessListener {
                Toast
                    .makeText(
                        this,
                        "Image uploaded..",
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

    private val getResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        var bitmap: Bitmap?
        if (result != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    imgUri = result
                    val source = ImageDecoder.createSource(contentResolver, imgUri!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    binding.galleryPreviewImg.setImageBitmap(bitmap)
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
                    binding.galleryPreviewImg.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }
}
