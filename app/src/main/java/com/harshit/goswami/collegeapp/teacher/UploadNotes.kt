package com.harshit.goswami.collegeapp.teacher

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.goswami.collegeapp.dataClasses.NotesData
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherUploadNotesBinding


class UploadNotes : AppCompatActivity() {
    private lateinit var bindind: ActivityTeacherUploadNotesBinding
    private var fileUri: Uri? = null
    private var downloadUri = ""
    private val fireDb = FirebaseDatabase.getInstance().reference
    private var storageRef = FirebaseStorage.getInstance().reference
    private val getResult = registerForActivityResult(
        GetContent()
    ) { uri: Uri? ->
        if (uri!!.port == RESULT_OK) {
            fileUri = uri
            val bookName = bindind.txtBookName
            if (fileUri.toString().startsWith("content://")) {
                var cursor: Cursor? = null
                cursor =
                    this.contentResolver.query(fileUri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") val fileTitle: String =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    bookName.text = fileTitle
                }
            } else bookName.text = fileUri!!.queryParameterNames.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityTeacherUploadNotesBinding.inflate(layoutInflater)
        setContentView(bindind.root)
        bindind.cardSelectpdf.setOnClickListener { getResult.launch("application/*") }

        bindind.btnUploadBook.setOnClickListener {
            if (fileUri == null) Toast.makeText(this@UploadNotes,"Please select Pdf",Toast.LENGTH_SHORT).show()
            else uploadFileAndData()
        }
    }
    private fun uploadFileAndData(){
        storageRef.child("BScIT ").child("Notes").child(TeacherDashboard.loggedTeacherName).child(bindind.txtBookName.text.toString())
            .putFile(fileUri!!)
            .addOnSuccessListener {
                Toast.makeText(this@UploadNotes,"Pdf uploaded",Toast.LENGTH_SHORT).show()
                it.storage.downloadUrl.addOnSuccessListener {it1->
                    downloadUri = it1.toString()
                    uploadData()
                }.addOnFailureListener {e->
                    Toast.makeText(this@UploadNotes,"downloadUriError!-${e.message}",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this@UploadNotes,"Firebase Error!-${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadData(){
        fireDb.child("BScIT").child("Faculty Data")
            .child("Notes").child(bindind.edtBookTitle.text.toString())
            .setValue(NotesData(bindind.edtBookTitle.text.toString(),downloadUri,TeacherDashboard.loggedTeacherName))
            .addOnSuccessListener {
                Toast.makeText(this@UploadNotes,"Data Uploaded",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this@UploadNotes,"Firebase Error!-${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
}