package com.harshit.goswami.collegeapp.teacher

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.harshit.goswami.collegeapp.FCMnotificationSender
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ActivityAddNewEventBinding
import java.io.IOException
import java.util.*

class AddNewEvent : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewEventBinding
    private var fileUri: Uri? = null
    private var downloadUrl: String? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var dbRef: DatabaseReference? = null
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference? = null
    private lateinit var date: String
    private lateinit var time: String

    private val getResult = registerForActivityResult(
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
                    binding.previewImg.setImageBitmap(bitmap)
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
                    binding.previewImg.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddImage.setOnClickListener {
            getResult.launch(
                "image/*"
            )
        }

        binding.btnUploadEvent.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val sdfDate =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                date = sdfDate.format(Calendar.getInstance().time)
                val sdfTime =
                    SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
                time = sdfTime.format(Calendar.getInstance().time)
            }
            val title = binding.edtEventTitle.text.toString()
            if (title == "") {
                binding.edtEventTitle.error = "title is empty"
                binding.edtEventTitle.isFocusable = true
            } else if (fileUri == null) uploadData() else uploadImgAndData()
        }
    }

    private fun uploadData() {
        val title: String = Objects.requireNonNull(binding.edtEventTitle.text).toString()
        val description: String =
            Objects.requireNonNull(binding.edtNoticeDescription.text).toString()
        dbRef = firebaseDatabase.reference

        val noticeData = NoticeData(title,description,downloadUrl.toString(),date,time,"")
        FirebaseFirestore.getInstance().collection("Events").document(date+time).set(noticeData)
            .addOnSuccessListener {
                if (fileUri != null) {
                    FCMnotificationSender(
                        "/topics/all", title, "New Event Added!!",
                        "BIGPIC",
                        downloadUrl.toString(),//applicationContext,
                        this
                    ).sendNotifications()
                }
            }
            .addOnFailureListener { e: Exception ->
                Toast
                    .makeText(
                        this,
                        "DataFailed " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
    }

    private fun uploadImgAndData() {
        if (fileUri != null) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // Defining the child of storageReference
            storageRef =
                firebaseStorage.reference.child("EventImages").child(date+time)
            // adding listeners on upload
            // or failure of image
            val uploadTask = storageRef!!.putFile(fileUri!!)
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
                        storageRef!!.downloadUrl
                            .addOnSuccessListener { uri: Uri ->
                                downloadUrl = uri.toString()
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

}