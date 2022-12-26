package com.harshit.goswami.collegeapp.teacher

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherUploadNotesBinding
import java.util.*


class UploadNotes : AppCompatActivity() {
    private lateinit var bindind: ActivityTeacherUploadNotesBinding
    private var fileUri: Uri? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val dbRef: DatabaseReference? = null
    private var storageRef: StorageReference? = null
    private val getResult = registerForActivityResult(
        GetContent()
    ) { uri: Uri? ->
        if (uri!!.port == RESULT_OK) {
            fileUri = uri
            val BookName = bindind.txtBookName
            if (fileUri.toString().startsWith("content://")) {
                var cursor: Cursor? = null
                cursor =
                    this.contentResolver.query(fileUri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") val fileTitle: String =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    BookName.text = fileTitle
                }
            } else BookName.text = fileUri!!.queryParameterNames.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityTeacherUploadNotesBinding.inflate(layoutInflater)
        setContentView(bindind.root)
        bindind.cardSelectpdf.setOnClickListener { getResult.launch("application/*") }
        bindind.btnUploadBook.setOnClickListener {
            storageRef = firebaseStorage.reference
            storageRef!!.child(" Notes ").child(UUID.randomUUID().toString()).putFile(fileUri!!)
        }
    }
}