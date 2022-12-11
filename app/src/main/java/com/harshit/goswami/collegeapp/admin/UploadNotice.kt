package com.harshit.goswami.collegeapp.admin

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.harshit.goswami.collegeapp.admin.dataClasses.NoticeData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminUploadNoticeBinding
import java.io.IOException
import java.util.*


class UploadNotice : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUploadNoticeBinding
    private var fileUri: Uri? = null
    private var downloadUrl: String? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var dbRef: DatabaseReference? = null
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference? = null
    private val getResult = registerForActivityResult(
        GetContent()
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
        binding = ActivityAdminUploadNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.AddImage.setOnClickListener {
            getResult.launch(
                "image/*"
            )
        }
        binding.btnUploadNotice.setOnClickListener {
            val title = binding.edtNotice.text.toString()
            if (title == "") {
                binding.edtNotice.error = "title is empty"
                binding.edtNotice.isFocusable = true
            } else if (fileUri == null) uploadData() else uploadImgAndData()
        }
    }

    private fun uploadData() {
        val title: String = Objects.requireNonNull(binding.edtNotice.text).toString()
        var date = "Date"
        var time = "Time"
        dbRef = firebaseDatabase.reference
        val uniqueKey = dbRef!!.child("Notices").push().key
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calforDate: Calendar = Calendar.getInstance()
            val sdfDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            date = sdfDate.format(Calendar.getInstance().time)
            val sdfTime = SimpleDateFormat("hh:mm a ", Locale.getDefault())
            time = sdfTime.format(Calendar.getInstance().time)
        }
        val noticeData = NoticeData(title, date, time, uniqueKey, downloadUrl.toString())
        dbRef = firebaseDatabase.reference
        dbRef!!.child("Notices").child(uniqueKey!!).setValue(noticeData)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e: Exception ->
                Toast
                    .makeText(
                        this@UploadNotice,
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
                firebaseStorage.reference.child("NoticeImages").child(UUID.randomUUID().toString())
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
                            this@UploadNotice,
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
                                            this@UploadNotice,
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
                        this@UploadNotice,
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